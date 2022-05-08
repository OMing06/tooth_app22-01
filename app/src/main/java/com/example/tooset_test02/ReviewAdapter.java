package com.example.tooset_test02;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewAdapter extends FirebaseRecyclerAdapter<ReviewModel, ReviewAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
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

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_review_title = itemView.findViewById(R.id.tv_review_title);
            tv_review_good = itemView.findViewById(R.id.tv_review_good);
            tv_review_bad = itemView.findViewById(R.id.tv_review_bad);
            tv_review_userName = itemView.findViewById(R.id.tv_review_userName);
            iv_review_image = itemView.findViewById(R.id.iv_review_image);
            tv_now = itemView.findViewById(R.id.tv_now);
        }
    }


}