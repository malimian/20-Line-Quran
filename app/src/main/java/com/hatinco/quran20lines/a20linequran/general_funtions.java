package com.hatinco.quran20lines.a20linequran;

import android.content.Context;

/**
 * Created by Fazal  Rehman on 7/27/2018.
 */

public class general_funtions {


    public int return_sura_pageno_index (Context cv , int pageno_){


        for (int i =0 ; i <= cv.getResources().getIntArray(R.array.sura_pages).length-1 ; i++){
            if (cv.getResources().getIntArray(R.array.sura_pages)[i] > pageno_){
                if (i <= 0) return 0;
                else return i-1;

            }
        }

        return cv.getResources().getIntArray(R.array.sura_pages).length-1;
    }

    public int get_juz_index (Context cv , int pageno){

        for (int i =0 ; i <= cv.getResources().getIntArray(R.array.para_index).length-1 ; i++){

            if (cv.getResources().getIntArray(R.array.para_index)[i] > pageno){
                if (i <= 0) return 0;
                else return i-1;            }

        }

        return 29;
    }


}
