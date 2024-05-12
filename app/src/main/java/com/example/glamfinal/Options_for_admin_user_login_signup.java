package com.example.glamfinal;

import static com.example.glamfinal.R.layout.activity_options_for_admin_user_login_signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Options_for_admin_user_login_signup extends AppCompatActivity {

    private Button btnClientSignup;
    private Button btnAdminLogin;
    private Button btnAdminSignup;
    private Button btnClientLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_options_for_admin_user_login_signup);

        // Initialize buttons
        btnClientLogin = findViewById(R.id.btnClientLogin);
        btnClientSignup = findViewById(R.id.btnClientSignup);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);
        btnAdminSignup = findViewById(R.id.btnAdminSignup);

        // Set click listeners
        btnClientLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle client login button click
                startActivity(new Intent(Options_for_admin_user_login_signup.this, Login.class));
            }
        });

        btnClientSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle client signup button click
                startActivity(new Intent(Options_for_admin_user_login_signup.this, Signup.class));
            }
        });

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle admin login button click
                startActivity(new Intent(Options_for_admin_user_login_signup.this, Admin_Login.class));
            }
        });

        btnAdminSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle admin signup button click
                startActivity(new Intent(Options_for_admin_user_login_signup.this, Admin_Signup.class));
            }
        });
    }
}
