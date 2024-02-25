package com.example.myapplication;

import static com.example.myapplication.Select_class.email;
import static com.example.myapplication.Select_class.userclass;
import static com.example.myapplication.class_showing.currentuserforchecking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

public class users_books extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    Button new_book_btn;
    public static String currentsubject;

    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_books);
        storageRef.child(userclass).child(currentuserforchecking).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
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
                            Intent intent= new Intent(getApplicationContext(),Signature_for_checking.class);
                            startActivity(intent);
                            currentsubject=prefix.getName();
                        }
                    });
                }
            }
        });
    }
}