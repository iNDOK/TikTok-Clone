package com.example.indok;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    Context context;
    ArrayList<String> fullNameList;
    ArrayList<String> userNameList;
    ArrayList<String> profilePicList;
    class SearchViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView full_name,user_name;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage=(ImageView) itemView.findViewById(R.id.profileImage);
            full_name=(TextView) itemView.findViewById(R.id.full_name);
            user_name=(TextView) itemView.findViewById(R.id.user_name);


        }
    }


    public SearchAdapter(Context context, ArrayList<String> fullNameList, ArrayList<String> userNameList, ArrayList<String> profilePicList) {
        this.context = context;
        this.fullNameList = fullNameList;
        this.userNameList = userNameList;
        this.profilePicList = profilePicList;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_search_list_items,parent,false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.full_name.setText(fullNameList.get(position));
        holder.user_name.setText(userNameList.get(position));

        Glide.with(context).asBitmap().load(profilePicList.get(position)).placeholder(R.mipmap.ic_launcher_round).into(holder.profileImage);

    }



    @Override
    public int getItemCount() {
        return fullNameList.size();
    }
}

