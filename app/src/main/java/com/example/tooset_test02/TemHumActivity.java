package com.example.tooset_test02;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TemHumActivity extends AppCompatActivity {

    ImageView img_water;
    TextView tv_tem, tv_hum;
    Button bt_btn;
    Switch switch_onOff, switch_auto;
    LinearLayout gradLayout;

    BluetoothAdapter bluetoothAdapter;
    UUID uuid;

    private static final int REQUEST_ENABLE_BT = 10; // 블루투스 활성화 상태
    private Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋
    private BluetoothDevice bluetoothDevice; // 블루투스 디바이스
    private BluetoothSocket bluetoothSocket = null; // 블루투스 소켓
    private OutputStream outputStream = null; // 블루투스에 데이터를 출력하기 위한 출력 스트림
    private InputStream inputStream = null; // 블루투스에 데이터를 입력하기 위한 입력 스트림
    private Thread workerThread = null; // 문자열 수신에 사용되는 thread
    private byte[] readBuffer; // 수신 된 문자열을 저장하기 위한 버퍼
    private int readBufferPosition; // 버퍼 내 문자 저장 위치
    int pariedDeviceCount; //페어링 된 디바이스 크기 저장

    int temp = 0;
    int humi = 0;

    String[] array = {"0"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tem_hum);
        setTitle("humidity");

        img_water = findViewById(R.id.img_water);
        tv_tem = findViewById(R.id.tv_tem);
        tv_hum = findViewById(R.id.tv_hum);
        switch_onOff = findViewById(R.id.switch_onOff);
        bt_btn = findViewById(R.id.bt_btn);
        switch_auto = findViewById(R.id.switch_auto);
        gradLayout = findViewById(R.id.gradLayout);

        switch_onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    toastMessage("팬을 켭니다.");
                    sendData("1");
                } else {
                    toastMessage("팬 작동을 종료합니다.");
                    sendData("2");
                }
            }
        });


        switch_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == false) { //true면
                    toastMessage("Auto 모드를 종료합니다.");
                    sendData("2");
                } else {
                    toastMessage("Auto모드로 전환합니다.");
                }
            }
        });

        bt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBluetoothDevice();
            }
        });

        //위치권한 허용 코드. 없으면 나중에 주변장치 검색이 되지 않는다.
        String[] permission_list = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions(TemHumActivity.this, permission_list,  1);

        //블루투스 활성화 코드
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을 때
            toastMessage("Bluetooth를 지원하지 않는 기기입니다.");
        }
        else {
            // 디바이스가 블루투스를 지원 할 때
            if(bluetoothAdapter.isEnabled()) { //블루투스가 활성화 돼 있으면
                selectBluetoothDevice(); //디바이스 선택
            } else { // 블루투스가 비 활성화 상태 (기기에 블루투스가 꺼져있음)
                // 블루투스를 활성화 하기 위한 다이얼로그 출력
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BT);

                selectBluetoothDevice();
                toastMessage("휴대폰 설정에서 OMing을 페어링해주세요.");
                //페어링 디바이스 목록 안 뜨면 앱 설청 들어가서 근처 기기 권한 허용하라고 안내하기
            }
        }

    } //onCreate 끝





    private void selectBluetoothDevice() {
        devices = bluetoothAdapter.getBondedDevices();
        pariedDeviceCount = devices.size();

        if(pariedDeviceCount == 0) { //페어링 된 장치 없는 경우
            toastMessage("블루투스를 연결해주세요");
        } else {
            toastMessage("OMing을 연결해주세요.");
            AlertDialog.Builder builder = new AlertDialog.Builder(this); //디바이스 선택 위한 다이얼로그 생성
            builder.setTitle("페어링 된 디바이스");

            List<String> list = new ArrayList<>();
            for(BluetoothDevice bluetoothDevice : devices) {
                list.add(bluetoothDevice.getName()); //다이얼로그 목록에 페어링 돼 있는 디바이스 장치 추가
            }

            final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
            list.toArray(new CharSequence[list.size()]);
            builder.setItems(charSequences, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    connectDevice(charSequences[which].toString());
                }
            });
            builder.setCancelable(false); //뒤로가기 막기

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    private void connectDevice(String deviceName) {
        for(BluetoothDevice tempDevice : devices) {
            if(deviceName.equals(tempDevice.getName())) { //페어링 목록 중 사용자가 선택한 것으로 연결하고 반복문 종료
                bluetoothDevice = tempDevice;
                break;

            }
        }
        uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //시리얼 uuid
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect(); //기기 간 블루투스 연결

            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            receiveData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveData() {
        final Handler handler = new Handler();

        readBufferPosition = 0;
        readBuffer = new byte[1024];

        //데이터를 지속적으로 받아오기 위해 핸들러, 스레드를 생성한다.
        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        int byteAvailable = inputStream.available();
                        if (byteAvailable > 0) {
                            byte[] bytes = new byte[byteAvailable];
                            inputStream.read(bytes);
                            for (int i = 0; i < byteAvailable; i++) {
                                byte tempByte = bytes[i];
                                if (tempByte == '\n') { //엔터 기준으로 값 받아옴
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);

                                    final String message = new String(encodedBytes, "UTF-8");
                                    readBufferPosition = 0;
                                    handler.post(new Runnable() {
                                        @SuppressLint("Range")
                                        @Override
                                        public void run() {
                                            if(switch_auto.isChecked() == true) {
                                                //만약 스위치가 on돼 있으면 3이라는 데이터를 보낸다
                                                //onCreate안의 버튼리스너 안에서 sendData()를 했을 경우 값이 한 번만 보내지기에
                                                //습도 60도 이하인 시점에 스위치를 on 했을 경우 팬이 켜지지 않았다.
                                                //60도 이상인 시점에 스위치를 on했을 경우에만 팬이 돌아간다.
                                                //그러나 핸들러 스레드 안에서 데이터를 보내면 값이 (1초 간격으로)지속적으로 보내지기에
                                                //60도 이하인 시점에 스위치를 on 해도, 60도 이상이 되면 팬이 켜진다
                                                //따라서 이 곳에 sendData를 넣었다.
                                                sendData("3");
                                            }

                                            array = message.split(",", 3); //반점을 기준으로 받아온 메세지를 쪼개어 배열에 저장한다.

                                            tv_hum.setText(array[1] + "%");
                                            tv_tem.setText(array[0] + "ºC");

                                            humi=Integer.parseInt(array[1]);
                                            temp=Integer.parseInt(array[0]);

                                            if(humi > 61) {
                                                img_water.setImageResource(R.drawable.humidity2);
                                                tv_hum.setTextColor(Color.parseColor("#ff9a9e"));
                                                gradLayout.setBackgroundResource(R.drawable.bg_gradient2);
                                            } else if (humi <= 60) {
                                                img_water.setImageResource(R.drawable.humidity1);
                                                tv_hum.setTextColor(Color.parseColor("#66a6ff"));
                                                gradLayout.setBackgroundResource(R.drawable.bg_gradient1);
                                            }

                                        } //run() 끝
                                    });
                                }
                                else {
                                    readBuffer[readBufferPosition++] = tempByte;
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        // 1초마다 받아옴
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        workerThread.start();
    } //private void receiveData() 끝

    private void sendData(String text) {
        text += "\n"; //문자열에 개행문자 추가
        try{
            // 데이터 보내기. 위에 "1", "2", "3" 같은 거.
            outputStream.write(text.getBytes());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void toastMessage(String toastMessage) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }
}