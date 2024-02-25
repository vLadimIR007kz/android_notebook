package com.example.myapplication;


import static com.example.myapplication.create_new_book.integer;
import static com.example.myapplication.create_new_book.names;

import com.example.myapplication.create_new_book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.Console;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button new_book_btn;
    public int i;
    public static String currentsubj,email;
    String[] directories1;
    public static String usersclass;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        //System.out.println(Login.email);
        usersclass="teacher";
        ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("loading img");
        progressDialog.show();
        storageRef.child("users_classes").child(email).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    // All the items under listRef.
                    System.out.print(12);
                    usersclass=item.getName();
                    System.out.println(item);
                    usersclass=usersclass.substring(0,usersclass.length()-4);
                    if(usersclass.length()==7 && usersclass.substring(3,3)==","){
                        usersclass=usersclass.substring(0,3);
                        System.out.println(usersclass);
                    }
                    if(usersclass.length()==6 && usersclass.substring(2,2)==","){
                        usersclass=usersclass.substring(0,2);
                        System.out.println(usersclass);
                    }
                }
                storageRef=storageRef.child(usersclass).child(email);
                System.out.println(usersclass);
                i=0;
                storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference prefix : listResult.getPrefixes()) {

                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.

                            System.out.println(prefix.getName());
                            int resourseId = getResources().getIdentifier("button" +i, "id", getPackageName());
                            i=i+1;
                            new_book_btn = findViewById(resourseId);
                            new_book_btn.setVisibility(View.VISIBLE);
                            new_book_btn.setText(prefix.getName());
                            new_book_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent= new Intent(getApplicationContext(),Signature.class);
                                    startActivity(intent);
                                    currentsubj=prefix.getName();
                                    finish();
                                }
                            });

                        }

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                        }
                        progressDialog.dismiss();
                    }});


            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.new_book :
                Intent intent= new Intent(getApplicationContext(),create_new_book.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.show_my_class:
                Intent intent2= new Intent(getApplicationContext(),Select_class.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent1= new Intent(getApplicationContext(),Login.class);
                startActivity(intent1);
                finish();

        }
        return super.onOptionsItemSelected(item);
    }
}