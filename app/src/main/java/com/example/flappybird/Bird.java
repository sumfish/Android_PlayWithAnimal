package com.example.flappybird;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Movie;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;

public class Bird {

    public int posX,posY;
    private Bitmap image;
    private ArrayList<Bitmap>  bitmapArray;
    public int velocityY = 10;
    public  double G = 0.3;

    private int pic;


    public Bird (ArrayList<Bitmap> birdArray) {


        posX = 100;
        posY = 100;
        bitmapArray = birdArray;
        pic = 0;

        image = bitmapArray.get(pic);


    }

    public void setGif(){
        if(pic>=7)
            pic = 0;
        else
            pic++;

        image = bitmapArray.get(pic);

    }

    public void setSize(int birdsize){
        for(int i=0;i<bitmapArray.size();i++){
            bitmapArray.set(i, getResizedBitmap(bitmapArray.get(i),birdsize,birdsize));
        }
    }
    public void draw(Canvas canvas) {
        setGif();
        canvas.drawBitmap(image, posX, posY, null);
    }

    public void fall(int conterF){

        posY += conterF * G;
    }
    public void fly(int conterG){
        posY -= Math.round(velocityY  - conterG * G);
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



}
