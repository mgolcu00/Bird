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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bird.Activities.SplashActivity;
import com.example.bird.Login.UserC;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class ProfileFragment extends Fragment {

    // TextView txtName, txtLastname;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");
    private String TAG = "Profile Fragment";
    private UserC user;
    private TextView textName;
    private TextView textLastName;
    private TextView textLocation;
    private TextView textStatu;
    private TextView textEmail;
    private TextView textMobileNumb;
    private TextView textAddress;
    private Spinner spinner;
    private ImageView profilePhoto;
    private StorageReference mStorageRef;
    private StorageReference pathRef;
    private Button btnLogout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile, container, false);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        textName = v.findViewById(R.id.txtUserName);
        textLastName = v.findViewById(R.id.txtLastName);
        profilePhoto = v.findViewById(R.id.profilePhoto);
        btnLogout = v.findViewById(R.id.btnLogout);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getContext(), SplashActivity.class));
            }
        });

//        textLocation = v.findViewById(R.id.textLocation);
//        textStatu = v.findViewById(R.id.textStatu);
//        textEmail = v.findViewById(R.id.textEmail);
//        textMobileNumb = v.findViewById(R.id.textMobileNumb);
//        textAddress = v.findViewById(R.id.textAddress);
//        spinner = v.findViewById(R.id.spinner);
//        initSpinner(v); //spinner'ı aktive eden metod
//        spinner.setOnItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();
        Read(auth.getCurrentUser().getUid());


        return v;
    }

    void Read(final String url) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String name = (String) dataSnapshot.child(url).child("name").getValue();
                String lastname = (String) dataSnapshot.child(url).child("lastname").getValue();
                String pass = (String) dataSnapshot.child(url).child("pass").getValue();
                String email = (String) dataSnapshot.child(url).child("email").getValue();
                String CreationDate = (String) dataSnapshot.child(url).child("CreationDate").getValue();
                String PhotoUrl = (String) dataSnapshot.child(url).child("imageUrl").getValue();
                //Toast.makeText("TAGG",PhotoUrl,Toast.LENGTH_LONG).show();
                Log.i("Tag(pp)", PhotoUrl);
                pathRef = mStorageRef.child("images/" + PhotoUrl);
                mStorageRef.child("images/" + PhotoUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                                profilePhoto.setImageBitmap(image);

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
                textName.setText(name);
                textLastName.setText(lastname);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

//    public static Bitmap getBitmapFromURL(String src) {
//        int SDK_INT = android.os.Build.VERSION.SDK_INT;
//        if (SDK_INT > 8) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                    .permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            try {
//                URL url = new URL(src);
//                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                System.out.println("byte count:" + image.getByteCount());
//                return image;
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//    }

//    private void initSpinner(View v) {
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
//                R.array.profiledropdown, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//    }


//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String selectedItem = parent.getItemAtPosition(position).toString();
//        System.out.println(selectedItem);
//        switch (selectedItem) {
//            case "Sign Out":
//                auth.signOut();
//                startActivity(new Intent(getContext(), SplashActivity.class));
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
}
