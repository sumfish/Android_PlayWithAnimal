package com.example.flappybird;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GameActivity extends AppCompatActivity {

    private Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        game =new Game(this);
        setContentView(game);
        LocalBroadcastManager.getInstance(this).registerReceiver(new MessageHandler(),new IntentFilter("kill"));

        game.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        game.fly();

                        break;

                    case MotionEvent.ACTION_UP:


                        break;

                    default:
                        break;
                }

                return true;
            }
        });



    }

    private void killActivity() {
        Log.d("?????","finish");
        this.finish();
        overridePendingTransition(0,0);
    }

    public class MessageHandler extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            killActivity();
        }
    }



}
