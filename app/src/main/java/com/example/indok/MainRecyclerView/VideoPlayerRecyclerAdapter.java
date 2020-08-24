package com.example.indok.MainRecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RequestManager;
import com.example.indok.Models.MediaObject;
import com.example.indok.R;

import java.util.ArrayList;


public class VideoPlayerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MediaObject> mediaObjects;
    private RequestManager requestManager;


    public VideoPlayerRecyclerAdapter(ArrayList<MediaObject> mediaObjects, RequestManager requestManager) {
        this.mediaObjects = mediaObjects;
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new com.example.indok.MainRecyclerView.VideoPlayerViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_mainpage, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((com.example.indok.MainRecyclerView.VideoPlayerViewHolder)viewHolder).onBind(mediaObjects.get(i), requestManager);
    }

    @Override
    public int getItemCount() {
        return mediaObjects.size();
    }

}
