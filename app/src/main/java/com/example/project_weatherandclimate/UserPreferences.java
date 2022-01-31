package com.example.project_weatherandclimate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final  class UserPreferences extends AppCompatActivity {
    private TextView inputCity = null;
    private Button confirmButton = null;
    private String cityName = null;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preferences);

        inputCity = findViewById(R.id.citypreferences);
        confirmButton = findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cityName = inputCity.getText().toString();



                Intent intent = new Intent(UserPreferences.this, Home.class);
                startActivity(intent);
            }
        });




    }

    public String Usercity (){

        return cityName;
    }


}