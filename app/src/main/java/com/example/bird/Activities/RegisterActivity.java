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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bird.Models.UserModel;
import com.example.bird.R;
import com.example.bird.Utils.GlideUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.example.bird.R.layout.activty_register;


public class RegisterActivity extends AppCompatActivity {

    private static final int PIC_CROP = 1;
    private Button btnSend;
    private EditText edtEmail;
    private EditText edtPass;
    private StorageReference mStorageRef;
    EditText edtPass2;
    EditText edtName;
    EditText edtLastname;
    TextView txtGoLogin;
    Intent intent;
    DatabaseReference myRef;
    FirebaseDatabase database;
    Uri filePath, filePath2;
    Date date;
    ProgressBar pb;
    Snackbar snackbar;
    ImageView profileImage;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private FirebaseAuth mAuth;
    private String TAG = "TAG";
    String ImageUrl = "";
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activty_register);

        btnSend = findViewById(R.id.btnSend);
        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtPass = findViewById(R.id.edtPassReg);
        edtPass2 = findViewById(R.id.edtPassReg2);
        txtGoLogin = findViewById(R.id.txtConnectLogin);
        edtLastname = findViewById(R.id.edtLastname);
        pb = findViewById(R.id.progressBarReg);

        profileImage = findViewById(R.id.imgReg);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        myRef = database.getReference("users");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(edtEmail.getText());
                String pass = String.valueOf(edtPass.getText());
                String pass2 = String.valueOf(edtPass2.getText());
                String name = String.valueOf(edtName.getText());
                String lastname = String.valueOf(edtLastname.getText());
                date = new Date();
                String DateValue = dateFormat.format(date);

                if (name.equals("")) {
                    snackbar.make(v, "isim boş olamaz ", Snackbar.LENGTH_LONG).show();
                } else if (lastname == "") {
                    snackbar.make(v, "Soyisim boş olamaz ", Snackbar.LENGTH_LONG).show();
                } else if (email.equals("")) {
                    snackbar.make(v, "email boş olamaz ", Snackbar.LENGTH_LONG).show();
                } else if (!pass.equals(pass2)) {
                    snackbar.make(v, "Passwords doesnt mach", Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            edtPass.setText(null);
                            edtPass2.setText(null);
                        }
                    });
                    snackbar.show();
                } else {
                    UserModel user = new UserModel(name, email, pass, lastname, DateValue,);

                    uploadImage(mStorageRef);
                    if (ImageUrl.equals("")) {
                        user.setImageUrl("Bird_Logo_png.png");
                    } else {
                        user.setImageUrl(ImageUrl);
                    }

                    Reg(user, v);

                }
            }
        });
        txtGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("IMAGEBUTTON","TESTTTT");
                chooseImage();

            }
        });

    }


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

                //Drawable d = new BitmapDrawable(getResources(),bitmap);
                //getOptimizeImage GOI = new getOptimizeImage(profileImage, String.valueOf(filePath));
                //GlideUtil.glide_use2(getApplicationContext(), String.valueOf(filePath), profileImage, 2048, 4096);
                GlideUtil.glide_use_Circle(getApplicationContext(), String.valueOf(filePath), profileImage);
                //GlideUtil.glideTransform(getApplicationContext(), 2, profileImage);
                //profileImage.setImageBitmap(bitmap);
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


    void Reg(final UserModel user, final View v) {
        pb.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPass())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser userF = mAuth.getCurrentUser();
                            //database
                            myRef.child(Objects.requireNonNull(userF.getUid())).setValue(user);
                            snackbar.make(v, "Register success", Snackbar.LENGTH_LONG).show();
                            pb.setVisibility(View.INVISIBLE);
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            //updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            snackbar.make(v, "Register failure", Snackbar.LENGTH_LONG).show();
                            pb.setVisibility(View.INVISIBLE);
                            //updateUI(null);
                        }


                    }
                });
    }

}

