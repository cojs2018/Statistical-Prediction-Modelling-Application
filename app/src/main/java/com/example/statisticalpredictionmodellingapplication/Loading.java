package com.example.statisticalpredictionmodellingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }


    //ToDo: Process for progress bar and cancel button

    public Loading(String training_set_dir, String test_set_dir){
        //Opens Loading screen and processes data files
        Bundle newSavedInstanceState = new Bundle();
        this.onCreate(newSavedInstanceState);

        //System.loadLibrary("ArffData");

        //ToDo: Add code that computes "arff_to_league(training_set_dir, test_set_dir)"
        call_cpp(training_set_dir, test_set_dir);
    }

    public native void call_cpp(String train, String test);
}
