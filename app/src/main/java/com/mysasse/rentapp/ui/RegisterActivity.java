package com.mysasse.rentapp.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.mysasse.rentapp.HomeActivity;
import com.mysasse.rentapp.R;
import com.mysasse.rentapp.data.DatabaseHandler;
import com.mysasse.rentapp.data.models.User;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private static final int SELECT_IMAGE_RC = 22;

    private ImageView userAvatarCiv;
    private TextInputEditText nameTxt;
    private TextInputEditText emailTxt;
    private TextInputEditText phoneTxt;
    private TextInputEditText passwordTxt;
    private ProgressBar registerProgressBar;

    private Uri mImageUri;

    //Database Handler
    private DatabaseHandler dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Instantiate the database handler
        dbh = new DatabaseHandler(this);

        //Register the necessary views for the registration process
        userAvatarCiv = findViewById(R.id.user_avatar_civ);

        nameTxt = findViewById(R.id.name_txt);
        emailTxt = findViewById(R.id.email_txt);
        phoneTxt = findViewById(R.id.phone_txt);
        passwordTxt = findViewById(R.id.password_txt);
        registerProgressBar = findViewById(R.id.register_progress_bar);

        Button registerBtn = findViewById(R.id.register_btn);

        TextView gotoLoginTv = findViewById(R.id.goto_login_tv);

        //Circle Image View Click Listener
        userAvatarCiv.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            ComponentName componentName = intent.resolveActivity(getPackageManager());

            if (componentName != null) {
                startActivityForResult(intent, SELECT_IMAGE_RC);
            }

        });

        registerBtn.setOnClickListener(view -> {
            //Get all the user input
            String name = String.valueOf(nameTxt.getText());
            String email = String.valueOf(emailTxt.getText());
            String phone = String.valueOf(phoneTxt.getText());
            String password = String.valueOf(passwordTxt.getText());

            if (!hasValidData(name, email, phone, password)) return;

            User user = new User(name, email, phone, "TENANT");
            user.setPassword(password);

            //Image is optional during the registration
            if (mImageUri != null) {
                //Get and set image bytes
                Bitmap bitmap = ((BitmapDrawable) userAvatarCiv.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                user.setProfilePic(baos.toByteArray());
            }

            registerUser(user);

        });

        //Register a click listener for the text view
        gotoLoginTv.setOnClickListener(view -> finish());
    }

    private void registerUser(User user) {
        registerProgressBar.setVisibility(View.VISIBLE);

        if (dbh.addUser(user)) {
            Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            Toast.makeText(this, "A fatal error occurred consult with a developer", Toast.LENGTH_SHORT).show();
        }
        registerProgressBar.setVisibility(View.GONE);
    }

    private boolean hasValidData(String name, String email, String phone, String password) {

        if (TextUtils.isEmpty(name)) {
            nameTxt.setError("Name is required");
            nameTxt.requestFocus();
            return false;
        }

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

        if (TextUtils.isEmpty(phone)) {
            phoneTxt.setError("Phone is required");
            phoneTxt.requestFocus();
            return false;
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            phoneTxt.setError("Invalid phone number");
            phoneTxt.requestFocus();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE_RC) {
                assert data != null;

                mImageUri = data.getData();
                userAvatarCiv.setImageURI(mImageUri);

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            Log.d(TAG, "onActivityResult: something is not OK");
        }
    }

}
