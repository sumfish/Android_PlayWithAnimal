package com.example.flappybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Emoji {

    private Bitmap img;
    public int posX,posY;
    private int screenHeight;
    private int velocityX = 15;
    public int face = 0; //1 = smile
    public boolean disappear = false;

    public Emoji(Bitmap b1,int x, int y,int f){

        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        img = b1;
        face = f;
        posX = x;
        posY = y;

    }

    public void draw(Canvas canvas) {

        canvas.drawBitmap(img, posX, posY, null);

    }

    public void move(){
        posX -= velocityX;
    }

}
