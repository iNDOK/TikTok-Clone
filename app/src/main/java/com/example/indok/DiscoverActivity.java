package com.example.indok;



import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiscoverActivity extends AppCompatActivity {
    EditText search_edit_text;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    ArrayList<String> fullNameList;
    ArrayList<String> userNameList;
    ArrayList<String> profilePicList;
    SearchAdapter searchAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        search_edit_text=findViewById(R.id.search_edit_text);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerview);


        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        fullNameList=new ArrayList<>();
        userNameList=new ArrayList<>();
        profilePicList=new ArrayList<>();

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()){

                    setAdapter(editable.toString());
                }else {
                    fullNameList.clear();
                    userNameList.clear();
                    profilePicList.clear();
                    recyclerView.removeAllViews();
                }
            }
        });

    }

    private void setAdapter(String searchedString) {

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullNameList.clear();
                userNameList.clear();
                profilePicList.clear();
                recyclerView.removeAllViews();

                int counter=0;
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    String uid=snapshot1.getKey();
                    String full_name=snapshot1.child("full_name").getValue(String.class);
                    String user_name=snapshot1.child("user_name").getValue(String.class);
                    String profile_Pic=snapshot1.child("profile_Pic").getValue(String.class);

                    if (full_name.toLowerCase().contains(searchedString.toLowerCase())){
                        fullNameList.add(full_name);
                        userNameList.add(user_name);
                        profilePicList.add(profile_Pic);
                        counter++;
                    }else if(user_name.toLowerCase().contains(searchedString.toLowerCase())){
                        fullNameList.add(full_name);
                        userNameList.add(user_name);
                        profilePicList.add(profile_Pic);
                        counter++;
                    }
                    if (counter==15)
                        break;
                }
                searchAdapter=new SearchAdapter(DiscoverActivity.this,fullNameList,userNameList,profilePicList);
                recyclerView.setAdapter(searchAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    public void backBtn(View view) {
        Intent intent=new Intent(DiscoverActivity.this,HomeActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideDown(this);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(DiscoverActivity.this,HomeActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideDown(this);
        finish();
    }

}
