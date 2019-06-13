package com.example.flappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bird {

    public int posX,posY;
    private Bitmap image;
    public int velocityY = 5;

    public Bird (Bitmap b) {

        posX = 100;
        posY = 100;

        image = b;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, posX, posY, null);
    }

    public void fall(){

        posY += velocityY;
    }
}
