package com.example.place.firebaseapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.place.firebaseapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this, "user already logged", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HomeActivity.class);
            finish();
            startActivity(intent);
        } else
            updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        findViewById(R.id.signup).setVisibility(View.VISIBLE);
        findViewById(R.id.signin).setVisibility(View.VISIBLE);
        findViewById(R.id.signin).setOnClickListener(this);
        findViewById(R.id.signup).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin:
                Intent signUpintent = new Intent(MainActivity.this, SignUpActivity.class);
                signUpintent.putExtra("isSignup", "0");
                startActivity(signUpintent);
                finish();
                break;
            case R.id.signup:
                Intent signInintent = new Intent(MainActivity.this, SignUpActivity.class);
                signInintent.putExtra("isSignup", "1");
                startActivity(signInintent);
                finish();
                break;
        }
    }
}
