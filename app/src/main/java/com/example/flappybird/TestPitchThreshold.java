package com.example.flappybird;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.content.res.ResourcesCompat;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.SilenceDetector;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.UniversalAudioInputStream;

public class TestPitchThreshold extends Activity {

    public TextView pitchText;
    public TextView word;
    private ProgressBar mProgress;
    public float temp_pitch;
    public float all_pitch;
    public float average_pitch;
    public int counter;
    private double threshold;
    private SilenceDetector silenceDetector;
    private AudioDispatcher dispatcher;
    private PitchDetectionHandler pdh;
    private AudioProcessor pitchProcessor;
    private Boolean has_set;
    private Button re;
    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.pitch_setting);

        LinearLayout l=findViewById(R.id.pitchLayout);
        l.setBackgroundResource(R.drawable.background);

        re=findViewById(R.id.restart);
        score=findViewById(R.id.score);
        Button ok=findViewById(R.id.submit);
        word = findViewById(R.id.description_word);
        pitchText = findViewById(R.id.Pitch);
        word.setTypeface(Typeface.createFromAsset(getAssets(),"font/sansitaone-webfont.ttf"));
        pitchText.setTypeface(Typeface.createFromAsset(getAssets(),"font/sansitaone-webfont.ttf"));
        re.setTypeface(Typeface.createFromAsset(getAssets(),"font/sansitaone-webfont.ttf"));
        ok.setTypeface(Typeface.createFromAsset(getAssets(),"font/sansitaone-webfont.ttf"));
        score.setTypeface(Typeface.createFromAsset(getAssets(),"font/sansitaone-webfont.ttf"));
        mProgress=findViewById(R.id.progressBar);

        mProgress.setProgress(0);
        mProgress.setMax(100);

        has_set=false;
        getLevel();
        getnoise();

    }

    public void getnoise(){

    }
    public void getLevel() {

        has_set=true;

        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e) {
                final float pitchInHz = res.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("pitch","thre");
                        temp_pitch = pitchInHz;
                    }
                });
            }
        };

        pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);
        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
    }

    public void Pitch_submit(View view) {

            if (dispatcher!=null) {
                dispatcher.removeAudioProcessor(pitchProcessor);
                dispatcher.stop();
                dispatcher=null;
                pdh=null;
                pitchProcessor=null;
            }

            has_set=false;
            Game.start = true;

            Intent intent = new Intent();
            intent.setClass(this, GameActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("PITCH", average_pitch);
            startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1)
        {
            String message=data.getStringExtra("score");
            Log.d("lastscore",message);
            score.setText("Last Score = "+message);
        }
    }
    public void start_pitch_test(View view) {
        mProgress.setProgress(0);
        all_pitch=0;
        counter=0;
        if(!has_set){
            getLevel();
        }
        //timer
        CountDownTimer timer = new CountDownTimer(3000+999, 1000){
            int progress=2;
            @Override
            public void onTick(long millisUntilFinished) {
                //word.setText(millisUntilFinished / 1000 + "s left");
                word.setText("Recording......");
                showPitch(temp_pitch);
                mProgress.incrementProgressBy(35);
                if(temp_pitch!=-1){
                    counter++;
                    all_pitch+=temp_pitch;
                }
            }
            @Override
            public void onFinish() {
                word.setText("Your average pitch is:");
                Log.d("aver",new Integer(counter).toString());
                if(counter==0){
                    average_pitch=200;
                }else {
                    average_pitch = all_pitch / counter;
                }
                showPitch(average_pitch);
                re.setText("re-Record");
            }
        };
        timer.start();

    }


    public void showPitch(float pitchInHz) {

        pitchText.setText("" + pitchInHz);

    }

}

