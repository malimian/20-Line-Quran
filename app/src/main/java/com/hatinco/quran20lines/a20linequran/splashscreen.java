package com.hatinco.quran20lines.a20linequran;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Fazal  Rehman on 7/29/2018.
 */

public class splashscreen extends Activity {

    private Timer timer;
    private ProgressBar progressBar;
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen_main);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(0);

        final long period = 20;
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //this repeats every 100 ms
                if (i<100){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            blc bb = new blc (getApplicationContext());

                        }
                    });
                    progressBar.setProgress(i);
                    i++;
                }else{
                    //closing the timer
                    timer.cancel();
                    Intent intent =new Intent(splashscreen.this,Main_Menue.class);
                    startActivity(intent);
                    // close this activity
                    finish();
                }
            }
        }, 0, period);



    }
}
