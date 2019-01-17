package com.example.nikhi.getcabby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private ImageView logoImageView;
    private TextView text1 , text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logoImageView = (ImageView) findViewById(R.id.imageLogo);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);

        Animation animation = AnimationUtils.loadAnimation(this , R.anim.transition);
        logoImageView.startAnimation(animation);
        text1.startAnimation(animation);
        text2.startAnimation(animation);

        final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        Thread timer = new Thread()
        {
            public void run()
            {
                try{
                    sleep(3000);

                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();

    }
}
