package com.example.statisticalpredictionmodellingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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
        start_new.setOnClickListener( v -> {
            startActivity( new Intent( MainActivity.this, Display_Results.class ) );
        } );

        final Button createORupdate = findViewById(R.id.createAndupdte);
        createORupdate.setOnClickListener( v -> {
            startActivity( new Intent( MainActivity.this, AddLeagueData.class ) );
        } );

        final Button read_me = findViewById(R.id.readme);
        read_me.setOnClickListener( v -> {
            //Display read_me.txt on screen
            ReadMeActivity readme = new ReadMeActivity();
            startActivity(new Intent(MainActivity.this, ReadMeActivity.class));
        } );
    }
}

