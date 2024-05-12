package com.example.glamfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class admin_home extends AppCompatActivity {

    FloatingActionButton logout_botton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);

        // Apply animations to CardViews
        animateCard(findViewById(R.id.card_bookings), R.anim.slide_in_right);
        animateCard(findViewById(R.id.card_my_bookings), R.anim.slide_in_left);
        animateCard(findViewById(R.id.card_old_bookings), R.anim.slide_in_right);
        animateCard(findViewById(R.id.card_give_review), R.anim.slide_in_left);
        animateCard(findViewById(R.id.card_check_reviews), R.anim.slide_in_right);
        animateCard(findViewById(R.id.card_update_user_info), R.anim.slide_in_left);
        animateCard(findViewById(R.id.card_ai_assistant), R.anim.slide_from_left_to_right);

        // Set click listeners for each CardView
        findViewById(R.id.card_bookings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin_home.this, ViewBookingsActivity.class));
            }
        });

        findViewById(R.id.card_my_bookings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin_home.this, AddServiceActivity.class));
            }
        });

        findViewById(R.id.card_old_bookings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin_home.this, Past_Bookings.class));
            }
        });

        findViewById(R.id.card_give_review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin_home.this, ServicesActivity.class));
            }
        });

        findViewById(R.id.card_check_reviews).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin_home.this, ReviewListActivity.class));
            }
        });

        findViewById(R.id.card_update_user_info).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin_home.this, update_user_info.class));
            }
        });

        findViewById(R.id.card_ai_assistant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin_home.this, chatbot.class));
            }
        });

        logout_botton = findViewById(R.id.Logout_admin);

        logout_botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("login", false);
                editor.putBoolean("admin",false);
                editor.apply();

                startActivity(new Intent(admin_home.this, Options_for_admin_user_login_signup.class));
                Toast.makeText(admin_home.this, "LogOut Successfully !!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // Method to apply animation to a CardView
    private void animateCard(View cardView, int animationResId) {
        Animation animation = AnimationUtils.loadAnimation(this, animationResId);
        cardView.startAnimation(animation);
    }
}
