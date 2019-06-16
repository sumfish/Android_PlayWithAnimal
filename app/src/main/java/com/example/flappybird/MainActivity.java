package com.example.flappybird;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private  Button b;
    private long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        b= findViewById(R.id.button);
        //Game.startButton = b;
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(new Game(this));
    }

    public void startGame(View view){
        Game.start = true;
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > TIME_INTERVAL) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, GameActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            mLastClickTime = nowTime;
        }


    }

    public void reset(){

        setContentView(R.layout.activity_main);
        b.setVisibility(View.VISIBLE);
    }


}
