package com.example.kuro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuro.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    EditText fullName, emailAddress, pwd;
    Button signup;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        firebaseFirestore = FirebaseFirestore.getInstance();
        fullName = findViewById(R.id.fullName);
        emailAddress = findViewById(R.id.emailAddress);
        pwd = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.goToLoginActivity);

        signup.setOnClickListener(view -> {
            String name = fullName.getText().toString().trim();
            String email = emailAddress.getText().toString().trim();
            String password = pwd.getText().toString();

            if(name.length()==0||email.length()==0||password.length()==0){
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
                progressDialog.cancel();
                firebaseFirestore.collection("User").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).set(new UserModel(name, email));
            }).addOnFailureListener(e -> {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            });
        });
        login.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
}

