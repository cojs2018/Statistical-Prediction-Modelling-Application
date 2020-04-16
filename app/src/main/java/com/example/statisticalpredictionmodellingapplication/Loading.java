package com.example.statisticalpredictionmodellingapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    //Default constructor
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Loading(String training_set_dir, String test_set_dir){
        //Opens Loading screen and processes data files
        Bundle newSavedInstanceState = new Bundle();
        this.onCreate(newSavedInstanceState);
        View view = new View(this);

        //Add code that computes "arff_to_league(training_set_dir, test_set_dir)"
        Season season = new Season();

        season.getArff(training_set_dir, test_set_dir);

        Results results = new Results(season);
    }

    //Process for progress bar and cancel button
    public class LoadingCancel {
        protected void OnViewCreate(@NonNull View view, @Nullable Bundle savedInstanceState) {
            Button cancel = findViewById(R.id.Cancel);

            cancel.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Return to main activity
                    MainActivity goBack = new MainActivity();
                }
            } );
        }
    }
}
