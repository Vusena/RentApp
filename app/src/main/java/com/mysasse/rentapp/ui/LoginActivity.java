package com.mysasse.rentapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mysasse.rentapp.R;
import com.mysasse.rentapp.data.DatabaseHandler;
import com.mysasse.rentapp.data.models.User;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private TextInputEditText emailTxt;
    private TextInputEditText passwordTxt;

    private ProgressBar loginProgressBar;
    private DatabaseHandler dbh;

    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //Instantiate the database handler
        dbh = new DatabaseHandler(this);

        users = dbh.getAllUsers();

        //Register necessary login views
        emailTxt = findViewById(R.id.email_txt);
        passwordTxt = findViewById(R.id.password_txt);
        loginProgressBar = findViewById(R.id.login_progress_bar);
        Button loginBtn = findViewById(R.id.login_btn);
        TextView gotoRegisterTv = findViewById(R.id.goto_register_tv);

        //Register a click listener on the text view
        gotoRegisterTv.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

        //Register click listener for the login button
        loginBtn.setOnClickListener(view -> {
            //Check for user existence and login if possible
            String email = String.valueOf(emailTxt.getText());
            String password = String.valueOf(passwordTxt.getText());

            if (!hasValidData(email, password)) return;

            loginUser(email, password);

        });
    }

    private void loginUser(String email, String password) {
        User currentUser = null;
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email.trim())) {
                Log.d(TAG, "loginUser: got the email");
                currentUser = user;
                break;
            }
        }

        if (currentUser != null) {
            //Confirm the password
            if (currentUser.getPassword().equalsIgnoreCase(password.trim())) {
                Log.d(TAG, "loginUser: password matches");
                //Set a shared preferences
                SharedPreferences preferences = getSharedPreferences(getString(R.string.credentials_text), MODE_WORLD_READABLE);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(getString(R.string.email_text), currentUser.getEmail());
                editor.putInt(getString(R.string.id_text), currentUser.getId());
                editor.apply();
                Toast.makeText(this, "User Logged in successfully", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No user with such an email", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasValidData(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailTxt.setError("Email is required");
            emailTxt.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTxt.setError("Invalid email");
            emailTxt.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordTxt.setError("Password is required");
            passwordTxt.requestFocus();
            return false;
        }

        if (password.trim().length() < 6) {
            passwordTxt.setError("Minimum of 6 chars required");
            passwordTxt.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
