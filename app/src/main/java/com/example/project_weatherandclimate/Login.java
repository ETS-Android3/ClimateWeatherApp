package com.example.project_weatherandclimate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final class Login extends AppCompatActivity {
    private TextView createNewAccount = null;
    private TextView inputEmail = null;
    private TextView inputPassword = null;
    private Button loginButton = null;
    private FirebaseAuth mAuth = null;
    private ProgressBar progressBar = null;



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {


            Intent intent5 = new Intent(Login.this, Home.class);
            startActivity(intent5);

        } else {
            // No user is signed in
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         loginButton = findViewById(R.id.buttonLogin);
         progressBar = findViewById(R.id.progressBarLogin);
         inputEmail = findViewById(R.id.loginEmail);
         inputPassword = findViewById(R.id.loginPassword);
         mAuth = FirebaseAuth.getInstance();

        createNewAccount = findViewById(R.id.createNewAccount);
        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Registration.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    private void loginUser() {

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        //email
        if(email.isEmpty()){
            inputEmail.setError("Email Is Required");
            inputEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Please prvide a valide Mail !");
            inputEmail.requestFocus();
            return;
        }


        if(password.isEmpty()){
            inputPassword.setError("Password Is Required");
            inputPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            inputPassword.setError("please provide a password longer than 6 characters");
            inputPassword.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        startActivity(new Intent(Login.this,UserPreferences.class));
                        Toast.makeText(Login.this, "User has been logged successfully", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.VISIBLE);
                    }else {
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Please check your email !", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.VISIBLE);

                    }



                }else {
                    Toast.makeText(Login.this, "Failed to login ! Please check your credentials.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.VISIBLE);

                }

            }
        });

    }
}