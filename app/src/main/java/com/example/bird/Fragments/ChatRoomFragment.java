package com.example.bird.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bird.Chat.Room;
import com.example.bird.Chat.RoomAdapter;
import com.example.bird.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatRoomFragment extends Fragment {
    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Room> Rooms = new ArrayList<Room>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = database.getReference("roomnames");
    private ProgressBar pb;
    Room r;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_chatrooms, container, false);
        pb = v.findViewById(R.id.pbRooms);
        pb.setVisibility(View.VISIBLE);
        recyclerView = v.findViewById(R.id.Rooms);
        r = new Room("100", 100, "TEMP");
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //database
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot ds : dataSnapshot.getChildren()
                ) {
                    Room room = new Room();
                    room.setRoomName(ds.child("RoomName").getValue(String.class));
                    room.setUserCount(ds.child("UserCount").getValue(int.class));
                    room.setRoomId(ds.child("id").getValue(String.class));
                    Rooms.add(room);
                }
                roomAdapter = new RoomAdapter(getContext(), Rooms);
                recyclerView.setAdapter(roomAdapter);
                pb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });


        return v;
    }

}
