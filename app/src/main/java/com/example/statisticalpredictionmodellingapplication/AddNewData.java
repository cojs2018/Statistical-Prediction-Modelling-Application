package com.example.statisticalpredictionmodellingapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TableLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class AddNewData extends AppCompatActivity {

    TableLayout data_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_new_data );

        FloatingActionButton add_button = findViewById(R.id.fAAdd);
        FloatingActionButton delete_button = findViewById(R.id.fADelete);
        FloatingActionButton save_button = findViewById(R.id.fASave);

        this.data_table = findViewById(R.id.tableLayout);

        add_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow();
            }
        } );

        delete_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRow();
            }
        } );

        save_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTable();
            }
        } );
    }

    protected void addRow() {
        //Adds new row to data table
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add team");

    }

    protected void deleteRow() {
        //Deletes row from data table

    }

    protected void saveTable() {
        //Saves table to database

    }
}