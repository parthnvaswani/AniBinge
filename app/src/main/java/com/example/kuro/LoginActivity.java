package com.example.kuro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    EditText emailAddress, pwd;
    Button login;
    TextView resetpwd, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailAddress = findViewById(R.id.emailAddress);
        pwd = findViewById(R.id.password);
        login = findViewById(R.id.login);
        resetpwd = findViewById(R.id.resetPassword);
        register = findViewById(R.id.goToSignUpActivity);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        login.setOnClickListener(view -> {
            String email = emailAddress.getText().toString().trim();
            String password = pwd.getText().toString();

            if (email.length() == 0 || password.length() == 0) {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.setMessage("Logging in...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                progressDialog.cancel();
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }).addOnFailureListener(e -> {
                progressDialog.cancel();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        resetpwd.setOnClickListener(view -> {
            String email = emailAddress.getText().toString();

            if (email.length() == 0) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.setMessage("Reset Password");
            progressDialog.show();
            firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(unused -> {
                progressDialog.cancel();
                Toast.makeText(LoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                progressDialog.cancel();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        register.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }
}