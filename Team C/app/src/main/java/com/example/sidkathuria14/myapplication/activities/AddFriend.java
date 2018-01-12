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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import static com.example.sidkathuria14.myapplication.helpers.Constants.TAG;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AddFriend extends AppCompatActivity implements ZXingScannerView.ResultHandler{
EditText etInput;DatabaseReference mDatabase;
   String email,userId;
    public List<User> friendList ;
    private ZXingScannerView mScannerView;
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
