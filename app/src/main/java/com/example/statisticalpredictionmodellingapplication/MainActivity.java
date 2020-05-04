package com.example.statisticalpredictionmodellingapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public MainActivity() {
        // Default constructor
        //Bundle thisInstanceState = new Bundle();
        //this.onCreate(thisInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button start_new = findViewById(R.id.startNew);
        start_new.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Display_Results.class));
            }
        } );

        final Button read_me = findViewById(R.id.readme);
        read_me.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display read_me.txt on screen
                ReadMeActivity readme = new ReadMeActivity();
                startActivity(new Intent(MainActivity.this, ReadMeActivity.class));
            }
        } );
    }
}

