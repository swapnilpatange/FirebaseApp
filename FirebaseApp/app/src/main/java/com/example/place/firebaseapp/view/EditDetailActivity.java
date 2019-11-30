package com.example.place.firebaseapp.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.place.firebaseapp.R;
import com.example.place.firebaseapp.model.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class EditDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView studentName, studentRollno;
    private final int PICK_IMAGE_REQUEST = 71;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    private ImageView displayImage;

    private Uri filePath;

    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            student = (Student) bundle.getSerializable("studentData");
        }
        studentName = findViewById(R.id.studentName);
        studentRollno = findViewById(R.id.studentRollno);
        displayImage = findViewById(R.id.displayImage);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        studentName.setOnClickListener(this);
        studentRollno.setOnClickListener(this);
        displayImage.setOnClickListener(this);
        findViewById(R.id.addProduct).setOnClickListener(this);
        if (student != null) {
            studentName.setText(student.getName());
            studentRollno.setText(student.getRollno());
            Glide.with(this).load(student.getImageUrl()).into(displayImage);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addProduct:

                if (studentName.getText().toString().trim().length() > 0 &&
                        studentRollno.getText().toString().trim().length() > 0) {
                    uploadImage();

                }
                break;
            case R.id.displayImage:
                chooseImage();
                break;

        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                displayImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            if (student == null) {
                                student = new Student();
                                student.setName(studentName.getText().toString());
                                student.setRollno(studentRollno.getText().toString());
                                student.setImageUrl(taskSnapshot.getDownloadUrl().toString());
                                String key = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).push().getKey();
                                student.setKey(key);
                                FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child(key).setValue(student);

                                Toast.makeText(EditDetailActivity.this, "student added successfully", Toast.LENGTH_LONG).show();
                            } else {
                                student.setRollno(studentRollno.getText().toString());
                                student.setName(studentName.getText().toString());
                                student.setImageUrl(taskSnapshot.getDownloadUrl().toString());
                                FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child(student.getKey()).setValue(student);

                                Toast.makeText(EditDetailActivity.this, "student updated successfully", Toast.LENGTH_LONG).show();
                            }
                            finish();
                            //Toast.makeText(EditDetailActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditDetailActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        } else if (student != null) {
            student.setRollno(studentRollno.getText().toString());
            student.setName(studentName.getText().toString());
            FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child(student.getKey()).setValue(student);
            finish();
            Toast.makeText(EditDetailActivity.this, "student updated successfully", Toast.LENGTH_LONG).show();
        }
    }
}
