package com.example.statisticalpredictionmodellingapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadMeActivity extends AppCompatActivity {

    public ReadMeActivity() { //Default constructor

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_read_me );

        View view = new View(this);

        this.onViewCreate(view, savedInstanceState);
    }

    public void onViewCreate(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView textView = view.findViewById(R.id.textView2);

        try {
            AssetManager am = this.getAssets();

            InputStream ins = am.open("README.md");
            InputStreamReader inputStreamReader = new InputStreamReader(ins);

            String str = "";
            while(inputStreamReader.read() != -1) {
                char c = (char) inputStreamReader.read();
                str.concat( String.valueOf( c ) );
            }

            textView.setText(str);
            textView.setScroller(new Scroller(view.getContext()));
        }
        catch(Exception exe){
            Context context = view.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("ERROR: Exception!");
            builder.setMessage(exe.getMessage());
        }

        final FloatingActionButton backButton = view.findViewById(R.id.goBack1);
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Go back to main menu
                startActivity(new Intent(ReadMeActivity.this, MainActivity.class));
            }
        } );
    }
}
