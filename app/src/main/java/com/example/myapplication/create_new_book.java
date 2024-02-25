package com.example.myapplication;

import static com.example.myapplication.MainActivity.currentsubj;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class create_new_book extends AppCompatActivity {
    TextInputEditText new_book_name;
    Button btn_create, new_book_btn;
    ProgressBar progressBar;
    TextView return_to_main;
    String new_book, button="button";
    public static int integer=0;
    public static String[] names =new String[24];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_book);
        new_book_name=findViewById(R.id.new_book_name);
        btn_create=findViewById(R.id.btn_create);
        progressBar=findViewById(R.id.progress_bar);
        return_to_main=findViewById(R.id.return_to_main);
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
                new_book=String.valueOf(new_book_name.getText());
                currentsubj=new_book;
                Intent intent= new Intent(getApplicationContext(),Signature.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public int getMyVariable() {
        return integer;
    }
}