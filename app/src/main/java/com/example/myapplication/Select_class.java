package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class Select_class extends AppCompatActivity {
    TextInputEditText class_name;
    Button btn_create;
    ProgressBar progressBar;
    TextView return_to_main;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    public static String email, userclass, currentemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_class);
        class_name=findViewById(R.id.class_name);
        btn_create=findViewById(R.id.btn_enter);
        progressBar=findViewById(R.id.progress_bar);
        return_to_main=findViewById(R.id.return_to_main);
        email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        return_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userclass=String.valueOf(class_name.getText());
                ProgressDialog progressDialog=new ProgressDialog(Select_class.this);
                progressDialog.setTitle("loading img");
                storageRef.child("teachers").child(userclass).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()){
                            System.out.println(prefix.getName());
                            System.out.println(email);
                            currentemail=prefix.getName();
                            if(email.equals(currentemail)){
                                userclass=String.valueOf(class_name.getText());
                                Intent intent2= new Intent(getApplicationContext(),class_showing.class);
                                startActivity(intent2);
                                finish();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }
}