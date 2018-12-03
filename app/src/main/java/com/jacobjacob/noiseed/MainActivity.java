package com.jacobjacob.noiseed;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public float resolution, resx, resy;
    public int seed, range, xvalue, yvalue, iterations, numberchecks, xrender, yrender, xstart, ystart, xstartold, ystartold, xscreen, yscreen, xscale, yscale, xtouch, ytouch, xtouch2, ytouch2, xscaleold, yscaleold;//xrend,yrend,xlimit,ylimit;
    public ImageView Image, Imageold;
    public boolean color, bool2, drawcircle, pickedimage, pickimage, secondtouch,regen;
    private static final int PICK_IMAGE = 100;
    public Bitmap pickedbmp,bmp;
    Uri imageuri, oldimageuri;
    Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        xscreen = size.x;
        yscreen = size.y;
        Image = findViewById(R.id.image);
        Imageold = findViewById(R.id.imageoldbmp);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        regen = true;
        color = false;
        pickedimage = true;
        seed = 713;
        resolution = (float) 0.07;//0.05;
        range = 3; //range 0 = standart
        iterations = 2; //min = 2, 0

        xrender = (int) (resolution * xscreen); //scale when higher precision is needed
        yrender = (int) (resolution * yscreen); //scale when higher precision is needed
        xstart = 0; // top left Corner of rectangle
        ystart = 0; // top left Corner of rectangle
        xscale = xrender / 2;//xrender/3; // size of rectangle
        yscale = yrender / 2;//yrender/3; // size of rectangle

        drawcircle = false;

        pickimage = true;

        resx = resolution * xscreen;
        resy = resolution * yscreen;
        bmp = Bitmap.createBitmap((int) xscreen, (int) yscreen, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bmp);
        pickedbmp = Bitmap.createBitmap(xscreen, yscreen, Bitmap.Config.ARGB_8888);

        opengallery();
        draw();
    }

    public void draw() {
        //color = !color;

        Bitmap newbmp = Bitmap.createBitmap((int) xscreen, (int) yscreen, Bitmap.Config.ARGB_8888);
        Bitmap oldbmp = Bitmap.createBitmap((int) xscreen, (int) yscreen, Bitmap.Config.ARGB_8888);
        //Bitmap bmp2 = Bitmap.createBitmap((int) xscreen, (int) yscreen, Bitmap.Config.ARGB_8888);
        //Bitmap bmp3 = Bitmap.createBitmap((int) xscreen, (int) yscreen, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        if (regen) {
            //Canvas canvas = new Canvas(bmp);
            for (int x = 0; x < (int) (resolution * xscreen); x++) {
                for (int y = 0; y < (int) (resolution * yscreen); y++) {
                    float x1 = (x / (resolution * xscreen));
                    float y1 = (y / (resolution * yscreen));

                    int r;
                    int g;
                    int b;

                    int random = (int) (Math.pow(x1, 3) * Math.pow(y1, 3) * Math.pow((seed + x1 + y1), 2));

                    r = (int) (255 * (float) (Math.floor((random * Math.PI) % 10) / 10));
                    g = (int) (255 * (float) (Math.floor((random * 2 / 3) % 10) / 10));
                    b = (int) (255 * (float) (Math.floor((random * 1 / 4) % 10) / 10));

                    if (!color) {
                        if (r > 255 / 2) {
                            r = 255;
                        } else {
                            r = 0;
                        }
                        g = r;
                        b = r;
                    }

                    paint.setColor(Color.argb(255, r, g, b));
                    float xr = (x + 1) / resolution;
                    float yr = (y + 1) / resolution;
                    float xres = x / resolution;
                    float yres = y / resolution;

                    if (xr < xscreen & yr < yscreen) {
                        canvas.drawRect(xres, yres, xr, yr, paint);
                    }

                }
            }
            //
            if (drawcircle) {
                paint.setColor(Color.rgb(255, 255, 255));                        //draw Circle
                canvas.drawRect(0, 0, xscreen, yscreen, paint);                         //draw Circle
                paint.setColor(Color.rgb(0, 0, 0));                             //draw Circle
                canvas.drawCircle(xscreen / 2, yscreen / 2, xscreen / 2, paint);   //draw Circle
                paint.setColor(Color.rgb(255, 255, 255));                        //draw Circle
                canvas.drawCircle(xscreen / 2, yscreen / 2, xscreen / 3, paint);    //draw Circle
                paint.setColor(Color.rgb(0, 0, 0));                              //draw Circle
                canvas.drawCircle(xscreen / 2, yscreen / 2, xscreen / 4, paint);    //draw Circle
            }
            regen = false;
        }
        /*/
        Image.setImageURI(oldimageuri);
        pickedbmp = Image.getDrawingCache();
        //*/
        //

        //*/
        try {
            Image.setImageURI(oldimageuri);
            pickedbmp = Image.getDrawingCache();
            //Image.setImageBitmap(pickedbmp);
            pickedbmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), oldimageuri);
        } catch (Exception e) {
            System.out.print(e);
        }
        if (pickedimage & pickedbmp != null) { // && pickedbmp != null

            //canvas.setBitmap(pickedbmp);
            bmp = pickedbmp;
            /*/bmp = pickedbmp;
            int newWidth = bmp.getWidth();
            int newHeight = bmp.getHeight();
            if (bmp.getWidth() > xscreen){
                newWidth = xscreen;
            }
            if (bmp.getHeight() > yscreen){
                newHeight = yscreen;
            }
            bmp = Bitmap.createScaledBitmap(pickedbmp, newWidth, newHeight, false);
            /*//*
            Bitmap bmOverlay = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
            Canvas canvas3 = new Canvas(bmOverlay);
            canvas3.drawBitmap(bmp, new Matrix(), null);
            canvas3.drawBitmap(pickedbmp, new Matrix(), null);
            bmp = bmOverlay;//*/
        }
        //Canvas canvas2 = new Canvas(bmp2);
        int redValue = 0;
        int blueValue = 0;
        int greenValue = 0;
        int pixel;

        if (range < 1) {
            range = 1;
        }
        /******************************************************************************************************************/

        if (bmp != null) {
            for (int iterat = 0; iterat < iterations; iterat++) {
                if (iterat == 1) {
                    oldbmp = bmp;
                }
                if (iterat > 1) {
                    oldbmp = newbmp;
                }

                //Canvas oldcanvas = new Canvas(oldbmp);
                Canvas newcanvas = new Canvas(newbmp);

                for (int xr = xstart; xr < xrender; xr++) {      //renders a rectangle at a time
                    for (int yr = ystart; yr < yrender; yr++) {  //renders a rectangle at a time
                        //
                        //float xrend =  (xr/xrender)*resx;
                        //float yrend =  (yr/yrender)*resy;
                        float xrend = (resx * xr) / xrender;
                        float yrend = (resy * yr) / yrender;

                        float xlimit = (resx * (xr + xscale)) / xrender; //((xr+xscale)/(xrender) * (resolution * xscreen))
                        float ylimit = (resy * (yr + yscale)) / yrender; //((yr+yscale)/(yrender) * (resolution * yscreen))
                        //*/
                    /*
                    int xrend =  (int)(resolution * xscreen * (xr/(xrender)));
                    int yrend =  (int)(resolution * yscreen * (yr/(yrender)));
                    int xlimit = (int)(resolution * xscreen * ((xr+1)/xrender));
                    int ylimit = (int)(resolution * yscreen * ((yr+1)/yrender));
                    */
                        for (int a = (int) xrend; a < xlimit; a++) { //loop screenblocks  //(resolution * xscreen)  ///xrend; a < (resolution * xscreen)xlimet; a+
                            for (int b = (int) yrend; b < ylimit; b++) { //(resolution * yscreen)
                                //
                                redValue = 0;
                                greenValue = 0;
                                blueValue = 0;
                                numberchecks = 0;
                                //*/

                                for (int x1 = 0; x1 < range; x1++) {     //loops through rect
                                    xvalue = (int) ((a + (x1 - range / 2)) / resolution);
                                    if (xvalue < 0) {
                                        xvalue = 0;
                                    }
                                    if (xvalue >= oldbmp.getWidth()) {
                                        xvalue = oldbmp.getWidth() - 1;
                                    }

                                    for (int y1 = 0; y1 < range; y1++) { //loops through rect

                                        yvalue = (int) ((b + (y1 - range / 2)) / resolution);
                                        if (yvalue < 0) {
                                            yvalue = 0;
                                        }
                                        if (yvalue >= oldbmp.getHeight()) {
                                            yvalue = oldbmp.getHeight() - 1;
                                        }

                                        pixel = oldbmp.getPixel(xvalue, yvalue);

                                        redValue += Color.red(pixel);
                                        greenValue += Color.green(pixel);
                                        blueValue += Color.blue(pixel);
                                        numberchecks += 1;
                                        //break;
                                    }
                                    //break;
                                }
                                int numberrange = numberchecks;// (range * range); //*range
                                paint.setColor(Color.rgb((int) (redValue / numberrange), (int) (greenValue / numberrange), (int) (blueValue / numberrange)));

                                float ares = a / resolution;
                                float bres = b / resolution;
                                float ar = (a + 1) / resolution;
                                float br = (b + 1) / resolution;

                                if (ar < xscreen & br < yscreen) {
                                    newcanvas.drawRect(ares, bres, ar, br, paint);
                                }
                            }
                        }//y is min
                        break;//*/
                    }//x is min
                    break;//*/
                }
            }
        }
/*******************************************************************************************/
        //
        //

        //*/
        Imageold.setImageBitmap(pickedbmp);
        if (pickedbmp == null){
            Imageold.setImageBitmap(bmp);
        }
         //*/
        Image.setImageBitmap(newbmp);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //
        secondtouch = false;
        if (event.getPointerCount() > 1) {
            //Log.d(DEBUG_TAG,"Multitouch event");
            // The coordinates of the current screen contact, relative to
            // the responding View or Activity.
            secondtouch = true;
            xtouch2 = (int) event.getX(1);
            ytouch2 = (int) event.getY(1);
            //xtouch = (int) event.getX(0);
            //ytouch = (int) event.getY(0);

            xscale = xrender * (xtouch - xtouch2) / (xscreen);//(xtouch - xtouch2)/(xscreen);
            yscale = yrender * (ytouch - ytouch2) / (yscreen);//(ytouch - ytouch2)/(yscreen);
            //
            if (xscale < 0) {
                xscale = -xscale;
            }
            if (yscale < 0) {
                yscale = -yscale;
            }//*/
            //xscaleold = xscale;
            //yscaleold = yscale;
        }
        if (event.getPointerCount() <= 1) {
            //xscale = xscaleold;
            //yscale = yscaleold;

        }
        /*/
        else {
            // Single touch event
            //Log.d(DEBUG_TAG,"Single touch event");
            xtouch = (int)MotionEventCompat.getX(event, index);
            ytouch = (int)MotionEventCompat.getY(event, index);
        }
        //*/
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                seed += 1;
                regen = true;
                color = !color;
                draw();
                break;
            case MotionEvent.ACTION_MOVE:
                //seed += 1;
                //regen = false;
                boolean draw = true;
                xstartold = xstart;
                ystartold = ystart;
                xtouch = (int) event.getX(0);
                ytouch = (int) event.getY(0);

                xstart = (xtouch * xrender / xscreen)- xscale / 2;
                ystart = (ytouch * yrender / yscreen)- yscale / 2;

                if (secondtouch) {
                    if (xtouch < xtouch2) {
                        xstart = (xtouch * xrender / xscreen);//- xscale / 2;
                    } else {
                        xstart = (xtouch2 * xrender / xscreen);
                    }
                    if (ytouch < ytouch2) {
                        ystart = (ytouch * yrender / yscreen);//- yscale / 2;
                    } else {
                        ystart = (ytouch2 * yrender / yscreen);
                    }
                }

                if (xstart == xstartold & ystartold == ystart) {
                    draw = false;
                }
                if (draw) {
                    draw();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void opengallery() {
        pickimage = true;
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageuri = data.getData();
            if (pickimage == true) {
                oldimageuri = imageuri;
                pickimage = false;
            }
            /*/
            Image.setImageURI(imageuri);
            pickedbmp = Image.getDrawingCache();
            //*/

            /*/
            try {
                pickedbmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
            }
            catch (Exception e){
                System.out.print(e);
            }
            //*/
        }
    }
}
