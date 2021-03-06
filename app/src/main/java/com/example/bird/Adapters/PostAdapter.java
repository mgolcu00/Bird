package com.example.bird.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bird.Models.PostModel;
import com.example.bird.Models.UserModel;
import com.example.bird.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Holder> {

    ArrayList<PostModel> postList;
    LayoutInflater inflater;
    Context context;

    public PostAdapter(Context ctx, ArrayList<PostModel> postD) {
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
        PostModel selectedPost = postList.get(position);
        holder.setData(selectedPost, position);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        StorageReference mStorageRef;
        StorageReference pathRef;
        FirebaseDatabase database;
        DatabaseReference mRef;
        TextView nameTextView;
        TextView lastNameTextView;
        TextView TextContent;
        TextView StatusText;
        ImageView profilePic;
        String PhotoUrl, PostImageUrl;
        ProgressBar pb;
        ImageView PostImage;
        UserModel user;

        public Holder(@NonNull View itemView) {
            super(itemView);
            pb = itemView.findViewById(R.id.pbItem);
            pb.setVisibility(View.VISIBLE);
            mStorageRef = FirebaseStorage.getInstance().getReference();
            nameTextView = itemView.findViewById(R.id.txtName2);
            lastNameTextView = itemView.findViewById(R.id.txtLastName2);
            TextContent = itemView.findViewById(R.id.txtTextArea);
            StatusText = itemView.findViewById(R.id.txtStatus2);
            profilePic = itemView.findViewById(R.id.imgProfilePic);
            PostImage = itemView.findViewById(R.id.PostImageAdded);
            database = FirebaseDatabase.getInstance();
            mRef = database.getReference("users");
        }

        public void setData(final PostModel post, int position) {

            Runnable r = () -> {

                mRef = database.getReference("users/" + post.getPostId());
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        user = dataSnapshot.getValue(UserModel.class);
                        try {

                            Thread.sleep(25000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                   /*
                   *  try {
                        user2 = dataSnapshot.child(url).<UserModel>getValue(UserModel.class);
                    } catch (Exception e) {
                        Log.i("Tag(pp)", e.getMessage());
                    }
                   * */


                        Log.i("Tag(pp)", user.getImageUrl());

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("TAG", "Failed to read value.", error.toException());
                    }
                });

            };

            Thread t = new Thread(r);

            t.start();
            if (user != null) {
                this.PhotoUrl = user.getImageUrl();
                this.PostImageUrl = post.getPostImageUrl();
                readStorage(PhotoUrl, profilePic);
                if (!PostImageUrl.equals("")) {
                    readStorage(PostImageUrl, PostImage);
                }
                this.nameTextView.setText(user.getUsername());
                this.lastNameTextView.setText(user.getLastname());
                this.TextContent.setText(post.getPosttext());
                this.StatusText.setText(user.getStatus());

            } else {
                user = Read(post.getUserId());
            }
        }

        public void readStorage(String PhotoUrl, final ImageView i) {
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
                            i.setImageBitmap(image);
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
                    Log.i("Storage", "HATA");
                }
            });
        }

        @Override
        public void onClick(View v) {

        }

        UserModel user2 = null;

        UserModel Read(final String url) {


            return user2;
        }
    }


}


