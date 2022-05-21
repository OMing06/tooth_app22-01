package com.example.tooset_test02;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
<<<<<<< HEAD
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
=======
>>>>>>> 0fa0245e94506fed904e9e9f6652f602ec035a00

public class ReviewAdapter extends FirebaseRecyclerAdapter<ReviewModel, ReviewAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private Context mContext;
<<<<<<< HEAD
    Boolean likeClick = false;
=======
>>>>>>> 0fa0245e94506fed904e9e9f6652f602ec035a00


    public ReviewAdapter(@NonNull FirebaseRecyclerOptions<ReviewModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull ReviewModel model) {
        holder.tv_review_title.setText(model.getTitle());
        holder.tv_review_good.setText(model.getGood_review());
        holder.tv_review_bad.setText(model.getBad_review());
        holder.tv_review_userName.setText(model.getReviewUserName());
        holder.tv_review_email.setText(model.getReviewUserEmail());
        holder.tv_now.setText(model.getNow_date());
        holder.rv_review_ratingBar.setRating(model.getRating());

        String imageUrl = null;
        imageUrl = model.getImageUrl();
        Glide.with(holder.itemView.getContext()).load(imageUrl).error(R.drawable.no_picture_image).into(holder.iv_review_image);


        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final String userId = firebaseUser.getUid();
        final String reviewKey = getRef(position).getKey();

        holder.getlikebtn(reviewKey,userId);
        holder.iv_review_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeClick = true;
                holder.likeReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(likeClick==true) {
                            if(snapshot.child(reviewKey).hasChild(userId)) {
                                holder.likeReference.child(reviewKey).child(userId).removeValue();
                                likeClick=false;
                            } else {
                                holder.likeReference.child(reviewKey).child(userId).setValue(true);
                                likeClick=false;
                            }
                        } else {}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.mainLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext = v.getContext();
                Intent intent = new Intent(mContext, ReviewDetail.class);
                intent.putExtra("title", String.valueOf(model.getTitle()));
                intent.putExtra("good_review", String.valueOf(model.getGood_review()));
                intent.putExtra("bad_review", String.valueOf(model.getBad_review()));
                intent.putExtra("reviewUserName", String.valueOf(model.getReviewUserName()));
                intent.putExtra("reviewUserEmail", String.valueOf(model.getReviewUserEmail()));
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

<<<<<<< HEAD
        TextView tv_review_title, tv_review_good, tv_review_bad, tv_review_userName, tv_now, tv_review_email, tv_review_likeCount;
        ImageView iv_review_image, iv_review_like;
        RatingBar rv_review_ratingBar;
        CardView reviewCardView;
        LinearLayout mainLayout2;
        DatabaseReference likeReference;
=======
        TextView tv_review_title, tv_review_good, tv_review_bad, tv_review_userName, tv_now, tv_review_email;
        ImageView iv_review_image;
        RatingBar rv_review_ratingBar;
        CardView reviewCardView;
        LinearLayout mainLayout2;
>>>>>>> 0fa0245e94506fed904e9e9f6652f602ec035a00

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_review_title = itemView.findViewById(R.id.tv_review_title);
            tv_review_good = itemView.findViewById(R.id.tv_review_good);
            tv_review_bad = itemView.findViewById(R.id.tv_review_bad);
            tv_review_userName = itemView.findViewById(R.id.tv_review_userName);
            tv_review_email = itemView.findViewById(R.id.tv_review_email);
            iv_review_image = itemView.findViewById(R.id.iv_review_image);
            rv_review_ratingBar = itemView.findViewById(R.id.rv_review_ratingBar);
            tv_now = itemView.findViewById(R.id.tv_now);
            reviewCardView = itemView.findViewById(R.id.reviewCardView);
            mainLayout2 = itemView.findViewById(R.id.mainLayout2);

            tv_review_likeCount = itemView.findViewById(R.id.tv_review_likeCount);
            iv_review_like = itemView.findViewById(R.id.iv_review_like);

        }


        public void getlikebtn(final String reviewKey, final String userId) {
            likeReference = FirebaseDatabase.getInstance().getReference("Likes");
            likeReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(reviewKey).hasChild(userId)) {
                        int likeCnt = (int)snapshot.child(reviewKey).getChildrenCount();
                        tv_review_likeCount.setText(likeCnt+"");
                        iv_review_like.setImageResource(R.drawable.ic_baseline_favorite_24);
                    } else {
                        int likeCnt = (int)snapshot.child(reviewKey).getChildrenCount();
                        tv_review_likeCount.setText(likeCnt+"");
                        iv_review_like.setImageResource(R.drawable.baseline_favorite_border_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}