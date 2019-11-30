package com.example.place.firebaseapp.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.place.firebaseapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getStringExtra("isSignup") != null && getIntent().getStringExtra("isSignup").equalsIgnoreCase("1")) {
                    mAuth.createUserWithEmailAndPassword(((TextView) findViewById(R.id.email)).getText().toString(), ((TextView) findViewById(R.id.password)).getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(getClass().getSimpleName(), "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(getClass().getSimpleName(), "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                } else {
                    mAuth.signInWithEmailAndPassword(((TextView) findViewById(R.id.email)).getText().toString(), ((TextView) findViewById(R.id.password)).getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(getClass().getSimpleName(), "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(getClass().getSimpleName(), "signInWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
            }
        });


    }

}
