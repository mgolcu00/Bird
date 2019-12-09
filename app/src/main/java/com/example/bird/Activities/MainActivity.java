package com.example.bird.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bird.Fragments.ChatRoomFragment;
import com.example.bird.Fragments.PostsFragment;
import com.example.bird.Fragments.ProfileFragment;
import com.example.bird.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressBar pb;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb=findViewById(R.id.pbWait);
        pb.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                database=FirebaseDatabase.getInstance();
                databaseReference=database.getReference();
                storage=FirebaseStorage.getInstance();
                storageReference=storage.getReference();
                pb.setVisibility(View.VISIBLE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).start();

        loadFragment(new ChatRoomFragment());
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        selectedFragment = new ProfileFragment();
                        //Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.chat_rooms:
                        selectedFragment = new ChatRoomFragment();
                        //Toast.makeText(MainActivity.this, "chat_selector rooms", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.posts:
                        selectedFragment = new PostsFragment();
                        //Toast.makeText(MainActivity.this, "Posts", Toast.LENGTH_SHORT).show();
                        break;
                }

                return loadFragment(selectedFragment);
            }
        });


    }

    private boolean loadFragment(Fragment fragment) {
        pb.setVisibility(View.INVISIBLE);
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}

