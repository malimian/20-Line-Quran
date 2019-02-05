package com.hatinco.quran20lines.a20linequran;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Fazal  Rehman on 7/23/2018.
 */

public class juzActivity extends AppCompatActivity {
    private ListView show_juz;
    ListAdapter adapter;
    int nightmode = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juz_surah);

        show_juz  = (ListView) findViewById(R.id.pagenumber);


        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if (extras != null){

                if (extras.getBoolean("showsurat"))
                new show_surat().execute();
                else new show_juz().execute();
                nightmode = extras.getInt("nightmode");
            }
            else{
                new show_juz().execute();
            }

        }

    }


    private class show_surat extends AsyncTask<Void, Void, Void> {

       private ProgressDialog pDialog ;

        ListAdapter adapter;

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(juzActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();

        }


        @Override
        protected Void doInBackground(Void... voids) {


            try {
                ArrayList<HashMap<String,String>> sura_pages =new ArrayList<>();

                for (int i=0;i<getResources().getIntArray(R.array.sura_pages).length;i++)
                {
                    HashMap<String,String> hashMap=new HashMap<>();//create a hashmap to store the data in key value pair
                    hashMap.put("sura_names",getResources().getStringArray(R.array.sura_names)[i]);
                    hashMap.put("sura_pages",String.valueOf(getResources().getIntArray(R.array.sura_pages)[i]));
                    hashMap.put("lbsuratranslate",getResources().getStringArray(R.array.sura_names_translation)[i]);
                    hashMap.put("lbsuratno",String.valueOf(i+1));
                    sura_pages.add(hashMap);

                }

                adapter = new SimpleAdapter(
                        juzActivity.this, sura_pages,
                        R.layout.list_item, new String[]
                        {
                                "lbsuratno",
                                "sura_names",
                                "lbsuratranslate",
                                "sura_pages"
                        }, new int[]{
                        R.id.lbsuratno,
                        R.id.lblListItem,
                        R.id.lbsuratranslate,
                        R.id.sura_page_no
                }
                );

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            pDialog.cancel();

            show_juz.setAdapter(adapter);

            show_juz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent o = new Intent(juzActivity.this , MainActivity.class);
                    o.putExtra("pagenos",juzActivity.this.getResources().getIntArray(R.array.sura_pages)[i]);
                    o.putExtra("nightmode",nightmode);
                    startActivity(o);
                }
            });

        }


    }


    private class show_juz extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog = new ProgressDialog(juzActivity.this);
        ListAdapter adapter;

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {


            try {
                ArrayList<HashMap<String,String>> sura_pages =new ArrayList<>();

                for (int i=0;i<getResources().getIntArray(R.array.para_index).length;i++)
                {
                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put("sura_names",getResources().getStringArray(R.array.para_name)[i]);
                    hashMap.put("sura_pages",String.valueOf(getResources().getIntArray(R.array.para_index)[i]));
                    hashMap.put("lbsuratranslate",  "Juz '"+(i+1));
                    hashMap.put("lbsuratno",        String.valueOf(i+1));
                    sura_pages.add(hashMap);

                }

                adapter = new SimpleAdapter(
                        juzActivity.this, sura_pages,
                        R.layout.list_item, new String[]
                        {
                                "lbsuratno",
                                "sura_names",
                                "lbsuratranslate",
                                "sura_pages"
                        }, new int[]{
                        R.id.lbsuratno,
                        R.id.lblListItem,
                        R.id.lbsuratranslate,
                        R.id.sura_page_no
                }
                );

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            pDialog.cancel();
            show_juz.setAdapter(adapter);

            show_juz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent o = new Intent(juzActivity.this , MainActivity.class);
                    o.putExtra("pagenos",juzActivity.this.getResources().getIntArray(R.array.para_index)[i]);
                    o.putExtra("nightmode",nightmode);
                    startActivity(o);
                }
            });
        }


    }

}
