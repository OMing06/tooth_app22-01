package com.example.tooset_test02;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.Random;

public class ReviewAdapter extends FirebaseRecyclerAdapter<ReviewModel, ReviewAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private Context mContext;


    public ReviewAdapter(@NonNull FirebaseRecyclerOptions<ReviewModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull ReviewModel model) {
        holder.tv_review_title.setText(model.getTitle());
        holder.tv_review_good.setText(model.getGood_review());
        holder.tv_review_bad.setText(model.getBad_review());
        holder.tv_review_userName.setText(model.getReviewUserName());
        holder.tv_now.setText(model.getNow_date());
        holder.rv_review_ratingBar.setRating(model.getRating());
        //holder.reviewCardView.setBackgroundColor(Color.parseColor(model.getColorRandom()));

        String imageUrl = null;
        imageUrl = model.getImageUrl();
        //Picasso.get().load(imageUrl).error(R.drawable.no_picture_image).into(holder.iv_review_image);
        Glide.with(holder.itemView.getContext()).load(imageUrl).error(R.drawable.no_picture_image).into(holder.iv_review_image);

        holder.mainLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext = v.getContext();
                Intent intent = new Intent(mContext, ReviewDetail.class);
                intent.putExtra("title", String.valueOf(model.getTitle()));
                intent.putExtra("good_review", String.valueOf(model.getGood_review()));
                intent.putExtra("bad_review", String.valueOf(model.getBad_review()));
                intent.putExtra("reviewUserName", String.valueOf(model.getReviewUserName()));
                intent.putExtra("now_date", String.valueOf(model.getNow_date()));
                intent.putExtra("imageUrl", String.valueOf(model.getImageUrl()));

                mContext.startActivity(intent); //결과값 전달
            }
        });

    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list, parent, false);
        return new myViewHolder(view);
    }


    class myViewHolder extends RecyclerView.ViewHolder {

        TextView tv_review_title, tv_review_good, tv_review_bad, tv_review_userName, tv_now;
        ImageView iv_review_image;
        RatingBar rv_review_ratingBar;
        CardView reviewCardView;
        CheckBox checkBox;
        LinearLayout mainLayout2;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_review_title = itemView.findViewById(R.id.tv_review_title);
            tv_review_good = itemView.findViewById(R.id.tv_review_good);
            tv_review_bad = itemView.findViewById(R.id.tv_review_bad);
            tv_review_userName = itemView.findViewById(R.id.tv_review_userName);
            iv_review_image = itemView.findViewById(R.id.iv_review_image);
            rv_review_ratingBar = itemView.findViewById(R.id.rv_review_ratingBar);
            tv_now = itemView.findViewById(R.id.tv_now);
            reviewCardView = itemView.findViewById(R.id.reviewCardView);
            mainLayout2 = itemView.findViewById(R.id.mainLayout2);

        }
    }
}