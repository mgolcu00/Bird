package com.example.bird.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bird.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    String TAG = "TAG";
    private FirebaseAuth mAuth;
    EditText edtUserName;
    EditText edtPass;
    Button btnLogin;
    TextView txtToReg;
    Intent intent;
    DatabaseReference myRef;
    FirebaseDatabase database;
    Snackbar sb;
    ProgressBar pb;
    ImageView img;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        edtUserName = findViewById(R.id.edtUserName);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtToReg = findViewById(R.id.txtConnectReg);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        img=findViewById(R.id.imgLogin);
        img.setColorFilter(getColor(R.color.colorFire));
        pb = findViewById(R.id.progressBarLog);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = String.valueOf(edtUserName.getText());
                String Pass = String.valueOf(edtPass.getText());
                pb.setVisibility(View.VISIBLE);
                auth(userName, Pass, v);

            }
        });

        txtToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    void auth(String userName, final String Pass, final View v) {
        mAuth.signInWithEmailAndPassword(userName, Pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            sb.make(v, "Login success", Snackbar.LENGTH_LONG).show();
                            pb.setVisibility(View.INVISIBLE);
                            myRef.child(user.getUid()).child("pass").setValue(Pass);
                            Read(Objects.requireNonNull(user).getUid());


                            //intent = new Intent(getApplicationContext(), ChatActivity.class);
                            //startActivity(intent);


//                            intent = new Intent(getApplicationContext(), ChatRoomsActivity.class);
//                            startActivity(intent);

                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            sb.make(v, "User not found", Snackbar.LENGTH_LONG).show();
                            pb.setVisibility(View.INVISIBLE);

                        }


                    }
                });
    }

    void Read(final String url) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String name = (String) dataSnapshot.child(url).child("name").getValue();
                String lastname = (String) dataSnapshot.child(url).child("lastname").getValue();
                Log.d(TAG, "\nName     : " + name +
                        "\nLastname : " + lastname);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}
