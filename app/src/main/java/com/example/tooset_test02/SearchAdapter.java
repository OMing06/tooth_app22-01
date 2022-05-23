package com.example.tooset_test02;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    ArrayList<SearchModel.SearchItems> items = new ArrayList<>();
    SearchActivity mSearchActivity;
    private Context mContext;

    public SearchAdapter(SearchActivity searchActivity) {
        mSearchActivity = searchActivity;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mSearchActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.search_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        holder.setItem(items.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(SearchModel.SearchItems item) {
        items.add(item);
    }

    public void setItems(ArrayList<SearchModel.SearchItems> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_search_image;
        TextView tv_search_name, tv_search_price, tv_search_url;
        LinearLayout mainLayout3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_search_image = findViewById(R.id.iv_search_image);
            tv_search_name = findViewById(R.id.tv_search_name);
            tv_search_price = findViewById(R.id.tv_search_price);
            mainLayout3 = findViewById(R.id.mainLayout3);
        }

        void setItem(SearchModel.SearchItems item) {
            tv_search_name.setText(removeSpecialCha(item.title));
            tv_search_price.setText(item.lprice+" 원");


            String imageUrl = item.image;
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .error(R.drawable.no_picture_image)
                    .into(iv_search_image);


            mainLayout3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext = v.getContext();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link));
                    //intent.putExtra("link", String.valueOf(item.link));
                    mContext.startActivity(intent);
                }
            });

        }

        private <T extends View>T findViewById(int id) {
            return itemView.findViewById(id);
        }
    }

    private String removeSpecialCha(String text) {
        String strBuffer;
        String removeString;

        strBuffer = text.replace("<b>", "");
        removeString = strBuffer.replace("</b>", "");

        return removeString;
    }


}
