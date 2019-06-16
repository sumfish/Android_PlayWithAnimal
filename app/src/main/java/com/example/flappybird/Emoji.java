package com.example.flappybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Emoji {

    private Bitmap smile,sad;
    public int posX,posY;
    private int screenHeight;
    private int velocityX = 10;

    public Emoji(Bitmap b1, Bitmap b2,int x, int y){

        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        smile = b1;
        sad = b2;
        posX = x;
        posY = y;

    }

    public void draw(Canvas canvas,int i) {
        if(i == 0)
            canvas.drawBitmap(smile, posX, posY, null);
        else
            canvas.drawBitmap(sad, posX, posY, null);
    }

    public void move(){
        posX -= velocityX;
    }

}
