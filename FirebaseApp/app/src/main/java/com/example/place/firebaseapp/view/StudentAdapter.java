package com.example.place.firebaseapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.place.firebaseapp.R;
import com.example.place.firebaseapp.model.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private final Activity activity;
    private List<Student> students;

    public StudentAdapter(List<Student> students, Activity activity) {
        this.students = students;
        this.activity = activity;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_item, null);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder viewHolder, final int position) {
        viewHolder.name.setText(students.get(position).getName());
        viewHolder.rollno.setText(students.get(position).getRollno());
        if (students.get(position).getImageUrl() != null && students.get(position).getImageUrl().length() > 0)
            Glide.with(activity).load(students.get(position).getImageUrl()).into(viewHolder.displayImage);


        viewHolder.deleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (students.get(position).getKey() != null && students.get(position).getKey().trim().length() > 0)
                    FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child(students.get(position).getKey()).removeValue();
            }
        });
        viewHolder.editStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EditDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("studentData",students.get(position));
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {

        public TextView name, rollno;
        public ImageView displayImage;

        public ImageView editStudent, deleteStudent;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            rollno = itemView.findViewById(R.id.rollno);
            displayImage = itemView.findViewById(R.id.displayImage);
            deleteStudent = itemView.findViewById(R.id.deleteStudent);
            editStudent = itemView.findViewById(R.id.editStudent);
        }
    }
}
