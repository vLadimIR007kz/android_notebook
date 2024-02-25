package com.example.myapplication;

import static com.example.myapplication.MainActivity.currentsubj;
import static com.example.myapplication.MainActivity.usersclass;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.Feature;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.google.mlkit.vision.text.TextRecognition;

import okio.ByteString;
import yuku.ambilwarna.AmbilWarnaDialog;

public class Signature extends AppCompatActivity {
    SignatureView signatureView;
    ImageButton imgEraser,imgColor, imgSave, imgRight, imgLeft;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    SeekBar seekBar;
    TextView txtpensize;

    File path=new File(Environment.getExternalStorageDirectory()+"/Documents/myPaintings/");
    String filename, subj=currentsubj, email;
    ImageView img;
    int counter;
    boolean closed=false;
    int defaultcolor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //handler2.postDelayed(runnable2, 10000);
        //handler.postDelayed(runnable, 30000);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        defaultcolor= ContextCompat.getColor(Signature.this,R.color.black);
        img=findViewById(R.id.imgview);
        email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        path=new File(path+"/"+usersclass+"/"+email+"/");
        signatureView=findViewById(R.id.signature_view);
        imgEraser=findViewById(R.id.btnEraser);
        imgColor= findViewById(R.id.btnColor);
        imgSave=findViewById(R.id.btnSave);
        imgRight=findViewById(R.id.btnright);
        imgLeft=findViewById(R.id.btnleft);
        seekBar=findViewById(R.id.penSize);
        txtpensize=findViewById(R.id.TxtPenSize);
        filename=path+"/"+subj+"/"+counter+".png";
        path.mkdirs();
        counter=0;
        try {
            loadImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtpensize.setText(progress+"dp");
                signatureView.setPenSize(progress);
                seekBar.setMax(50);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        imgColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
        imgEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clearCanvas();
            }
        });
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    filename=path+"/"+subj+"/"+counter+".png";
                    new File(path+"/"+subj+"/").mkdirs();
                    saveImage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                filename=path+"/"+subj+"/"+counter+".png";
                new File(path+"/"+subj+"/").mkdirs();
                try {
                    saveImage();
                } catch (IOException e) {

                }
                signatureView.clearCanvas();
                counter=counter+1;
                filename=path+"/"+subj+"/"+counter+".png";
                try{
                    loadImage();
                }
                catch (IOException e){

                }
            }
        });
        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter!=0){
                    new File(path+"/"+subj+"/").mkdirs();
                    filename=path+subj+"/"+counter+".png";
                    try {
                        saveImage();
                    } catch (IOException e) {

                    }
                    counter=counter-1;
                    signatureView.clearCanvas();
                    new File(path+"/"+subj+"/").mkdirs();
                    try {
                        filename=path+"/"+subj+"/"+counter+".png";
                        loadImage();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }
    private void loadImage() throws IOException {
        File file= new File(filename);
        try{

            download();

            if(file.exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(file)).copy(Bitmap.Config.ARGB_8888, true);
                Context context = getApplicationContext();
                signatureView.setBitmap(bmp);
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e){

        }

    }

    private void saveImage() throws IOException {
            File file = new File(filename);
            Bitmap bm = signatureView.getSignatureBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bmData = bos.toByteArray();
            upload(bmData, filename);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bmData);
            fos.flush();
            fos.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
    private void upload(byte[] bmdata, String filename)throws IOException {
        UploadTask uploadTask = storageRef.child(usersclass).child(email).child(subj).child(counter+".png").putBytes(bmdata);
    }
    private void download()throws IOException {
        ProgressDialog progressDialog=new ProgressDialog(Signature.this);
        progressDialog.setTitle("loading img");
        progressDialog.show();
        File path1= new File(path+"/"+subj+"/"+counter+".png");
        File path2=new File(path+"/"+subj);
        if(!path2.exists()){
            path2.mkdirs();
        }
        final long ONE_MEGABYTE = 1024000 * 1024000;
        StorageReference gsReference = storage.getReferenceFromUrl("gs://tetrad-app.appspot.com/"+usersclass+"/"+email+
        "/"+subj+"/"+counter+".png");

        gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(path1);
                    fos.write(bytes);
                    fos.flush();
                    fos.close();
                    Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(path1)).copy(Bitmap.Config.ARGB_8888, true);
                    Context context = getApplicationContext();
                    signatureView.setBitmap(bmp);

                    progressDialog.dismiss();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
            }
        });

    }
   /* private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!closed) {
                try {
                    loadImage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                handler.postDelayed(this, 50000); // Повторный вызов функции через 15 секунд
            }
        }
    };
    private Handler handler2 = new Handler();
    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            if (!closed) {
                try {
                    saveImage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                handler2.postDelayed(this, 35000); // Повторный вызов функции через 15 секунд
            }
        }
    };*/
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.secondary_menu, menu);
        return true;

    }
    private void openColorPicker() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultcolor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultcolor=color;
                signatureView.setPenColor(color);
            }
        });
        ambilWarnaDialog.show();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.getback:
                Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                closed=true;
                finish();
                return true;
            case R.id.recognize:
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!textRecognizer.isOperational()) {

                }

                Frame frame = new Frame.Builder().setBitmap(signatureView.getSignatureBitmap()).build();
                SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);

                StringBuilder recognizedText = new StringBuilder();

                for (int i = 0; i < textBlocks.size(); i++) {
                    TextBlock textBlock = textBlocks.valueAt(i);
                    recognizedText.append(textBlock.getValue());
                    recognizedText.append("\n");
                }
                System.out.println(recognizedText);
                Toast.makeText(this, recognizedText, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
