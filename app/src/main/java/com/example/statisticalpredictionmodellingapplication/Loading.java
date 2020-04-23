package com.example.statisticalpredictionmodellingapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.nio.file.InvalidPathException;

public class Loading extends AppCompatActivity {

    private static final int PICKFILE_RESULT_CODE = 8778;

    public String training_set;
    public String test_set;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        View view = new View(this);

        try {
            //ACTION_GET_CONTENT is the intent to choose a file via the system browser.
            Intent intent0 = new Intent( Intent.ACTION_GET_CONTENT );
            //Filter to only show results that can be opened
            intent0.addCategory( Intent.CATEGORY_OPENABLE );
            //Only show files of type .arff
            intent0.setType( "*/*" );
            startActivityForResult( intent0, PICKFILE_RESULT_CODE );

            Intent intent1 = new Intent( Intent.ACTION_GET_CONTENT );
            intent1.addCategory( Intent.CATEGORY_OPENABLE );
            intent1.setType( "*/*" );
            startActivityForResult( intent1, PICKFILE_RESULT_CODE );

            Season season = new Season();
            season.getArff(training_set, test_set);

            Results results = new Results(season);

            startActivity(new Intent(Loading.this, Results.class));
        }
        catch(Exception exe) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("ERROR: Exception!");
            builder.setMessage(exe.getMessage());
        }
    }

    //Default constructor
    public Loading() {/*Unused*/}

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case PICKFILE_RESULT_CODE:
                if(resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String result = uri.getPath();
                    if(result.contains("MATCHES")) {
                        test_set = result;
                    }
                    else {
                        training_set = result;
                    }
                    /*if(result.endsWith(".arff")) {

                    }
                    else {
                        throw new InvalidPathException("INVALID FILE TYPE", "Must choose file of type arff");
                    }*/
                }
        }
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
                    goBack.onCreate(savedInstanceState);
                }
            } );
        }
    }
}
