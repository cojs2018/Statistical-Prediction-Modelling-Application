package com.example.statisticalpredictionmodellingapplication;

import android.content.Intent;

public class arff_file
{
    public void getFile()
    {
        //ACTION_OPEN_DOCUMENT is the intent to choose a file via the system browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        //Filter to only show results that can be opened
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        //Only show files of type .arff
        intent.setType("*.arff");

        //ToDo, add file to application

    }

    public String file_name;
}
