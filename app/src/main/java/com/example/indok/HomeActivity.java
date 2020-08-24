package com.example.indok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

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
import com.example.indok.VideoEditorFolder.PortraitCameraActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity<Protected> extends AppCompatActivity {

private ArrayList<MediaObject> mediaObjectList=new ArrayList<>();
private VideoPlayerRecyclerView recyclerView;
    public static ApiInterface apiInterface;
    ///////////////////////////////////////
    private static final int CAMERA_PERMISSION_REQUEST_CODE=88888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                        Toast.makeText(HomeActivity.this, "Network Error.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Users> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "Network Error.", Toast.LENGTH_SHORT).show();

                }
            });
    }

    public static void setWindowFlag( Activity activity, final int bits, boolean on){
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

    public void followingBtn(View view) {

        Intent intent=new Intent(HomeActivity.this,FollowingActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSwipeRight(this);
        finish();
    }

    public void addBtn(View view) {

        checkPermission();

        Intent intent=new Intent(HomeActivity.this, PortraitCameraActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideUp(this);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPermission();
    }

    private void checkPermission(){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return;
        }
        if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        switch (requestCode){
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(HomeActivity.this,"permission has been granted",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(HomeActivity.this,"[WARN] permission is not granted",Toast.LENGTH_SHORT).show();
                }
                break;
        }
}

    public void discoverPage(View view) {
        Intent intent=new Intent(HomeActivity.this,DiscoverActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideUp(this);
        finish();
    }

    public void accountPage(View view) {
        Intent intent=new Intent(HomeActivity.this,AccountActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideUp(this);
        finish();
    }

    public void messagePage(View view) {
        Intent intent=new Intent(HomeActivity.this,MessageActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideUp(this);
        finish();
    }
}