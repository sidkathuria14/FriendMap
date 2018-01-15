package com.example.sidkathuria14.myapplication.activities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sidkathuria14.myapplication.R;
import com.example.sidkathuria14.myapplication.models.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import static com.example.sidkathuria14.myapplication.helpers.Constants.TAG;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AddFriend extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    EditText etInput;
    DatabaseReference mDatabase;
    String email, userId;
    public List<User> friendList;
    private ZXingScannerView mScannerView;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_friend);
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase = FirebaseDatabase.getInstance().getReference("allusers");
//        etInput = (EditText)findViewById(R.id.etUserId);
//        userId = mDatabase.push().getKey();
//final User user = new User("sid","blahblah@yahoo.com");
//
//        mDatabase.child(userId).setValue(user);
//        Log.d(TAG, "onCreate: " + mDatabase.getKey());
//        ((Button)findViewById(R.id.btnAdd)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void handleResult(Result result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(result.getText());
        uid = result.getText();
        Log.d(TAG, "handleResult: addfriend zxing " + uid);
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser().getUid()
        FirebaseDatabase.getInstance().getReference(uid).orderByChild("name")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
User user = dataSnapshot.getValue(User.class);
                        Log.d(TAG, "onChildAdded: final vala" + user.name);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "onChildChanged: ");
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onChildRemoved: ");
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "onChildMoved: ");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError);
                    }
                });


//        Log.d(TAG, "handleResult: " + checkUID);
//        FirebaseUser user = FirebaseAuth.getInstance().FirebaseDatabase.getInstance().getReference(uid).getDatabase();
//                orderByKey().equalTo(uid).getRef();
//final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
//        ref.orderByChild(uid + "/name").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        FirebaseUser user = FirebaseAuth.getInstance().signInWithCredential()

        AlertDialog alert1 = builder.create();
        alert1.show();

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }
}
