package com.ragabaat.admincollage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadNotice extends AppCompatActivity {

    private CardView addImage;
    private EditText noticeTitle;
    private Button uploadNoticeBtn;

    private final int REQ = 1;
    private Bitmap bitmap;
    private ImageView noticeImageView;
    private DatabaseReference reference,dbRef;
    private StorageReference storageReference;
    String downLoadUrl = "";
    private ProgressDialog bd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        setContentView(R.layout.activity_upload_notice);

        addImage = findViewById(R.id.addImage);
        noticeImageView = findViewById(R.id.noticeImageView);
        noticeTitle = findViewById(R.id.noticeTitle);
        uploadNoticeBtn = findViewById(R.id.uploadNoticeBtn);
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        bd = new ProgressDialog(this);
        
        
        uploadNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noticeTitle.getText().toString().isEmpty()){
                    noticeTitle.setError(getString(R.string.emptytitle));
                    noticeTitle.requestFocus();
                    
                } else if (bitmap == null){
                    uploadData();
                }
                else {
                    
                    uploadImage();
                }
            }
        });
        
        
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();
            }
        });
    }

    private void uploadImage() {

        bd.setMessage("Uploading");
        bd.show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Notice").child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(UploadNotice.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                
                if (task.isSuccessful()){
                    
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    
                                    downLoadUrl = String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                }
                else {
                    bd.dismiss();
                    Toast.makeText(UploadNotice.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
        });
        
    }

    private void uploadData() {

        String title = noticeTitle.getText().toString();

        dbRef = reference.child("Notice");
        final String uniqueKey = dbRef.push().getKey();
        Calendar calForData = Calendar.getInstance();
        SimpleDateFormat currentData = new SimpleDateFormat("dd-MM-yy");
        String date = currentData.format(calForData.getTime());


        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh,mm a");
        String time = currentTime.format(calForTime.getTime());

        NoticeData noticeData = new NoticeData(title,downLoadUrl,date,time,uniqueKey);

         dbRef.child(uniqueKey).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {

                 bd.dismiss();
                 Toast.makeText(UploadNotice.this,"Notice Upload it",Toast.LENGTH_LONG).show();
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {

                 bd.dismiss();
                 Toast.makeText(UploadNotice.this,"Something went wrong",Toast.LENGTH_LONG).show();
             }
         });

        
    }

    private void openGallery() {

        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          startActivityForResult(pickImage,REQ);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode ==RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            noticeImageView.setImageBitmap(bitmap);
        }
    }
}