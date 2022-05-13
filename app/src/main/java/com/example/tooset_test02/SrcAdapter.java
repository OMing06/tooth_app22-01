package com.example.tooset_test02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SrcAdapter extends RecyclerView.Adapter<SrcAdapter.ViewHolder> {
    ArrayList<SrcModel.SearchItems> items = new ArrayList<>();
    SrcActivity mSrcActivity;

    public SrcAdapter(SrcActivity srcActivity) {
        mSrcActivity = srcActivity;
    }

    @NonNull
    @Override
    public SrcAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mSrcActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.search_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SrcAdapter.ViewHolder holder, int position) {
        holder.setItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(SrcModel.SearchItems item) {
        items.add(item);
    }

    public void setItems(ArrayList<SrcModel.SearchItems> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_search_image;
        TextView tv_search_name, tv_search_price, tv_search_url;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_search_image = findViewById(R.id.iv_search_image);
            tv_search_name = findViewById(R.id.tv_search_name);
            tv_search_price = findViewById(R.id.tv_search_price);
            tv_search_url = findViewById(R.id.tv_search_url);
        }

        void setItem(SrcModel.SearchItems item) {
            tv_search_name.setText(removeSpecialCha(item.title));
            tv_search_price.setText(item.lprice+" 원");
            tv_search_url.setText(item.link);


            /*String imageUrl;
            imageUrl = item.image;
            Glide.with(imageUrl)
                    .load(imageUrl)
                    .error(R.drawable.no_picture_image)
                    .into(iv_search_image);*/
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
