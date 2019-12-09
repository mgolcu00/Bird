package com.example.bird.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bird.Activities.CreatePostActivity;
import com.example.bird.Login.UserC;
import com.example.bird.Post.PostAdapter;
import com.example.bird.Post.PostData;
import com.example.bird.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PostsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference("posts");
    private ArrayList<PostData> posts = new ArrayList<PostData>();
    private FloatingActionButton fab;
    public ProgressBar pbPOST;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_posts, container, false);
        pbPOST=v.findViewById(R.id.pbPosts);
        pbPOST.setVisibility(View.VISIBLE);
        recyclerView = v.findViewById(R.id.ViewRec);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        fab = v.findViewById(R.id.PostAddFab);
        fab.setColorFilter(R.color.colorFire);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreatePostActivity.class));
            }
        });
        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()
                ) {
                    PostData postD = new PostData();
                    postD.setUser(ds.child("user").getValue(UserC.class));
                    postD.setPosttext(ds.child("posttext").getValue(String.class));
                    postD.setCreatingDate(ds.child("creatingDate").getValue(String.class));
                    posts.add(postD);
                }
                Collections.sort(posts, PostData.pstDate);
                PostAdapter adapter = new PostAdapter(getContext(), posts);
                recyclerView.setAdapter(adapter);
                pbPOST.setVisibility(View.INVISIBLE);
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });
        //
        return v;
    }
}
