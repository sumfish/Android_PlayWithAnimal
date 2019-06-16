package com.example.flappybird;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

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

    AudioDispatcher dispatcher;
    AudioProcessor pitchProcessor;
    PitchDetectionHandler pdh;
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

        game = new Game(this, backgroundImage);

        FrameLayout frm = findViewById(R.id.frameLayout);
        frm.addView(game);

        // Set SurfaceView transparent
        game.setZOrderOnTop(true);
        SurfaceHolder sfhTrackHolder = game.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        LocalBroadcastManager.getInstance(this).registerReceiver(new MessageHandler(), new IntentFilter("kill"));

        final float pitchThre=getIntent().getFloatExtra("PITCH",0);
        Log.d("pitch",new Float(pitchThre).toString());
        getLevel(pitchThre);

        /*
        game.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        game.fly(3);
                        Log.d("bird",new Integer(game.mBird.posY).toString());
                        break;

                    case MotionEvent.ACTION_UP:


                        break;

                    default:
                        break;
                }

                return true;
            }
<<<<<<< Updated upstream
        });
=======
        });*/

    }

    public void getLevel(final float averge){


        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

        pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result,AudioEvent e) {
                final float pitchInHz = result.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("nowpitch", new Float(pitchInHz).toString());

                        if (game.mIsDrawing){

                            game.mCanvas = null;
                            long start = System.currentTimeMillis();
                            game.drawSomething();
                            game.randomEmoji--;
                            if(game.randomEmoji <= 0)
                                game.setEmoji();

                            long end = System.currentTimeMillis();

                            if (end - start < 30) {
                                try {
                                    Thread.sleep(30 - (end - start));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(pitchInHz!=-1) {
                                Log.d("nowpitch", new Float(pitchInHz).toString());
                                if (Math.abs(pitchInHz - averge) < 50) {
                                    if (pitchInHz > averge) {
                                        //fly
                                        game.fly(1);
                                    } else {
                                        game.fly(-1);
                                    }
                                } else if (Math.abs(pitchInHz - averge) < 100) {
                                    if (pitchInHz > averge) {
                                        game.fly(1.5);
                                    } else {
                                        game.fly(-1.5);
                                    }
                                } else if (Math.abs(pitchInHz - averge) < 150) {
                                    if (pitchInHz > averge) {
                                        game.fly(2);
                                    } else {
                                        game.fly(-2);
                                    }
                                }else if (Math.abs(pitchInHz - averge) < 200) {
                                    if (pitchInHz > averge) {
                                        game.fly(2.5);
                                    } else {
                                        game.fly(-2.5);
                                    }
                                } else {
                                    if (pitchInHz > averge) {
                                        game.fly(3);
                                    } else {
                                        game.fly(-3);
                                    }
                                }
                            }

                        }
                    }
                });
            }
        };
        pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);
        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();

    }


    private void killActivity() {

        Log.d("?????","finish");
        //////
        if (dispatcher!=null) {
            dispatcher.removeAudioProcessor(pitchProcessor);
            dispatcher.stop();
            dispatcher=null;
            pitchProcessor=null;
            pdh=null;
        }

        Intent intent=new Intent();
        intent.putExtra("score",new Integer(game.score).toString());
        Log.d("inscore",new Integer(game.score).toString());
        setResult(1,intent);
        finish();//finishing activity
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
