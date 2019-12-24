package com.example.bird.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.bird.Activities.ChatActivity;
import com.example.bird.Models.RoomModel;
import com.example.bird.R;

import java.util.ArrayList;

public class RoomAdapter extends Adapter<RoomAdapter.MyViewHolder> {
    ArrayList<RoomModel> mRoomModels;
    LayoutInflater mInflater;
    Context context;

    public RoomAdapter(Context ctx, ArrayList<RoomModel> roomModels) {
        this.mInflater = LayoutInflater.from(ctx);
        this.mRoomModels = roomModels;
        this.context = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_room, parent, false);
        MyViewHolder holder = new MyViewHolder(view, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RoomModel selectedRoomModel = mRoomModels.get(position);
        holder.setData(selectedRoomModel, position);
    }


    @Override
    public int getItemCount() {

        return mRoomModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Title;
        TextView UserCount;
        Context context;
        RoomModel sr;

        public MyViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            this.context = ctx;
            Title = itemView.findViewById(R.id.RoomTitle);
            UserCount = itemView.findViewById(R.id.RoomUserCount);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), ChatActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("key", sr.getRoomName());
                    context.getApplicationContext().startActivity(intent);
                }
            });
        }

        public void setData(RoomModel selectedRoomModel, int position) {
            this.Title.setText(selectedRoomModel.getRoomName());
            this.UserCount.setText(selectedRoomModel.getRoomCount() + " Kişi aktif");
            this.sr = selectedRoomModel;

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("key", sr.getRoomName());
            context.startActivity(intent);

        }

    }
}
