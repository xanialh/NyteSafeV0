package com.example.nytesafev0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextEmail, editTextPassword; // Variables for the e-mail and password text boxes
    private Button buttonLogin, buttonSignUpPage; // Variables for the log in and sign up buttons

    private FirebaseAuth authenticator; // Variable for a Firebase authenticator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail); // Set variables for the text boxes
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (Button) findViewById(R.id.buttonLogin); // Set variables for the buttons
        buttonSignUpPage = (Button) findViewById(R.id.buttonSignUpPage);

        buttonLogin.setOnClickListener(this); // Add a listeners for clicking the buttons
        buttonSignUpPage.setOnClickListener(this);

        authenticator = FirebaseAuth.getInstance(); // Create an instance of a firebase authenticator
    }

    @Override
    public void onClick(View clickedElement) { // When something in this page is clicked
        switch (clickedElement.getId()) { // Get the ID of the clicked element
            case R.id.buttonLogin:
                userLogin(); // Start user login process
                break;

            case R.id.buttonSignUpPage:
                startActivity(new Intent(this, SignUp.class));
                break;
        }
    }

    public void userLogin() {
        String email = editTextEmail.getText().toString().trim(); // Get the e-mail in the e-mail text box
        String password = editTextPassword.getText().toString().trim(); // Get the password in the password text box

        if (email.isEmpty()) { // If the e-mail is missing
            editTextEmail.setError("E-Mail is required."); // Set an error
            editTextEmail.requestFocus();
            return; // End the method early
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { // If the e-mail isn't valid
            editTextEmail.setError("Please enter a valid email."); // Set an error
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) { // If the password is missing
            editTextPassword.setError("Password is required."); // Set an error
            editTextPassword.requestFocus();
            return; // End the method early
        }

        // If the code reaches here, start attempting a log-in

        authenticator.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) { // If the login was successful (correct details)
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Get the user that just logged in from Firebase Authentication

                    if (user.isEmailVerified()) { // If the user is verified
                        startActivity(new Intent(Login.this, HomePage.class)); // Go to the home page
                    }
                    else { // If the user isn't verified
                        user.sendEmailVerification(); // Send an email verification link
                        Toast.makeText(Login.this, "Please check your e-mail to verify your account. Make sure to check your spam folder.", Toast.LENGTH_LONG).show(); // Message for user
                    }
                }
                else { // If the login failed (incorrect details or other reason)
                    Toast.makeText(Login.this, "Failed to log in. Are your login details correct?", Toast.LENGTH_LONG).show(); // Show an error message
                }
            }
        });
    }
}