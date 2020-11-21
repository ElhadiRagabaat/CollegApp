package com.ragabaat.admincollage.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.ragabaat.admincollage.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateTeacherActivity extends AppCompatActivity {

    private CircleImageView updateTeacherImage;
    private EditText updateTeacherName,updateTeacherEmail,updateTeacherPost;
    private Button updateTeacherBtn,deleteTeacherBtn;

    private String name,email,image,post;
    private final int REQ= 1;
    private Bitmap bitmap = null;

    private StorageReference storageReference;
    private DatabaseReference reference;
    private String  downLoadUrl,category,uniqukey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        ///// for revieve data by intent to this activity

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        post = getIntent().getStringExtra("post");
        image = getIntent().getStringExtra("image");

         uniqukey = getIntent().getStringExtra("key");

         category = getIntent().getStringExtra("category");


        reference = FirebaseDatabase.getInstance().getReference().child("teacher");
        storageReference = FirebaseStorage.getInstance().getReference();


         updateTeacherImage =findViewById(R.id.updateTeacherImage);
         updateTeacherName = findViewById(R.id.updateTeacherName);
          updateTeacherEmail = findViewById(R.id.updateTeacherEmail);
           updateTeacherPost = findViewById(R.id.updateTeacherPost);
            updateTeacherBtn  = findViewById(R.id.updateTeacherBtn);
            deleteTeacherBtn  = findViewById(R.id.deleteTeacherBtn);



            //// for the retrieve image
        try {
            Picasso.get().load(image).into(updateTeacherImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //// for the all info
        updateTeacherEmail.setText(email);
        updateTeacherName.setText(name);
        updateTeacherPost.setText(post);

        updateTeacherImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                openGallery();

                return false;
            }
        });

        updateTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = updateTeacherName.getText().toString();
                email = updateTeacherEmail.getText().toString();
                post = updateTeacherPost.getText().toString();

                checkValidation();
            }
        });

        deleteTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                deleteData();
            }
        });

    }

    private void deleteData() {

        reference.child(category).child(uniqukey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(UpdateTeacherActivity.this,"Teacher Info deleted Successfully ",Toast.LENGTH_LONG)
                        .show();
                Intent intent = new Intent(UpdateTeacherActivity.this,UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(UpdateTeacherActivity.this," Something went wrong ",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void checkValidation() {

        if (name.isEmpty()){
            updateTeacherName.setError("Empty");
            updateTeacherName.requestFocus();

        }else if (email.isEmpty()){
            updateTeacherEmail.setError("Empty");
            updateTeacherEmail.requestFocus();

        }else if (post.isEmpty()){
            updateTeacherPost.setError("Empty");
            updateTeacherPost.requestFocus();

        }else if (bitmap == null){

            updateData("");

        }else {

            uploadImage();
        }


    }

    private void updateData(String s) {

        HashMap hp = new HashMap();
        hp.put("name",name);
        hp.put("email",email);
        hp.put("post",post);
        hp.put("image",s);



        reference.child(category).child(uniqukey).updateChildren(hp)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {

                        Toast.makeText(UpdateTeacherActivity.this,"Teacher Info Updated Successfully ",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(UpdateTeacherActivity.this,UpdateFaculty.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(UpdateTeacherActivity.this," Something went wrong ",Toast.LENGTH_LONG).show();
            }
        });

    }


    private void uploadImage() {

//        pd.setMessage("Uploading");
//        pd.show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Teachers").child(finalImage+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(UpdateTeacherActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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

                                    updateData(downLoadUrl);
                                }
                            });
                        }
                    });
                }
                else {
//                    pd.dismiss();
                    Toast.makeText(UpdateTeacherActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    private void openGallery() {

        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);

    }



    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode ==RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateTeacherImage.setImageBitmap(bitmap);
        }
    }
}