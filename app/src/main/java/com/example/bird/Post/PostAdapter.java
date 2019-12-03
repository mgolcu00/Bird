package com.example.bird.Post;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bird.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Holder> {

    ArrayList<PostData> postList;
    LayoutInflater inflater;
    Context context;

    public PostAdapter(Context ctx, ArrayList<PostData> postD) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(context);
        this.postList = postD;
    }

    @NonNull
    @Override
    public PostAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_post, parent, false);
        Holder mHolder = new Holder(v);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.Holder holder, int position) {
        PostData selectedPost = postList.get(position);
        holder.setData(selectedPost,position);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView;
        TextView TextContent;
        TextView StatusText;

        public Holder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.NameUserPost);
            TextContent = itemView.findViewById(R.id.contentText);
            StatusText = itemView.findViewById(R.id.userStatus);

        }

        public void setData(PostData post, int position) {
            this.nameTextView.setText(post.getUser().getName());

            this.TextContent.setText(post.getPosttext());
            this.StatusText.setText("statu");

        }

        @Override
        public void onClick(View v) {

        }
    }
}


