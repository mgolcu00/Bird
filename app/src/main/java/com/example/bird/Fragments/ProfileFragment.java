package com.example.bird.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.example.bird.Activities.SplashActivity;
import com.example.bird.Models.UserModel;
import com.example.bird.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.URL;


public class ProfileFragment extends Fragment {

    // TextView txtName, txtLastname;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");
    private String TAG = "Profile Fragment";
    private UserModel user;
    private TextView textName;
    private TextView textLastName;
    private TextView textLocation;
    private TextView txtStatus;
    private TextView textEmail;
    private TextView textMobileNumb;
    private TextView textAddress;
    private Spinner spinner;
    private ImageButton btnMail;
    private ImageButton btnPhone;
    private ImageButton btnLocation;
    private ImageView profilePhoto;
    private StorageReference mStorageRef;
    private StorageReference pathRef;
    private Button btnLogout;
    private ProgressBar pb;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile, container, false);
        pb = v.findViewById(R.id.pbProfile);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        textName = v.findViewById(R.id.txtUserName);
        textLastName = v.findViewById(R.id.txtLastName);
        profilePhoto = v.findViewById(R.id.profilePhoto);
        btnLogout = v.findViewById(R.id.btnLogOut);
        btnMail=v.findViewById(R.id.btnEmail);
        btnPhone=v.findViewById(R.id.btnPhone);
        btnLocation=v.findViewById(R.id.btnLocation);
        auth = FirebaseAuth.getInstance();
        txtStatus=v.findViewById(R.id.txtStatusProfile);
        Read(auth.getCurrentUser().getUid());
        pb.setVisibility(View.VISIBLE);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getContext(), SplashActivity.class));
            }
        });

        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        return v;
    }

    void Read(final String url) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user= dataSnapshot.child(url).getValue(UserModel.class);
//                String name = (String) dataSnapshot.child(url).child("username").getValue();
//                String lastname = (String) dataSnapshot.child(url).child("lastname").getValue();
//                String pass = (String) dataSnapshot.child(url).child("pass").getValue();
//                String email = (String) dataSnapshot.child(url).child("email").getValue();
//                String CreationDate = (String) dataSnapshot.child(url).child("CreationDate").getValue();
//                String PhotoUrl = (String) dataSnapshot.child(url).child("imageUrl").getValue();
                //Toast.makeText("TAGG",PhotoUrl,Toast.LENGTH_LONG).show();
                Log.i("Tag(pp)", user.getImageUrl());


                pathRef = mStorageRef.child("images/" + user.getImageUrl());
                mStorageRef.child("images/" + user.getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("Tag(pp)", uri.toString());
                        int SDK_INT = android.os.Build.VERSION.SDK_INT;
                        if (SDK_INT > 8) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            try {
                                URL url = new URL(uri.toString());
                                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                System.out.println("byte count:" + image.getByteCount());
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), image);
                                circularBitmapDrawable.setCircular(true);
                                profilePhoto.setImageDrawable(circularBitmapDrawable);
                                pb.setVisibility(View.INVISIBLE);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
                textName.setText(user.getUsername());
                textLastName.setText(user.getLastname());
                txtStatus.setText(user.getStatus());


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


}

