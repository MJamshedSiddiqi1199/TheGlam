package com.example.glamfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ServicesActivity extends AppCompatActivity {

    TextView tvServicesTitle;
    FrameLayout fv_haircut, fv_facial, fv_makeup;
    FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isAdmin = prefs.getBoolean("admin", false);

        fv_haircut = findViewById(R.id.fv_haircut);
        fv_facial = findViewById(R.id.fv_facial);
        fv_makeup = findViewById(R.id.fv_makeup);

        actionButton=findViewById(R.id.actionButton);

        // Hide action button if the user is an admin
        if (isAdmin) {
            actionButton.setVisibility(View.GONE);
        }

        fv_haircut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, Service_List.class);
                intent.putExtra("service_id", 2);
                startActivity(intent);
            }
        });

        fv_facial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, Service_List.class);
                intent.putExtra("service_id", 3);
                startActivity(intent);
            }
        });

        fv_makeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, Service_List.class);
                intent.putExtra("service_id", 1);
                startActivity(intent);
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        animateServiceBoxRightToLeft(R.id.tvServicesTitle);
        animateServiceBoxLeftToRight(R.id.fv_haircut);
        animateServiceBoxRightToLeft(R.id.fv_facial);
        animateServiceBoxRightToLeft(R.id.fv_makeup);
    }

    private void animateServiceBoxRightToLeft(int resourceId) {
        View viewToAnimate = findViewById(resourceId);
        if (viewToAnimate!= null) {
            Animation slideInLeftToRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
            viewToAnimate.startAnimation(slideInLeftToRight);
        }
    }

    private void animateServiceBoxLeftToRight(int resourceId) {
        View viewToAnimate = findViewById(resourceId);
        if (viewToAnimate!= null) {
            Animation slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
            viewToAnimate.startAnimation(slideInLeft);
        }
    }
}
