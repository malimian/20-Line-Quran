package com.hatinco.quran20lines.a20linequran;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    private static final int REQUEST_WRITE_PERMISSION = 1001;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    static Boolean not_open_bottom_bar_frst_time = true;
    static Boolean page_Sound_ = true;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    BottomBar bottomBar;
    PageCurlView ss;
    FloatingActionButton fab;
    static int night_mode = 0;
    Boolean first_time_current_then_sharedpref = true;
    static int  page_open = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        first_time_current_then_sharedpref = true;
        ss = new PageCurlView(MainActivity.this);
        if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("backcolor", 0) != 0 ) ss.modes_clr = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("backcolor", 0);

        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if (extras != null){
                if(extras.getInt("pagenos") == 0){
                    ss.current_page = extras.getInt("pagenos");
                }else
                    ss.current_page = extras.getInt("pagenos")-1;

                page_open = extras.getInt("pagenos");
                    ss.night_mode = extras.getInt("nightmode");
                    night_mode = extras.getInt("nightmode");
                    ss.page_no = 0;
            }

        }

        this.setContentView(R.layout.activity_main);
        appBarLayout = (AppBarLayout)findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Run every time resume is Presed
        not_open_bottom_bar_frst_time = true;

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawer.openDrawer(Gravity.START);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.START);
            }
        });
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomBar.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                fab.setVisibility(View.GONE);
            }
        });



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

         bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (not_open_bottom_bar_frst_time) not_open_bottom_bar_frst_time = false;
                else bottom_bar_fun(tabId , ss);
            }
        });


        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (not_open_bottom_bar_frst_time) not_open_bottom_bar_frst_time = false;
                else bottom_bar_fun(tabId , ss);
            }
        });

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);


        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(
//                        getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(
//                                listDataHeader.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT)
//                        .show();

                finish();
                Intent o = new Intent(MainActivity.this, MainActivity.class);

                if(groupPosition == 0 ) o.putExtra("pagenos", MainActivity.this.getResources().getIntArray(R.array.sura_pages)[childPosition]);
                else o.putExtra("pagenos", MainActivity.this.getResources().getIntArray(R.array.para_index)[childPosition]);
                startActivity(o);
                return false;
            }
        });


        //lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.hide_toolbar) {
            bottomBar.setVisibility(View.GONE);
            return true;
        }

        if (id == R.id.hide_toolbar) {
            bottomBar.setVisibility(View.GONE);
            return true;
        }

        if (id == R.id.hide_topbar) {
            toolbar.setVisibility(View.GONE);
            return true;
        }


        if (id == R.id.bookmark_) {
            bookmark_f();
            return true;
        }


        if (id == R.id.goto_page) {
            goto_page();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_back) {
            drawer.closeDrawer(GravityCompat.START);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    /**
     * Locks the orientation to a specific type.  Possible values are:
     * <ul>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_BEHIND}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_LANDSCAPE}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_NOSENSOR}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_PORTRAIT}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_SENSOR}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_UNSPECIFIED}</li>
     * <li>{@link ActivityInfo#SCREEN_ORIENTATION_USER}</li>
     * </ul>
     * @param orientation
     */
    public void lockOrientation( int orientation ) {
        setRequestedOrientation(orientation);
    }


    public void onDestroy(){
        super.onDestroy();
        System.gc();
        finish();
    }


    @TargetApi(Build.VERSION_CODES.M)
    private Boolean requestPermission() {

        int ExtstorePermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            return false;
        }
        else return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



        }else{
            //handle user denied permission, maybe dialog box to user
        }
    }

    public boolean CanWriteSystem(){
        if (Build.VERSION.SDK_INT < 23) {
            return Settings.System.canWrite(MainActivity.this);
        }
        else return false;
    }

    public void bottom_bar_fun(int tabId , final PageCurlView ss ){

        if (tabId == R.id.bottom_bar_night_mode) {
            if(requestPermission()){
                if((CanWriteSystem()))
                {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),
                            "Please Turn System Permission On"
                            ,
                            Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.screen_brightness, null);
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.setTitle("SET BRIGHTNESS");
                    final AlertDialog b = dialogBuilder.create();
                    b.show();


                    final SeekBar mSeekBar = (SeekBar) dialogView.findViewById(R.id.seekBar1);
                    Button cancle = (Button) dialogView.findViewById(R.id.cancle);
                    mSeekBar.setMax(255);


                    // Set the SeekBar initial progress from screen current brightness
                    final WindowManager.LayoutParams lp = getWindow().getAttributes();

                    float curBrightnessValue = 0;

                    try {
                        curBrightnessValue = android.provider.Settings.System.getInt(
                                getContentResolver(),
                                android.provider.Settings.System.SCREEN_BRIGHTNESS);
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }

                    int screen_brightness = (int) curBrightnessValue;

                    mSeekBar.setProgress(screen_brightness);

                    mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        int progress = 0;
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            // Change the screen brightness
                            float brightness = i / (float)255;
                            lp.screenBrightness = brightness;
                            getWindow().setAttributes(lp);
                            progress = i;
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {}

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            android.provider.Settings.System.putInt(getContentResolver(),
                                    android.provider.Settings.System.SCREEN_BRIGHTNESS,
                                    progress);

                        }
                    });
                    cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            b.dismiss();
                        }
                    });
                }
            }
        }

        if (tabId == R.id.bottom_bar_night_ic_share) {
            // The tab with id R.id.tab_favorites was selected,
            // change your content accordingly.

          //  ss.modes_clr = getRandomColor();
            if(requestPermission()) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                 intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), getBitmapFromAsset(MainActivity.this, "" + (page_open +(ss.page_no)) + ".jpg"), "title", null)));
                startActivity(Intent.createChooser(intent, "Share This Page"));
            }
        }

        if (tabId == R.id.bottom_bar_night_sound) {
            page_Sound_ = ! page_Sound_;
            ss.page_sound = page_Sound_;

            Snackbar snackbar;

            if(page_Sound_){
                 snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Page Sound ON", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.GREEN);
                snackbar.show();
            }

            else{
                 snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Page Sound OFF", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.RED);
            }

            snackbar.show();
        }


        if (tabId == R.id.bottom_bar_show_bar) {

                toolbar.setVisibility(View.GONE);
                bottomBar.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);

                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "ToolBar Hidden", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.RED);
                snackbar.show();

        }


        if (tabId == R.id.bottom_bar_starbutton) {bookmark_f();}



    }


    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Surah Index");
        listDataHeader.add("Juz Index");

        listDataChild.put(listDataHeader.get(0), Arrays.asList(getResources().getStringArray(R.array.sura_names))); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Arrays.asList(getResources().getStringArray(R.array.para_name)));
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    public void bookmark_f (){
        general_funtions gf_  = new general_funtions();
        final blc db_obj = new blc(this);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.bookmarks, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(true);
        b.show();
        TextView surat_name_book = (TextView) dialogView.findViewById(R.id.surat_name_book);
        TextView surat_name_translation_book = (TextView) dialogView.findViewById(R.id.surat_name_translation_book);
        TextView pageno_page = (TextView) dialogView.findViewById(R.id.pageno_page);
        TextView juzindex = (TextView) dialogView.findViewById(R.id.pageno_juz);
        final EditText booktitle = (EditText) dialogView.findViewById(R.id.title_book);
        Button save_ = (Button) dialogView.findViewById(R.id.save_);
        Button cancle = (Button) dialogView.findViewById(R.id.cancle);

        final int temp_pages = page_open +(ss.page_no) ;

        surat_name_book.setText( "Surah "+String.valueOf(this.getResources().getStringArray(R.array.sura_names_eng)[gf_.return_sura_pageno_index(this , (temp_pages))]+" - "+this.getResources().getStringArray(R.array.sura_names)[gf_.return_sura_pageno_index(this , (temp_pages))]) );
        surat_name_translation_book.setText( String.valueOf(this.getResources().getStringArray(R.array.sura_names_translation)[gf_.return_sura_pageno_index(this , (temp_pages))]) );
        pageno_page.setText("Page #"+(temp_pages) );
        juzindex.setText("Juz : "+String.valueOf(this.getResources().getStringArray(R.array.para_name)[gf_.get_juz_index(this , (temp_pages))]));


        save_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    db_obj.insert_in_bookmarks(page_open , booktitle.getText().toString());
                    b.dismiss();

                    Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            String.valueOf("Page No# "+temp_pages+" Bookmarked"),
                            Snackbar.LENGTH_SHORT)
                            .setAction("Action", null);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.GREEN);
                    snackbar.show();

                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(),
                            e.toString() ,
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

    }


    public void goto_page(){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.gotopage_dialouge, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(true);
        b.show();
        final EditText pageno_togo = (EditText) dialogView.findViewById(R.id.pageno_togo);
        final Button go = (Button) dialogView.findViewById(R.id.go);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pageno_togo.getText().toString().equals("")) pageno_togo.setText("0");

                if(Integer.parseInt(pageno_togo.getText().toString()) > 303 || Integer.parseInt(pageno_togo.getText().toString()) < 0){
                    Toast.makeText(getApplicationContext(),
                            "Invalid Page No# "+pageno_togo.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent o = new Intent(MainActivity.this, MainActivity.class);
                o.putExtra("pagenos", Integer.parseInt(pageno_togo.getText().toString()));
                if (night_mode == 1) o.putExtra("nightmode" , 1);
                else            o.putExtra("nightmode" , 0);
                startActivity(o);
                b.dismiss();
                finish();
            }
        });

    }
}
