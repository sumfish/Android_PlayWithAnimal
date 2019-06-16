package com.example.flappybird;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.flappybird.Face.CameraSource;
import com.example.flappybird.Face.CameraSourcePreview;
import com.example.flappybird.Face.FaceDetectionProcessor;
import com.example.flappybird.Face.GraphicOverlay;

import java.io.IOException;

public class GameActivity extends Activity {

    private static final String TAG = "GameActivity";
    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private ImageView backgroundImage;

    private FaceDetectionProcessor faceDetectionProcessor;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_game);

        faceDetectionProcessor = new FaceDetectionProcessor();
        preview = findViewById(R.id.firePreview);
        graphicOverlay = findViewById(R.id.fireFaceOverlay);
        backgroundImage = findViewById(R.id.backgroundImage);

        createCameraSource();
        startCameraSource();

        game = new Game(this, backgroundImage, faceDetectionProcessor);

        FrameLayout frm = findViewById(R.id.frameLayout);
        frm.addView(game);

        // Set SurfaceView transparent
        game.setZOrderOnTop(true);
        SurfaceHolder sfhTrackHolder = game.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        LocalBroadcastManager.getInstance(this).registerReceiver(new MessageHandler(), new IntentFilter("kill"));

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
        //Log.d("?????","finish");
        this.finish();
        overridePendingTransition(0, 0);
    }

    public class MessageHandler extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            killActivity();
        }
    }

    private void createCameraSource() {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        try {
            Log.i(TAG, "Using Face Detector Processor");
            cameraSource.setMachineLearningFrameProcessor(faceDetectionProcessor);

        } catch (Exception e) {
            Log.e(TAG, "Can not create image processor", e);
            Toast.makeText(
                    getApplicationContext(),
                    "Can not create image processor: " + e.getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e("Game", "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }
}
