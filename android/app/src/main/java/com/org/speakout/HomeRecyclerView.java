package com.org.speakout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.org.speakout.fragments.HomePageFragment;

import androidx.annotation.NonNull;

public class HomeRecyclerView extends androidx.recyclerview.widget.RecyclerView.Adapter<HomeRecyclerView.MyViewHolder> {


    private HomePageFragment homeFragment;

    public HomeRecyclerView(HomePageFragment homeFragment) {

       this.homeFragment = homeFragment;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news_feed,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(this.homeFragment.getArrayList().get(position).getTitle());
        holder.description.setText(this.homeFragment.getArrayList().get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return this.homeFragment.getArrayList().size();
    }

    public static class MyViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        TextView title, description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.row_title);
            description = itemView.findViewById(R.id.row_description);
        }
    }

}
