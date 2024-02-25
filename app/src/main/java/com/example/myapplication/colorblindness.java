package com.example.myapplication;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class colorblindness extends AppCompatActivity {

    Button buttonnored, buttonnoblue, buttonnogreen;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.color);
        buttonnoblue=findViewById(R.id.btn_noblue);
        buttonnored=findViewById(R.id.btn_nored);
        buttonnogreen=findViewById(R.id.btn_nogreen);
        textView=findViewById(R.id.text_return);

        buttonnoblue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        buttonnogreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        buttonnored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorMatrix colorMatrix = new ColorMatrix();
                float[] cmData = new float[]{
                        1, 0, 0, 0, 0, // Красный
                        0, 1, 0, 0, 0, // Зеленый
                        0, 0, 1, 0, 0, // Синий
                        0, 0, 0, 1, 0  // Альфа
                        };
                colorMatrix= new ColorMatrix(cmData);
                ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
                getWindow().getDecorView().getRootView().getBackground().setColorFilter(colorFilter);


            }
        });
    }
}
