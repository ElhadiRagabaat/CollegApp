package com.ragabaat.admincollage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button adminLoginfBtn;
    private EditText adminLoginEmail,adminLoginPassword;
    private FirebaseAuth mAuth;
    private String password,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        adminLoginfBtn = findViewById(R.id.adminLoginfBtn);
        adminLoginEmail = findViewById(R.id.adminLoginEmail);
        adminLoginPassword = findViewById(R.id.adminLoginPassword);

        mAuth  = FirebaseAuth.getInstance();


        adminLoginfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password = adminLoginPassword.getText().toString();
                email= adminLoginEmail.getText().toString();

                if (email.isEmpty()){
                    adminLoginEmail.setError("Please Enter Email");
                    adminLoginEmail.requestFocus();
                }
                else   if (password.isEmpty()){
                    adminLoginPassword.setError("Please Enter Password");
                    adminLoginPassword.requestFocus();
                }
                else {

                    loginfunc();
                }


            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    private void loginfunc() {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Login Successfully.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            // ...
                        }

                        // ...
                    }
                });
    }
}