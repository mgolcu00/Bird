package com.example.bird.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bird.Models.UserModel;
import com.example.bird.Models.PostModel;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CreatePostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST =71 ;
    private Button btnExit;
    private Button btnSend;
    private Button btnImageAdd;
    private EditText edtDescription;
    private EditText edtLocation;
    private EditText edtDate;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference("users");
    private DatabaseReference mRef2 = mDatabase.getReference("posts");
    String date;
    private String Url;
    private String ImageUrl;
    private UserModel user;
    private Uri filePath;
    private ImageView PostImageView;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    private PostModel post;
    //UserModel temp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_createpost);

        btnSend = findViewById(R.id.btnSendCreatePost);
        btnExit = findViewById(R.id.btnexitCreatePost);
        btnImageAdd=findViewById(R.id.AddImage);
        edtDescription=findViewById(R.id.edtDescirption);
        edtLocation=findViewById(R.id.edtLocationCreatePost);
        edtDate=findViewById(R.id.edtDateCreatePost);
        PostImageView=findViewById(R.id.imgPostimage);
        storage=FirebaseStorage.getInstance();
        mStorageRef= storage.getReference();
        Url = mAuth.getCurrentUser().getUid();


        date = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date());


        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                               // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user = dataSnapshot.child(Url).getValue(UserModel.class);

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

                String uuid = UUID.randomUUID().toString();
                post= new PostModel(user.getId(),uuid,date,edtDate.getText().toString(),edtLocation.getText().toString(),edtDescription.getText().toString(),"");
                uploadImage(mStorageRef);
                post.setPostImageUrl(ImageUrl);
                mRef2.child(post.getPostId()).setValue(post);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        btnImageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

    }

    //IMAGE
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Fotoğraf Seç"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //GlideUtil
                //Drawable d = new BitmapDrawable(getResources(),bitmap);
                //getOptimizeImage GOI = new getOptimizeImage(profileImage, String.valueOf(filePath));
                //GlideUtil.glide_use2(getApplicationContext(), String.valueOf(filePath), profileImage, 2048, 4096);
                //GlideUtil.glide_use_Circle(getApplicationContext(), String.valueOf(filePath), PostImageView);
                //GlideUtil.glideTransform(getApplicationContext(), 2, profileImage);
                PostImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //Upload
    private void uploadImage(StorageReference storageReference) {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            ImageUrl = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/" + ImageUrl);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
}
