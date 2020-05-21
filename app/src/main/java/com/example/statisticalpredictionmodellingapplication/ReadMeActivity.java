package com.example.statisticalpredictionmodellingapplication;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static android.provider.CalendarContract.CalendarCache.URI;

public class ReadMeActivity extends AppCompatActivity {

    public ReadMeActivity() { //Default constructor

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_read_me );

        TextView textView = findViewById(R.id.display_read);

        try {
            String str = "# Statistical Prediction Modelling Application\n" +
                    "\n" +
                    "The Statistical Prediction Modelling Application is an Android application project that takes user input on football league data and uses this to predict the results of the league, using linear programming methods. Results will then be displayed in the application. The user can then choose to see the data in either table, graph or display the full results.\n" +
                    "\n" +
                    "## Installation\n" +
                    "\n" +
                    "To install, clone from GitHub and run on Android Studio.\n" +
                    "\n" +
                    "## Usage\n" +
                    "\n" +
                    "Upon loading the app, press \"Create or update league data\" to manage input data into the database. To create a new league, press create new on the screen. Add a team by staying on the \"league\" tab and press \"+\" or add a match by going to the \"match\" tab and press \"+\". Press \"x\" to delete a selected row. Press the save icon to save data to the database. The data will then stored with a unique time_stamp code.\n" +
                    "\n" +
                    "To generate and view results, press \"Start new data visualization\". Afterwards, use the tabs to view the results in different formats.\n" +
                    "\n" +
                    "## Contributions\n" +
                    "\n" +
                    "Pull requests are welcome, For major changes, please open an issue first and discuss what you would like to change.\n" +
                    "\n" +
                    "Please make sure to change requests as appropriate.\n";

            textView.setText(str);

            textView.setOnClickListener( v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, URI.parse("https://github.com/cojs2018/Statistical-Prediction-Modelling-Application"));
                startActivity(browserIntent);
            } );
        }
        catch(Exception exe){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ERROR: Exception!");
            builder.setMessage(exe.getMessage());
            builder.show();
        }
    }
}
