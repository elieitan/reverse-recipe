package com.example.reverserecipe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.reverserecipe.R;
import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {
    ImageView background;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        background = (ImageView) findViewById(R.id.s_img);
        auth = FirebaseAuth.getInstance();

        Glide.with(this)
                .load(R.drawable.splash)
                .into(background);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

               if (auth!=null&&auth.getCurrentUser()!=null){
                   startActivity(new Intent(Splash.this,MainActivity.class));
               }else {
                   startActivity(new Intent(Splash.this,LoginActivity.class));
               }
                finish();

            }
        }, 3000);
    }


}
