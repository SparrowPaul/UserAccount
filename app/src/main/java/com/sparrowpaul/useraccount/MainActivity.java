package com.sparrowpaul.useraccount;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    /*
    This is the signup screen of the app
    this app has no login screen

    fields here are working (meaning they send information to the database)
     */

    private EditText userName, inputemail, inputpassword, phoneNumber;
    private Button doneBtn;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, ResultScreen.class));
            finish();
        }

        setContentView(R.layout.activity_main);

        initViews();

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = userName.getText().toString();
                final String email = inputemail.getText().toString();
                String password = inputpassword.getText().toString();
                final String number = phoneNumber.getText().toString();

                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                mDatabase = FirebaseDatabase.getInstance().getReference();




                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(MainActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                //progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                    String userId = mDatabase.push().getKey();
                                    Profile profile = new Profile(name,email,number);
                                    mDatabase.child(userId).setValue(profile);

                                    startActivity(new Intent(MainActivity.this, ResultScreen.class));
                                    finish();
                                }
                            }
                        });
            }
        });

    }

    private void initViews() {
        userName = findViewById(R.id.editText);
        inputpassword = findViewById(R.id.editText3);
        inputemail = findViewById(R.id.editText2);
        phoneNumber = findViewById(R.id.editText4);

        doneBtn = findViewById(R.id.button2);
    }
}
