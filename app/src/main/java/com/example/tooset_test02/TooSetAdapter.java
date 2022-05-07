package com.example.tooset_test02;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//리사이클러뷰 아이템 목록 표시 위해 어댑터 사용
public class TooSetAdapter extends RecyclerView.Adapter<TooSetAdapter.ViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList tooset_id, tooset_name, tooset_type, tooset_date, tooset_resdate, tooset_color;

    TooSetAdapter(Activity activity, Context context, ArrayList tooset_id, ArrayList tooset_name, ArrayList tooset_type, ArrayList tooset_date, ArrayList tooset_resdate, ArrayList tooset_color) {
        //생성자에서 데이터 리스트 객체 전달받음
        this.activity = activity;
        this.context = context;
        this.tooset_id = tooset_id;
        this.tooset_name = tooset_name;
        this.tooset_type = tooset_type;
        this.tooset_date = tooset_date;
        this.tooset_color = tooset_color;
        this.tooset_resdate = tooset_resdate; //날짜 계산결과

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context); //xml에 정의해둔 디자인대로 실제 메모리에 올려줌
        View view = inflater.inflate(R.layout.tooset_list, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) { //아이템 세팅
        holder.tv_tooset_id.setText(String.valueOf(tooset_id.get(position)));
        holder.tv_tooset_name.setText(String.valueOf(tooset_name.get(position)));
        holder.tv_tooset_type.setText(String.valueOf(tooset_type.get(position)));
        holder.tv_tooset_date.setText(String.valueOf(tooset_date.get(position)));
        holder.tv_tooset_resdate.setText(String.valueOf(tooset_resdate.get(position)));
        holder.tv_tooset_color.setBackgroundColor(Color.parseColor(String.valueOf(tooset_color.get(position))));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() { //카드를 클릭하면.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(tooset_id.get(position)));
                intent.putExtra("name", String.valueOf(tooset_name.get(position)));
                intent.putExtra("type", String.valueOf(tooset_type.get(position)));
                intent.putExtra("date", String.valueOf(tooset_date.get(position)));
                intent.putExtra("resdate", String.valueOf(tooset_resdate.get(position)));
                intent.putExtra("color", String.valueOf(tooset_color.get(position)));

                activity.startActivityForResult(intent, 1); //결과값 전달
            }
        });
    }

    @Override
    public int getItemCount() {
        return tooset_id.size();
    } //id값 1,2,3......

    public class ViewHolder extends RecyclerView.ViewHolder { //아이템 요소 저장

        TextView tv_tooset_id, tv_tooset_name, tv_tooset_type, tv_tooset_date, tv_tooset_resdate, tv_tooset_color;
        LinearLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_tooset_id = itemView.findViewById(R.id.tv_tooset_id);
            tv_tooset_name = itemView.findViewById(R.id.tv_tooset_name);
            tv_tooset_type = itemView.findViewById(R.id.tv_tooset_type);
            tv_tooset_date = itemView.findViewById(R.id.tv_tooset_date);
            tv_tooset_resdate = itemView.findViewById(R.id.tv_tooset_resdate);
            tv_tooset_color = itemView.findViewById(R.id.tv_tooset_color);
            mainLayout = itemView.findViewById(R.id.mainLayout);

            tv_tooset_color.setBackgroundColor(Color.WHITE);
        }
    }
}
