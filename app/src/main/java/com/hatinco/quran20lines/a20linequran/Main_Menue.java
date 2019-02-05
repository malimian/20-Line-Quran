package com.hatinco.quran20lines.a20linequran;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Main_Menue extends AppCompatActivity implements View.OnClickListener{

    private Button resumeReading;
    private Button suraindex;
    private Button juzindex;
    private Switch nightmode;
    private Button pagecolor;
    private Button bookmarks;
    private Button aboutUs;

    static Boolean night_mode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menue);


        resumeReading = (Button)findViewById( R.id.resume_reading );
        suraindex = (Button)findViewById( R.id.suraindex );
        juzindex = (Button)findViewById( R.id.juzindex );
        nightmode = (Switch)findViewById( R.id.nightmode );
        pagecolor = (Button)findViewById( R.id.pagecolor );
        bookmarks = (Button)findViewById( R.id.bookmarks );
        aboutUs = (Button)findViewById( R.id.about_us );

        resumeReading.setOnClickListener( this );
        suraindex.setOnClickListener( this );
        juzindex.setOnClickListener( this );
        pagecolor.setOnClickListener( this );
        bookmarks.setOnClickListener( this );
        aboutUs.setOnClickListener( this );getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);

        nightmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                Snackbar snackbar;
                if (isChecked){

                    night_mode = true;

                    snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Night Mode ON", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.GREEN);
                    snackbar.show();
                }
                else
                {
                    night_mode = false;

                    snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Night Mode OFF", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.RED);
                    snackbar.show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if ( v == resumeReading ) {
            // Handle clicks for resumeReading
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Main_Menue.this);
            Intent o = new Intent(Main_Menue.this , MainActivity.class);
            int pageno = prefs.getInt("pageno", 0);
            o.putExtra("pagenos",pageno);
            if (night_mode) o.putExtra("nightmode" , 1);
            else            o.putExtra("nightmode" , 0);
            startActivity(o);
        } else if ( v == suraindex ) {
            // Handle clicks for suraindex
            Intent o = new Intent(Main_Menue.this , juzActivity.class);
            o.putExtra("showsurat",true);
            if (night_mode) o.putExtra("nightmode" , 1);
            else            o.putExtra("nightmode" , 0);
            startActivity(o);
        } else if ( v == juzindex ) {
            // Handle clicks for juzindex
            Intent o = new Intent(Main_Menue.this , juzActivity.class);
            if (night_mode) o.putExtra("nightmode" , 1);
            else            o.putExtra("nightmode" , 0);
            startActivity(o);
        } else if ( v == pagecolor ) {
            // Handle clicks for pagecolor
            Intent i = new Intent(Main_Menue.this , change_backcolor.class);
            startActivity(i);
        } else if ( v == bookmarks ) {
            // Handle clicks for bookmarks
            Intent o = new Intent(Main_Menue.this , Bookmarks.class);
            if (night_mode) o.putExtra("nightmode" , 1);
            else            o.putExtra("nightmode" , 0);
            startActivity(o);
        } else if ( v == aboutUs ) {
            // Handle clicks for aboutUs
            Intent o = new Intent(Main_Menue.this , about_us.class);
            startActivity(o);
        }
    }
}
