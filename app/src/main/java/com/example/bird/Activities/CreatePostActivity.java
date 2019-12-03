package com.example.bird.Activities;

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

public class CreatePostActivity extends AppCompatActivity {

    TextView TextVieww;
    Button btnExit;
    Button btnSend;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
    DatabaseReference mRef = mDatabase.getReference("users");
    DatabaseReference mRef2 = mDatabase.getReference("posts");

    String Url;
    UserC user;

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

        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserC temp;
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                temp= dataSnapshot.child(Url).getValue(UserC.class);
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
                post.setPosttext(String.valueOf(TextVieww.getText()));
                mRef2.child(Url).setValue(post);
                Toast.makeText(getApplicationContext(), TextVieww.getText(),Toast.LENGTH_LONG).show();
            }
        });



    }
}
