package com.ragabaat.admincollage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UpoladBdfActivity extends AppCompatActivity {


    private CardView addPdf;
    private EditText pdfTitle;
    private Button uploadPdfeBtn;
    private TextView pdfTextView;
    private String pdfName,title;

    private final int REQ = 1;
    private Uri pdfData;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downLoadUrl = "";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upolad_bdf);



        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        addPdf = findViewById(R.id.addPdf);
        pdfTextView = findViewById(R.id.pdfTextView);
       pdfTitle = findViewById(R.id.pdfTitle);
        uploadPdfeBtn = findViewById(R.id.uploadPdfBtn);


        addPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        uploadPdfeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                title =pdfTitle.getText().toString();
                if (title.isEmpty()){
                    pdfTitle.setError("Enter Title");
                    pdfTitle.requestFocus();
                }else if (pdfData == null){
                    Toast.makeText(UpoladBdfActivity.this,"please upload Pdf file",Toast.LENGTH_LONG).show();

                }

                else {

                    uploadPdf();
                }

            }
        });

    }

    private void uploadPdf() {


        pd.setTitle("please Waite.....");
        pd.setMessage("Uploading Pdf");
        pd.show();

        StorageReference reference = storageReference.child("pdf/"+pdfName+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isCanceled());
                Uri uri = uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UpoladBdfActivity.this," there is problem for upload Pdf file",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void uploadData(String downLoadUrl) {

        String uniqueKey = databaseReference.child("pdf").push().getKey();
        HashMap data = new HashMap();

        data.put("pdfTitle",title);
        data.put("pdfUtl",downLoadUrl);

        databaseReference.child("pdf").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                pd.dismiss();
                Toast.makeText(UpoladBdfActivity.this," pdf file upload Successfully",Toast.LENGTH_LONG).show();

                pdfTitle.setText("");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();

                Toast.makeText(UpoladBdfActivity.this," field to upload Pdf ",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void openGallery() {

       Intent intent = new Intent();
       intent.setType("pdf/docs/ppt");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent,"Select Pdf File"),REQ);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode ==RESULT_OK){

            pdfData = data.getData();

            if (pdfData.toString().startsWith("content://")){

                Cursor cursor = null;

                try {
                    cursor =  UpoladBdfActivity.this.getContentResolver().query(pdfData,null,null,null,null);

                    if (cursor != null && cursor.moveToFirst()){
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if (pdfData.toString().startsWith("file://")){
                pdfName = new File(pdfData.toString()).getName();
            }
            pdfTextView.setText(pdfName);
        }
    }
}