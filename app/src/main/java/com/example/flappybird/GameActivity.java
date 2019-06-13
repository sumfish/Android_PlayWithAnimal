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

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(new Game(this));
        LocalBroadcastManager.getInstance(this).registerReceiver(new MessageHandler(),new IntentFilter("kill"));
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

    public void reset(){

    }

}
