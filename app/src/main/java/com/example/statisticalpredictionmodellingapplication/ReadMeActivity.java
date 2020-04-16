package com.example.statisticalpredictionmodellingapplication;

import android.content.Context;
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

public class ReadMeActivity extends AppCompatActivity {

    public ReadMeActivity() { //Default constructor
        this.onCreate(new Bundle());
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
            File file = new File("README.md");
            FileReader fileReader = new FileReader(file);

            String str = new String();

            while(fileReader.read() != -1) {
                char c[] = new char[200];
                fileReader.read(c);
                str.concat(c.toString());
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
                MainActivity goBackToMain = new MainActivity();
            }
        } );
    }
}
