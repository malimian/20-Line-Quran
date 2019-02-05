package com.hatinco.quran20lines.a20linequran;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

import static java.security.AccessController.getContext;

public class change_backcolor extends AppCompatActivity {

    private int seekR, seekG, seekB;
    SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    LinearLayout mScreen;
    Button b1;
    Button b2;
    ProgressBar progress_bar;
    static int a = 0;
    ImageView page_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_backcolor);

        mScreen = (LinearLayout) findViewById(R.id.myScreen);
        b1 = (Button) findViewById(R.id.apply);
        b2 = (Button) findViewById(R.id.reset);

        redSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_R);
        greenSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_G);
        blueSeekBar = (SeekBar) findViewById(R.id.mySeekingBar_B);
        page_ = (ImageView) findViewById(R.id.page);

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);

        redSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        greenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        blueSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.getDefaultSharedPreferences(
                        getApplicationContext()).edit().putInt(
                                "backcolor", a
                ).apply();

                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Page Overlay Color Set Successful", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.GREEN);
                snackbar.show();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.getDefaultSharedPreferences(
                        getApplicationContext()).edit().putInt(
                        "backcolor", 0
                ).apply();

                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Page Overlay Reset to default", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.RED);
                snackbar.show();

            }
        });

    }


    private SeekBar.OnSeekBarChangeListener seekBarChangeListener
            = new SeekBar.OnSeekBarChangeListener()
    {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
// TODO Auto-generated method stub
            if(progress != 0)
            updateBackground();
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
// TODO Auto-generated method stub
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
// TODO Auto-generated method stub
        }
    };

    private void updateBackground()
    {
        seekR = redSeekBar.getProgress();
        seekG = greenSeekBar.getProgress();
        seekB = blueSeekBar.getProgress();
         a = 0xff000000
                + seekR * 0x10000
                + seekG * 0x100
                + seekB;
        mScreen.setBackgroundColor(a);

        new changeoverlay().execute();
    }


    public Bitmap ColourBitmap(Bitmap img, int clr){
        img = reduseimage(img);
        Bitmap rBitmap = img.copy(Bitmap.Config.ARGB_8888, true);
        Paint paint=new Paint();
        ColorFilter filter = new PorterDuffColorFilter(clr, PorterDuff.Mode.OVERLAY);
        paint.setColorFilter(filter);
        Canvas myCanvas =new Canvas(rBitmap);
        myCanvas.drawBitmap(rBitmap, 0, 0, paint);

        return rBitmap;
    }


    private Bitmap reduseimage(Bitmap f ) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            f.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
            return (BitmapFactory.decodeStream(bs, null, options));

        } catch (Exception e) {

        }
        return null;
    }


    private class changeoverlay extends AsyncTask<Void, Void, Bitmap> {


        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progress_bar.setVisibility(View.VISIBLE);
            page_.setVisibility(View.GONE);
        }


        @Override
        protected Bitmap doInBackground(Void... voids) {
            return    ColourBitmap(PageCurlView.getBitmapFromAsset(getBaseContext(),""+(5)+".jpg") ,  a );
        }

        protected void onPostExecute(Bitmap result) {
            progress_bar.setVisibility(View.GONE);
            page_.setVisibility(View.VISIBLE);
            page_.setImageBitmap( result );
        }


    }

}
