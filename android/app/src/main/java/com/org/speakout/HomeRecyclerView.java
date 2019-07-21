package com.org.speakout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;

public class HomeRecyclerView extends androidx.recyclerview.widget.RecyclerView.Adapter<HomeRecyclerView.MyViewHolder> {


    private HomePageActivity homePageActivity;

    HomeRecyclerView(HomePageActivity homePageActivity) {
        homePageActivity = this.homePageActivity;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news_feed,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        TextView title, description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView = itemView.findViewById(R.id.row_title);
            description = itemView.findViewById(R.id.row_description);
        }
    }


}
