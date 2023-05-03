package com.example.nytesafev0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonAcceptTerms; // Accept Terms & Conditions button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAcceptTerms = (Button) findViewById(R.id.buttonAcceptTerms); // Set the variable to the Accept T & C button
        buttonAcceptTerms.setOnClickListener(this); // Add a listener for clicking the button
    }

    @Override
    public void onClick(View clickedElement) { // When something in this page is clicked
        if (clickedElement.getId() == R.id.buttonAcceptTerms){ // If it's the Accept T & C button
            startActivity(new Intent(this, Login.class)); // Go to the login page
        }
    }
}