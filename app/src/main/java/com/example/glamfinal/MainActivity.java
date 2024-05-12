package com.example.glamfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Find the ImageView
        ImageView splashIcon = findViewById(R.id.splash_icon);

        // Load animation
        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_from_left_to_right);

        // Set animation to ImageView
        splashIcon.startAnimation(slideAnimation);

        // Start next activity after animation completes
        slideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Start the main activity
                SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                boolean check_admin = prefs.getBoolean("admin", false);
                boolean login = prefs.getBoolean("login", false);

                if(check_admin==false && login==false)
                {
                    Intent intent = new Intent(MainActivity.this, Options_for_admin_user_login_signup.class);
                    startActivity(intent);
                }
                else if(check_admin==false && login==true)
                {
                    Intent intent = new Intent(MainActivity.this, home.class);
                    startActivity(intent);
                }
                else if((check_admin == true) && (login == true))
                {
                    Intent intent = new Intent(MainActivity.this, admin_home.class);
                    startActivity(intent);
                }

                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }
}
