package com.example.project_weatherandclimate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.FirebaseDatabase;

public final class Registration extends AppCompatActivity implements View.OnClickListener{
    private TextView inputFirstName, inputLastName, inputEmail, inputPassword, inputCity;
    private Button registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        inputFirstName = findViewById(R.id.firstName);
        inputLastName = findViewById(R.id.lastName);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputCity = findViewById(R.id.city);
        registerButton = findViewById(R.id.button);
        registerButton.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);




    }

    @Override
    public void onClick(View v) {

        registerUser();

    }

    private void registerUser() {

        String firstName = inputFirstName.getText().toString();
        String lastName = inputLastName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String city = inputCity.getText().toString();

        //Fist Name
        if(firstName.isEmpty()){
            inputFirstName.setError("First Name Is Required");
            inputFirstName.requestFocus();
            return;
        }

        //Last Name
        if(lastName.isEmpty()){
            inputLastName.setError("Last Name Is Required");
            inputLastName.requestFocus();
            return;
        }

        //City Name
        if(city.isEmpty()){
            inputLastName.setError("Last Name Is Required");
            inputLastName.requestFocus();
            return;
        }

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

        //Password
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
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                  User user = new User(firstName, lastName,city);
                  FirebaseDatabase.getInstance().getReference("users")
                          .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                          .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful()){
                              Toast.makeText(Registration.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                              progressBar.setVisibility(View.VISIBLE);
                              FirebaseAuth.getInstance().signOut();
                              startActivity(new Intent(Registration.this, Login.class));
                          }else {
                              Toast.makeText(Registration.this, "Failed to Register User", Toast.LENGTH_LONG).show();
                              progressBar.setVisibility(View.GONE);

                          }
                      }
                  });
                }else {
                    Toast.makeText(Registration.this, "Failed to Register ", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);

                }
            }
        });

    }
}