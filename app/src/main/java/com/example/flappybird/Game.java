package com.example.flappybird;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.example.flappybird.Face.CameraSource;
import com.example.flappybird.Face.CameraSourcePreview;
import com.example.flappybird.Face.GraphicOverlay;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    public Bird mBird;
    private SurfaceHolder mSurfaceHolder;
    public Canvas mCanvas;
    private Bitmap background;
    private ArrayList<Bitmap> birdArray;
    public static boolean mIsDrawing;
    private int FPS = 30;
    private Pipe mPipe1,mPipe2,mPipe3,mPipe4,mPipe5,mPipe6;
    private int screenHeight;
    private int screenWidth;
    private int gap = 600;
    private int birdSize = 100;
    private  Thread t;
    private int G =1;
    private boolean isFlying = false;
    private int counterG;
    private int counterF;
    private boolean isStrong = false;
    private boolean[] scoreArr ;
    public int score = 0;
    public int randomEmoji;
    private boolean isEmojing = false;
    private boolean bindToPipe = false;
    private boolean ignorePipe = false;
    private Emoji mEmoji;
    private int emojiSize = 150;
    private int face = 0;
    public double v_level;

    private ImageView mBackgroundImg;

    public static boolean start = false;

    public Game(Context context, ImageView backgroundImg){
        super(context);
        init();

        mBackgroundImg = backgroundImg;
    }

    public void init(){

        Resources res = getResources();
        Bitmap  originB = BitmapFactory.decodeResource(res, R.drawable.flyingbird);
        Bitmap newB = getResizedBitmap(originB,birdSize,birdSize);

        Bitmap up = getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pipe_up), 100, Resources.getSystem().getDisplayMetrics().heightPixels / 2);

        Bitmap down = getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pipe_down), 100, Resources.getSystem().getDisplayMetrics().heightPixels / 2);

        Bitmap smile = getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.smile),emojiSize,emojiSize);
        Bitmap sad = getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.sad),emojiSize,emojiSize);


        //mBird = new Bird(newB);
        initGif();
        mBird = new Bird(birdArray);
        mPipe1 = new Pipe(down,up,1500,100);
        mPipe2 = new Pipe(down,up,2200,0);
        mPipe3 = new Pipe(down,up,2900,200);
        mPipe4 = new Pipe(down,up,3600,50);
        //mPipe5 = new Pipe(down,up,3500,150);
        //mPipe6 = new Pipe(down,up,4000,250);
        mEmoji = new Emoji(smile,sad,0,0);

        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        scoreArr = new boolean[4];
        for(int i=0;i<scoreArr.length;i++)
            scoreArr[i] = false;

        Random r = new Random();
        randomEmoji = 100 + r.nextInt(200);

        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);


    }

    public void initGif(){
        Resources res = getResources();
        birdArray = new ArrayList<Bitmap>();
        birdArray.add(getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.bird1),birdSize,birdSize));
        birdArray.add(getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.bird2),birdSize,birdSize));
        birdArray.add(getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.bird3),birdSize,birdSize));
        birdArray.add(getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.bird4),birdSize,birdSize));
        birdArray.add(getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.bird5),birdSize,birdSize));
        birdArray.add(getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.bird6),birdSize,birdSize));
        birdArray.add(getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.bird7),birdSize,birdSize));
        birdArray.add(getResizedBitmap(BitmapFactory.decodeResource(res, R.drawable.bird8),birdSize,birdSize));




    }


    public void drawSomething() {

        try {

            mCanvas = mSurfaceHolder.lockCanvas();
           if(mCanvas!=null) {

               mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
               update();

               mBird.draw(mCanvas);
               if (ignorePipe != true) {
                   mPipe1.draw(mCanvas);
                   mPipe2.draw(mCanvas);
                   mPipe3.draw(mCanvas);
                   mPipe4.draw(mCanvas);
               }
               if(isEmojing)
                   mEmoji.draw(mCanvas,face);
               //mPipe5.draw(mCanvas);
               //mPipe6.draw(mCanvas);

               Paint paint=new Paint();
               paint.setStyle(Paint.Style.FILL);
               paint.setStrokeWidth(12);
               paint.setTextSize(100);
               mCanvas.drawText(Integer.toString(score),100,100,paint);

            }

        }catch (Exception e){

        }finally {
            if (mCanvas != null){
                try {
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void run() {

        while (mIsDrawing){
            mCanvas = null;
            long start = System.currentTimeMillis();
            Log.d("jump",new Integer(mBird.posY).toString());
            drawSomething();
            randomEmoji--;
            if(randomEmoji <= 0)
                setEmoji();

            long end = System.currentTimeMillis();

            /*if (end - start < 30) {
                try {
                    Thread.sleep(30 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/


        }


    }

    public void fly(double level){
       isFlying = true;
       counterG = 0;
       counterF = 0;
       v_level=level;
    }

    public void  flyAnimate(){

        mBird.fly(counterG,v_level);

        counterG++;

        if(counterG == 10 || mBird.posY< 0)
            isFlying = false;

    }

    public void fallAnimate(){
        mBird.fall(counterF);
        counterF++;

    }

    public void strongBird(){
        Log.d("so","strong");

    }

    public void setEmoji(){
        //isEmojing = true;
        bindToPipe = true;
        randomEmoji = 20000;
        Random r = new Random();
        face = r.nextInt()%2;

    }



    public void update() {
        if(start) {
            check();
            if(isFlying)
                flyAnimate();
            else
                fallAnimate();
            mPipe1.move();
            mPipe2.move();
            mPipe3.move();
            mPipe4.move();

            if(isEmojing)
                mEmoji.move();

            //mPipe5.move();
            //mPipe6.move();


        }
    }

    public void check(){
        List<Pipe> pipes = new ArrayList<>();

        pipes.add(mPipe1);
        pipes.add(mPipe2);
        pipes.add(mPipe3);
        pipes.add(mPipe4);

        if(mBird.posY + birdSize > screenHeight){
            mIsDrawing = false;
            end();
        }


        for (int i = 0; i < pipes.size(); i++) {

            if(ignorePipe == false) {
                if (mBird.posY < pipes.get(i).posY + (screenHeight / 2) - (gap / 2)) {
                    if (mBird.posX + birdSize > pipes.get(i).posX && mBird.posX < pipes.get(i).posX + 100) {
                        mIsDrawing = false;
                        end();
                    }

                } else if (mBird.posY + birdSize > (screenHeight / 3) + (gap / 2) + pipes.get(i).posY - 100) {
                    if (mBird.posX + birdSize > pipes.get(i).posX && mBird.posX < pipes.get(i).posX + 100) {
                        mIsDrawing = false;
                        end();
                    }

                }


                if (pipes.get(i).posX - 10 < 0) {
                    if (scoreArr[i] == false) {
                        score++;
                        scoreArr[i] = true;
                    }
                }


                if (pipes.get(i).posX + 100 < 0) {
                    Random r = new Random();
                    int value1 = r.nextInt(5)*100;
                    int value2 = r.nextInt(5)*50;

                    if(i == 0)
                        pipes.get(i).posX = pipes.get(pipes.size()-1).posX + value1 + 500;
                    else
                        pipes.get(i).posX =pipes.get(i-1).posX +value1 +500;
                    pipes.get(i).posY = value2;

                    if(bindToPipe == true){
                        bindToPipe = false;
                        isEmojing = true;
                        mEmoji.posX = pipes.get(i).posX-30;
                        mEmoji.posY = (screenHeight / 3) + (gap / 2) + pipes.get(i).posY -350;


                        AlphaAnimation animation1 = new AlphaAnimation(1.0f, 0.1f);
                        animation1.setDuration(2000);
                        animation1.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {}
                            @Override
                            public void onAnimationRepeat(Animation animation) {}
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mBackgroundImg.setVisibility(INVISIBLE);
                                ignorePipe = true;
                            }
                        });
                        mBackgroundImg.startAnimation(animation1);
                    }

                    scoreArr[i] = false;

                }
            }


        }

        if(isEmojing){
            if (mBird.posY < mEmoji.posY + emojiSize && mBird.posY + birdSize > mEmoji.posY ) {
                if (mBird.posX < mEmoji.posX + emojiSize && mBird.posX + birdSize > mEmoji.posX ) {
                    strongBird();
                    isEmojing = false;
                    Random r = new Random();
                    randomEmoji = 100 + r.nextInt(200);

                }

            }

            if(mEmoji.posX + emojiSize <0){
                isEmojing = false;
                Random r = new Random();
                randomEmoji = 100 + r.nextInt(200);

            }
        }
    }


    public void end(){

        //Log.d("?????","finish really?");
        Intent intent = new Intent("kill");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        //Log.d("?????","finish really?");

    }




    public Bitmap getResizedBitmap(Bitmap b, int newWidth, int newHeight) {
        int width = b.getWidth();
        int height = b.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;


        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(b, 0, 0, width, height, matrix, false);
        b.recycle();

        return resizedBitmap;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;

        //new Thread(this).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;

    }
}
