package com.example.bird.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bird.Models.MessageModel;
import com.example.bird.R;
import com.example.bird.Utils.GlideUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.URL;

public class ChatActivity extends AppCompatActivity {
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
        TextView messengerTextView;
        ImageView messengerImageView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (ImageView) itemView.findViewById(R.id.messagePp);
        }
    }

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<MessageModel, MessageViewHolder>
            mFirebaseAdapter;
    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    private ImageView mAddMessageImageView;
    private String mUsername;
    private String mPhotoUrl;
    private FirebaseDatabase database;
    private DatabaseReference UserRef;
    private DatabaseReference MesseageRef;
    private String MESSAGES_CHILD;
    private StorageReference mStorageRef;
    private StorageReference pathRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle b = getIntent().getExtras();
        String ROOM_NAME = String.valueOf(b.getString("key"));
        MESSAGES_CHILD = "rooms/" + ROOM_NAME + "/messages";
        //Initaialize Firebase Database
        database = FirebaseDatabase.getInstance();
        UserRef = database.getReference("users");
        MesseageRef = database.getReference(MESSAGES_CHILD);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
            return;
        } else {
            //mUsername = mFirebaseUser.getDisplayName();
            UserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String name = (String) dataSnapshot.child(mFirebaseAuth.getCurrentUser().getUid()).child("name").getValue();
                    String lastname = (String) dataSnapshot.child(mFirebaseAuth.getCurrentUser().getUid()).child("lastname").getValue();
                    Log.d("TAG", "\nName     : " + name +
                            "\nLastname : " + lastname);
                    mUsername = name;
                    String PhotoUrl = (String) dataSnapshot.child(mFirebaseAuth.getCurrentUser().getUid()).child("imageUrl").getValue();
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
                                    mPhotoUrl = String.valueOf(url);
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

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("TAG", "Failed to read value.", error.toException());
                }
            });
            Log.i("TEST", "Username: " + mUsername);
        }

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<MessageModel> parser = new SnapshotParser<MessageModel>() {
            @Override
            public MessageModel parseSnapshot(DataSnapshot dataSnapshot) {
                MessageModel friendlyMessage = dataSnapshot.getValue(MessageModel.class);
                if (friendlyMessage != null) {
                    friendlyMessage.setId(dataSnapshot.getKey());
                }
                return friendlyMessage;
            }
        };

        DatabaseReference messagesRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD);
        FirebaseRecyclerOptions<MessageModel> options =
                new FirebaseRecyclerOptions.Builder<MessageModel>()
                        .setQuery(messagesRef, parser)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<MessageModel, MessageViewHolder>(options) {
            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new MessageViewHolder(inflater.inflate(R.layout.item_messeage, viewGroup, false));


            }

            @Override
            protected void onBindViewHolder(final MessageViewHolder viewHolder,
                                            int position,
                                            MessageModel friendlyMessage) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                if (friendlyMessage.getText() != null) {
                    viewHolder.messageTextView.setText(friendlyMessage.getText());
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                    viewHolder.messageImageView.setVisibility(ImageView.GONE);
                }
                GlideUtil.glide_use_Circle(getApplicationContext(), mPhotoUrl, viewHolder.messengerImageView);
                viewHolder.messengerTextView.setText(friendlyMessage.getName());
            }
        };
        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mMessageEditText.setScroller(new Scroller(getApplicationContext()));
        mMessageEditText.setMaxLines(2);
        mMessageEditText.setVerticalScrollBarEnabled(true);
        mMessageEditText.setMovementMethod(new ScrollingMovementMethod());
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mSendButton = (Button) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                MessageModel messageModel = new
                        MessageModel(mMessageEditText.getText().toString(),
                        mUsername);
                messageModel.setPpUrl(mPhotoUrl);
                Log.i("TEST", "Name: " + messageModel.getName() + " | Text: " + messageModel.getText());
                mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                        .push().setValue(messageModel);
                mMessageEditText.setText("");
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in.
        // TODO: Add code to check if user is signed in.
    }

    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void ReadName(final String url) {

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String name = (String) dataSnapshot.child(url).child("name").getValue();
                String lastname = (String) dataSnapshot.child(url).child("lastname").getValue();
                Log.d("TAG", "\nName     : " + name +
                        "\nLastname : " + lastname);
                mPhotoUrl=(String) dataSnapshot.child(url).child("ppUrl").getValue();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }

}
