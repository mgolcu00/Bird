package com.example.bird.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bird.Login.UserC;
import com.example.bird.Post.PostData;
import com.example.bird.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CreatePostActivity extends AppCompatActivity {

    private TextView TextVieww;
    private Button btnExit;
    private Button btnSend;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference("users");
    private DatabaseReference mRef2 = mDatabase.getReference("posts");
    String date;
    private String Url;
    private UserC user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_createpost);
        TextVieww = findViewById(R.id.maTxt);
        btnSend = findViewById(R.id.btnPostSend);
        btnExit = findViewById(R.id.btnPostExit);
        Url = mAuth.getCurrentUser().getUid();
        final PostData post = new PostData();
        post.setPosttext(String.valueOf(TextVieww.getText()));
        date = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date());


        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserC temp;
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                temp = dataSnapshot.child(Url).getValue(UserC.class);
                post.setUser(temp);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post.setCreatingDate(date);
                String uuid = UUID.randomUUID().toString();
                post.setPosttext(String.valueOf(TextVieww.getText()));
                mRef2.child(Url + uuid).setValue(post);
                Toast.makeText(getApplicationContext(), TextVieww.getText(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


    }
}
