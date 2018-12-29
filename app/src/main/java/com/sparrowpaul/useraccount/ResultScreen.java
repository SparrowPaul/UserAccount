package com.sparrowpaul.useraccount;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResultScreen extends AppCompatActivity {


    /*
    this activity is to receive data from the real time database
     */
    private TextView profileName, profileNumber, profileEmail;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);
        intViews();



        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference();
        FirebaseUser user =   auth.getCurrentUser();
        userID = user.getUid();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                    Toast.makeText(ResultScreen.this, "Successfully signed in with: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                } else {
                    // User is signed out
                    Toast.makeText(ResultScreen.this, "Successfully signed out " + user.getEmail(), Toast.LENGTH_SHORT).show();

                }
                // ...
            }
        };

        mDatabase.child(userID).addValueEventListener(new ValueEventListener() {  //function to retrieve data from the database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds: dataSnapshot.getChildren()) {
            Profile profile = new Profile();
            profile.setPersonName(ds.child(userID).getValue(Profile.class).getPersonName()); //this lines are returning null and i don't know why
            profile.setEmail(ds.child(userID).getValue(Profile.class).getEmail());
            profile.setTelNumber(ds.child(userID).getValue(Profile.class).getTelNumber());

            profileEmail.setText(profile.getEmail());
            profileNumber.setText(profile.getTelNumber());
            profileName.setText(profile.getPersonName());

            System.out.println("Name is "+profile.getPersonName());
        }
        

        

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    private void intViews() {  // finding the views
        profileName = findViewById(R.id.resultUserName);
        profileEmail = findViewById(R.id.resultEmail);
        profileNumber = findViewById(R.id.resultNumber);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
