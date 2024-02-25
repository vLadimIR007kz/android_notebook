package com.example.myapplication;

import static com.example.myapplication.Select_class.userclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

public class class_showing extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    Button new_book_btn;
    public static String currentuserforchecking;

    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_showing);
        storageRef.child(userclass).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference prefix : listResult.getPrefixes()){
                    int resourseId= getResources().getIdentifier("button" +i, "id", getPackageName());
                    i++;
                    new_book_btn = findViewById(resourseId);
                    new_book_btn.setVisibility(View.VISIBLE);
                    new_book_btn.setText(prefix.getName());
                    new_book_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent= new Intent(getApplicationContext(),users_books.class);
                            startActivity(intent);
                            currentuserforchecking=prefix.getName();
                        }
                    });
                }
            }
        });
    }
}