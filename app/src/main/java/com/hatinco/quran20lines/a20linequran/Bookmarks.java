package com.hatinco.quran20lines.a20linequran;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Bookmarks extends AppCompatActivity {

    ListView ls;
    int nightmode = 0;
    int pageno_ = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ls = (ListView) findViewById(R.id.booklist);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                custom_bookmark();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if (extras != null){
               nightmode = extras.getInt("nightmode");
            }
        }

        new show_bookmarks().execute();

    }

    private class show_bookmarks extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;
        ListAdapter adapter;
        blc ss = new blc(Bookmarks.this);

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Bookmarks.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();
        }



        @Override
        protected Void doInBackground(Void... voids) {

            try {

                adapter = new SimpleAdapter(
                        Bookmarks.this, ss.bookmarks_list(),
                        R.layout.row_bookmark, new String[]
                        {       "title",
                                "pageno"
                        }, new int[]{
                        R.id.title,
                        R.id.pageno
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

            ls.setAdapter(adapter);

            ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent o = new Intent(Bookmarks.this , MainActivity.class);
                    o.putExtra("pagenos", Integer.parseInt(ss.bookmarks_list().get(i).get("pageno")));
                    o.putExtra("nightmode",nightmode);
                    startActivity(o);
                }
            });
        }


    }



    public void custom_bookmark(){

        general_funtions gf_  = new general_funtions();
        final blc db_obj = new blc(this);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Bookmarks.this);
        LayoutInflater inflater = Bookmarks.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_bookmark_dialouge, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        b.show();

        final EditText title_book =(EditText) dialogView.findViewById(R.id.title_book);
        final EditText pageno_page =(EditText) dialogView.findViewById(R.id.pageno_page);
        final Spinner sp_surat_or_juz =(Spinner) dialogView.findViewById(R.id.sp_surat_or_juz);
        final Button save_ = (Button) dialogView.findViewById(R.id.save_);
        Button cancle = (Button) dialogView.findViewById(R.id.cancle);

        RadioGroup choose_group =(RadioGroup) dialogView.findViewById(R.id.radioGroup);
        final int selectedId = choose_group.getCheckedRadioButtonId();

        choose_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {


                if(i == R.id.choose_juz){
                    pageno_page.setVisibility(View.GONE);
                    sp_surat_or_juz.setVisibility(View.VISIBLE);
                    title_book.setText("");

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Bookmarks.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.para_name));
                    dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
                    sp_surat_or_juz.setAdapter(dataAdapter);

                    sp_surat_or_juz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            pageno_ = getResources().getIntArray(R.array.para_index)[i];
                            String item = adapterView.getItemAtPosition(i).toString();
                            title_book.setText(String.valueOf(item));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });



                    save_.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            try {
                                db_obj.insert_in_bookmarks(pageno_,title_book.getText().toString());

                                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        String.valueOf("Page No# "+pageno_+" Bookmarked"),
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


                }


                if(i == R.id.choose_page){
                    pageno_page.setVisibility(View.VISIBLE);
                    sp_surat_or_juz.setVisibility(View.GONE);
                    title_book.setText("");



                    save_.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            try {

                                if(Integer.parseInt(pageno_page.getText().toString()) > 303 || Integer.parseInt(pageno_page.getText().toString()) < 1){
                                    Toast.makeText(getApplicationContext(),
                                            "Invalid Page No# "+pageno_page.getText().toString(),
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                db_obj.insert_in_bookmarks(Integer.parseInt(pageno_page.getText().toString()),title_book.getText().toString());

                                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        String.valueOf("Page No# "+pageno_page.getText().toString()+" Bookmarked"),
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

                }


                if(i == R.id.choose_surat){
                    pageno_page.setVisibility(View.GONE);
                    sp_surat_or_juz.setVisibility(View.VISIBLE);
                    title_book.setText("");


                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Bookmarks.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sura_names));
                    dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
                    sp_surat_or_juz.setAdapter(dataAdapter);


                    sp_surat_or_juz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            pageno_ = getResources().getIntArray(R.array.sura_pages)[i];
                            String item = adapterView.getItemAtPosition(i).toString();
                            title_book.setText(String.valueOf(item));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    save_.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            try {
                                db_obj.insert_in_bookmarks(pageno_,title_book.getText().toString());

                                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        String.valueOf("Page No# "+pageno_+" Bookmarked"),
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
                }


            }
        });



        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                new show_bookmarks().execute();
            }
        });


    }


}
