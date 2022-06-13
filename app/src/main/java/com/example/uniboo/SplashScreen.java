package com.example.uniboo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    ImageView appLogo;
    TextView appName, appSubtitle;
    ProgressBar progressBar;
    Handler handler;
    Runnable run;
    int progressStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        appLogo = findViewById(R.id.appLogoID);
        appName = findViewById(R.id.appNameID);
        appSubtitle = findViewById(R.id.appSubtitleID);
        progressBar = findViewById(R.id.progressBarID);
        handler = new Handler();
        run = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, Membership.class);
                startActivity(i);
                finish();
            }
        };
        initialProgress();
    }

    public void initialProgress(){
        progressStatus = progressBar.getProgress();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < 85){
                    progressStatus = progressStatus + 1;


                    handler.post((new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);

                            if(progressStatus ==15){
                                appName.setVisibility(View.VISIBLE);

                            }
                            if(progressStatus ==25){
                                appSubtitle.setVisibility(View.VISIBLE);
                            }
                            if(progressStatus == 45){
                                appLogo.setVisibility(View.VISIBLE);

                            }
                        }
                    }));
                    try
                    {
                        Thread.sleep(65);
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                    }
                }
                handler.postDelayed(run, 65);
            }
        }).start();
    }
}