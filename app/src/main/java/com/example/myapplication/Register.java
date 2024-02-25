package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Register extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword, editTextclass;
    Button buttonReg;
    ProgressBar progressBar;
    TextView logNow;
    private FirebaseAuth mAuth;
    public String usersclass;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextclass=findViewById(R.id.students_class);
        logNow=findViewById(R.id.loginnow);
        editTextEmail=findViewById(R.id.email);
        editTextPassword=findViewById(R.id.password);
        buttonReg=findViewById(R.id.btn_reg);
        progressBar=findViewById(R.id.progress_bar);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String[] email = new String[1];
                String password;
                email[0] =String.valueOf(editTextEmail.getText());
                password=String.valueOf(editTextPassword.getText());
                usersclass=String.valueOf(editTextclass.getText());
                if(TextUtils.isEmpty(email[0])){
                    Toast.makeText(Register.this, "Enter your email", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Enter your password", Toast.LENGTH_SHORT).show();
                }
                mAuth.createUserWithEmailAndPassword(email[0], password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    byte[] bmdata="Techie Delight".getBytes();;
                                    UploadTask uploadTask = storageRef.child("users_classes").child(email[0]).child(usersclass+".txt").putBytes(bmdata);
                                    Toast.makeText(Register.this, "Registration complete", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(Register.this, "Registration failed",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });



            }
        });
        logNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}