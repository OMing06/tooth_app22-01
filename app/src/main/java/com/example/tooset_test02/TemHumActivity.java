package com.example.tooset_test02;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    //Button on_btn, off_btn, bt_btn;
    Button bt_btn, shared_btn;
    Switch switch_onOff, switch_auto;
    LinearLayout gradLayout;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH_AUTO = "switchAuto";
    private boolean switchOnOff, swAuto;

    BluetoothAdapter bluetoothAdapter;
    UUID uuid;

    private static final int REQUEST_ENABLE_BT = 10; // 블루투스 활성화 상태
    private Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋
    private BluetoothDevice bluetoothDevice; // 블루투스 디바이스
    private BluetoothSocket bluetoothSocket = null; // 블루투스 소켓
    private OutputStream outputStream = null; // 블루투스에 데이터를 출력하기 위한 출력 스트림
    private InputStream inputStream = null; // 블루투스에 데이터를 입력하기 위한 입력 스트림
    private Thread workerThread = null; // 문자열 수신에 사용되는 쓰레드
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
        //img_water2 = findViewById(R.id.img_water2);
        tv_tem = findViewById(R.id.tv_tem);
        tv_hum = findViewById(R.id.tv_hum);
        //on_btn = findViewById(R.id.on_btn);
        //off_btn = findViewById(R.id.off_btn);
        switch_onOff = findViewById(R.id.switch_onOff);
        bt_btn = findViewById(R.id.bt_btn);
        switch_auto = findViewById(R.id.switch_auto);
        gradLayout = findViewById(R.id.gradLayout);
        shared_btn = findViewById(R.id.shared_btn);

        /*on_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TemHumActivity.this, "팬을 켭니다.", Toast.LENGTH_SHORT).show();
                sendData("1");
            }
        });

        off_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TemHumActivity.this, "팬 작동을 종료합니다.", Toast.LENGTH_SHORT).show();
                sendData("2");
            }
        });*/


        shared_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        switch_onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Toast.makeText(TemHumActivity.this, "팬을 켭니다.", Toast.LENGTH_SHORT).show();
                    sendData("1");
                } else {
                    Toast.makeText(TemHumActivity.this, "팬 작동을 종료합니다.", Toast.LENGTH_SHORT).show();
                    sendData("2");
                }
            }
        });


        switch_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) { //true면
                    Toast.makeText(TemHumActivity.this, "Auto 모드로 전환합니다.", Toast.LENGTH_SHORT).show();
                    //sendData("3");

                    swAuto = true;receiveData();

                } else {
                    Toast.makeText(TemHumActivity.this, "Auto 모드를 종료합니다.", Toast.LENGTH_SHORT).show();
                    sendData("2");
                    swAuto = true;
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
            Toast.makeText(getApplicationContext(), "Bluetooth를 지원하지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "휴대폰 설정에서 OMing을 페어링 후 재접속해주세요.", Toast.LENGTH_LONG).show();

                //페어링 디바이스 목록 안 뜨면 앱 설청 들어가서 근처 기기 권한 허용하라고 안내하기
            }
        }

        //loadData();
        //updateViews();

    } //onCreate 끝

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putBoolean(SWITCH_AUTO, switch_auto.isChecked());

        editor.apply();

        Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switchOnOff = sharedPreferences.getBoolean(SWITCH_AUTO, false);
    }

    private void updateViews() {
        switch_auto.setChecked(switchOnOff);
    }





    private void selectBluetoothDevice() {
        devices = bluetoothAdapter.getBondedDevices();
        pariedDeviceCount = devices.size();

        if(pariedDeviceCount == 0) { //페어링 된 장치 없는 경우
            Toast.makeText(getApplicationContext(), "블루투스를 연결해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "OMing을 연결해주세요.", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this); //디바이스 선택 위한 다이얼로그 생성
            builder.setTitle("페어링 된 디바이스");

            List<String> list = new ArrayList<>();
            // 모든 디바이스의 이름을 리스트에 추가
            for(BluetoothDevice bluetoothDevice : devices) {
                list.add(bluetoothDevice.getName());
            }
            //list.add("취소");

            final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
            list.toArray(new CharSequence[list.size()]);
            builder.setItems(charSequences, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 해당 디바이스와 연결하는 함수 호출
                    connectDevice(charSequences[which].toString());
                }
            });

            // 뒤로가기 버튼 누를 때 창이 안닫히도록 설정
            builder.setCancelable(false);

            // 다이얼로그 생성
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    private void connectDevice(String deviceName) {
        //페어링 된 디바이스 모두 탐색
        for(BluetoothDevice tempDevice : devices) {
            // 사용자가 선택한 이름과 같은 디바이스로 설정하고 반복문 종료

            if(deviceName.equals(tempDevice.getName())) {
                bluetoothDevice = tempDevice;
                break;

            }
        }
        uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();

            // 데이터 송,수신 스트림을 얻어옵니다.
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            // 데이터 수신 함수 호출
            receiveData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveData() {
        final Handler handler = new Handler();
        //데이터 수신을 위한 버퍼 생성
        readBufferPosition = 0;
        readBuffer = new byte[1024];

        //데이터 수신을 위한 쓰레드 생성
        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        //데이터 수신 확인
                        int byteAvailable = inputStream.available();
                        //데이터 수신 된 경우
                        if (byteAvailable > 0) {
                            //입력 스트림에서 바이트 단위로 읽어옴
                            byte[] bytes = new byte[byteAvailable];
                            inputStream.read(bytes);
                            //입력 스트림 바이트를 한 바이트씩 읽어옴
                            for (int i = 0; i < byteAvailable; i++) {
                                byte tempByte = bytes[i];
                                //개행문자를 기준으로 받음 (한줄)
                                if (tempByte == '\n') {
                                    //readBuffer 배열을 encodeBytes로 복사
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    //인코딩 된 바이트 배열을 문자열로 변환
                                    final String message = new String(encodedBytes, "UTF-8");
                                    readBufferPosition = 0;
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            array = message.split(",", 3);

                                            tv_hum.setText(array[1] + "%");
                                            tv_tem.setText(array[0] + "ºC");

                                            humi=Integer.parseInt(array[1]);
                                            temp=Integer.parseInt(array[0]);

                                            //sendData("3");

                                            if(swAuto = true) {
                                                sendData("3");
                                            }




                                            if(humi > 59) {
                                                img_water.setImageResource(R.drawable.humidity2);
                                                gradLayout.setBackgroundResource(R.drawable.bg_gradient2);
                                            }

                                        } //run()
                                    });
                                }
                                else { // 개행 문자가 아닐 경우
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
            // 데이터 송신
            outputStream.write(text.getBytes());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}