package com.example.flappybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Pipe {
    private Bitmap pipeUp,pipeDown;
    public int posX,posY;

    private int screenHeight;
    private int  velocityX = 10;
    private int gap = 600;

    public Pipe(Bitmap b1,Bitmap b2, int x, int y){

        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        pipeDown = b1;
        pipeUp = b2;
        posX = x;
        posY = y;

    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(pipeDown,posX, -(gap / 2) + posY, null);
        canvas.drawBitmap(pipeUp,posX, ((screenHeight / 3) + (gap / 2)+posY -100), null);


    }
    public void move(){
        posX -= velocityX;
    }

}
