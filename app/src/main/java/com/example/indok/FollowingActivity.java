package com.example.indok;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.indok.MainRecyclerView.VerticalSpacingItemDecorator;
import com.example.indok.MainRecyclerView.VideoPlayerRecyclerAdapter;
import com.example.indok.MainRecyclerView.VideoPlayerRecyclerView;
import com.example.indok.Models.MediaObject;
import com.example.indok.Responses.ApiClient;
import com.example.indok.Responses.ApiInterface;
import com.example.indok.Responses.Users;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingActivity extends AppCompatActivity {
    private ArrayList<MediaObject> mediaObjectList=new ArrayList<>();
    private VideoPlayerRecyclerView recyclerView;
    public static ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        init();

    }

    private void init() {

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        recyclerView = (VideoPlayerRecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(0);
        recyclerView.addItemDecoration(itemDecorator);

        //////////////////////////////////////////////
        SnapHelper mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(recyclerView);
        //////////////////////////////////////////////

        LoadAllPosts();

    }

    private void LoadAllPosts() {





        Call<Users> call = apiInterface.performAllPosts();
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {

                if(response.isSuccessful())
                {
                    mediaObjectList = (ArrayList<MediaObject>) response.body().getAllPosts();

                    recyclerView.setMediaObjects(mediaObjectList);
                    VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(mediaObjectList, initGlide());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    recyclerView.setKeepScreenOn(true);
                    recyclerView.smoothScrollToPosition(mediaObjectList.size()+1);
                }
                else
                {
                    Toast.makeText(FollowingActivity.this, "Network Error.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(FollowingActivity.this, "Network Error.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on){
        Window win=activity.getWindow();
        WindowManager.LayoutParams winParams=win.getAttributes();
        if(on)
        {
            winParams.flags |=bits;
        }else{
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.color.colorPrimaryDark)
                .error(R.color.colorPrimaryDark);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    @Override
    protected void onDestroy() {
        if(recyclerView!=null)
            recyclerView.releasePlayer();
        super.onDestroy();
    }
    @Override
    protected void onStop() {
        super.onStop();

        if(recyclerView!=null)
            recyclerView.releasePlayer();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    public void foryouBtn(View view) {
        Intent intent=new Intent(FollowingActivity.this,HomeActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSwipeLeft(this);
        finish();
    }
}