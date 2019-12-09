package com.example.bird.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bird.Login.LoginActivity;
import com.example.bird.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class SplashActivity extends AppCompatActivity {
    private static final int TIME=2000;//milisecond type
    private ImageView testLogo;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference ref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        testLogo=findViewById(R.id.imageTestLogo);
        testLogo.setColorFilter(getColor(R.color.colorFire));
        auth= FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ref = null;
                do{
                    database = FirebaseDatabase.getInstance();
                    ref = database.getReference();
                    storage=FirebaseStorage.getInstance();
                }while (ref==null);

                if(auth.getCurrentUser()!=null){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        },TIME);



    }
}
