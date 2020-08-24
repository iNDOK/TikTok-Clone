package com.example.indok.MainRecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RequestManager;
import com.example.indok.Models.MediaObject;
import com.example.indok.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoPlayerViewHolder extends RecyclerView.ViewHolder{
    FrameLayout media_container;
    TextView title;
    ImageView thumbnail,volumeControl,soundDisk;
    ProgressBar progressBar;
    View parent;
    RequestManager requestManager;

    public VideoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        media_container = itemView.findViewById(R.id.media_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.textView2);
        progressBar = itemView.findViewById(R.id.progressbar);
        volumeControl = itemView.findViewById(R.id.volume_control);
        soundDisk = itemView.findViewById(R.id.disc);

    }
    public void onBind(MediaObject mediaObject,RequestManager requestManager){
        this.requestManager=requestManager;
        parent.setTag(this);
        title.setText(mediaObject.getDescription()+",\n"+mediaObject.getDate());
        this.requestManager.load(mediaObject.getThumbnail()).into(thumbnail);



    }
}
