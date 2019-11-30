package com.example.place.firebaseapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.place.firebaseapp.R;
import com.example.place.firebaseapp.model.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Student> students = new ArrayList<>();

    private RecyclerView studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        studentList = findViewById(R.id.studentList);

        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.addStudent).setOnClickListener(this);
        FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Student> list = new ArrayList<Student>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    list.add(child.getValue(Student.class));
                }
                studentList.setAdapter(new StudentAdapter(list, HomeActivity.this));
                studentList.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.addStudent:
                Intent intent1 = new Intent(HomeActivity.this, EditDetailActivity.class);
                startActivity(intent1);
                break;

        }
    }
}
