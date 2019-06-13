package com.example.flappybird;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private Bird mBird;
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Bitmap background;
    private boolean mIsDrawing;
    private int FPS = 30;
    private Pipe mPipe1,mPipe2,mPipe3,mPipe4,mPipe5,mPipe6;
    private int screenHeight;
    private int screenWidth;
    private int gap = 600;
    private int birdSize = 100;

    //public static Button startButton;

    public static boolean start = false;




    public Game(Context context,AttributeSet attrs){
        super(context,attrs);
        init();

    }




    public void init(){

        Resources res = getResources();
        Bitmap  originB = BitmapFactory.decodeResource(res, R.drawable.flyingbird);
        Bitmap newB = getResizedBitmap(originB,birdSize,birdSize);

        Bitmap up = getResizedBitmap(BitmapFactory.decodeResource
                (getResources(), R.drawable.pipe_up), 100, Resources.getSystem().getDisplayMetrics().heightPixels / 2);

        Bitmap down = getResizedBitmap(BitmapFactory.decodeResource
                (getResources(), R.drawable.pipe_down), 100, Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        ;
        mBird = new Bird(newB);
        mPipe1 = new Pipe(down,up,1500,100);
        mPipe2 = new Pipe(down,up,2200,0);
        mPipe3 = new Pipe(down,up,2900,200);
        mPipe4 = new Pipe(down,up,3600,50);
        //mPipe5 = new Pipe(down,up,3500,150);
        //mPipe6 = new Pipe(down,up,4000,250);

        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);


        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);

    }


    private void drawSomething() {

        try {

            mCanvas = mSurfaceHolder.lockCanvas();
           if(mCanvas!=null) {
               //mCanvas.drawRGB(0, 100, 205);
               mCanvas.drawBitmap(background,0f,0f,null);
               update();
               mBird.draw(mCanvas);
               mPipe1.draw(mCanvas);
               mPipe2.draw(mCanvas);
               mPipe3.draw(mCanvas);
               mPipe4.draw(mCanvas);
               //mPipe5.draw(mCanvas);
               //mPipe6.draw(mCanvas);
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

            drawSomething();

            long end = System.currentTimeMillis();

            if (end - start < 30) {
                try {
                    Thread.sleep(30 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(start) {
            if (mBird.posY > -25)
                mBird.posY -= mBird.velocityY * 10;
            String y = Integer.toString(mBird.posY);
            Log.d("QQQQQQ", "touch");
            Log.d("bird", y);
        }
        return super.onTouchEvent(event);
    }


    public void update() {
        if(start) {
            check();
            mBird.fall();
            mPipe1.move();
            mPipe2.move();
            mPipe3.move();
            mPipe4.move();
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

        for (int i = 0; i < pipes.size(); i++) {

            if (mBird.posY < pipes.get(i).posY + (screenHeight / 2) - (gap / 2)  ) {
                if( mBird.posX + birdSize > pipes.get(i).posX && mBird.posX < pipes.get(i).posX + 100)
                    reset();
            } else if (mBird.posY+ birdSize > (screenHeight / 3) + (gap / 2) + pipes.get(i).posY- 100 ) {
                if( mBird.posX + birdSize > pipes.get(i).posX && mBird.posX < pipes.get(i).posX + 100)
                    reset();
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
            }
        }
    }


    public void reset(){
        /*start = false;
        startButton.setText("restart");
        startButton.setVisibility(View.VISIBLE);*/


        mBird.posY = 100;

        mPipe1.posX = 1500;
        mPipe1.posY= 100;

        mPipe2.posX = 2200;
        mPipe2.posY = 0;

        mPipe3.posX = 2600;
        mPipe3.posY = 200;

        mPipe4.posX = 3000;
        mPipe4.posY= 50;

        //mPipe5.posX = 2000;
        //mPipe5.posY = 150;

        //mPipe6.posX = 2500;
        //mPipe6.posY = 250;
    }

    public Bitmap getResizedBitmap(Bitmap b, int newWidth, int newHeight) {
        int width = b.getWidth();
        int height = b.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION

        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap =
                Bitmap.createBitmap(b, 0, 0, width, height, matrix, false);
        b.recycle();
        return resizedBitmap;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;

        new Thread(this).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;

    }
}
