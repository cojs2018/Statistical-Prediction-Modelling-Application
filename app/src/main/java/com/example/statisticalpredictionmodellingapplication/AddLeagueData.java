package com.example.statisticalpredictionmodellingapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.statisticalpredictionmodellingapplication.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Date;
import java.util.Vector;
import java.util.regex.Pattern;

import kotlin.jvm.functions.Function3;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AddLeagueData extends AppCompatActivity {

    TableLayout data_table;
    TableLayout schedule;

    String time_stamp;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_league_data );

        /*Start dialog to choose either to update existing table pair or create new pair of date tables.
         * 1. Query database for a list of data tables. */
        SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("LeagueData.db", MODE_PRIVATE, null);
        Cursor tablesCursor = sqLiteDatabase.rawQuery("SELECT t.*, ROWID FROM sqlite_master t LIMIT 501;", null);

        //2. Call new alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update data set");

        //3. If cursor has return a list of items, show these as items on screen, else say there are no items available
        if(tablesCursor.moveToFirst()) {
            Vector<String> tableList = new Vector<>();
            while(tablesCursor.moveToNext()) {
                String tablename = tablesCursor.getString(1);
                if(tablename.contains("LEAGUE"))
                    tableList.add(tablename.substring(7));
            }

            builder.setItems( tableList.toArray( new CharSequence[tableList.size()]), (dialog, which) -> updateTable(tableList.elementAt(which)) );
        }
        else {
            CharSequence cs[] = new CharSequence[1];
            cs[0] = "These are no table in database";
            builder.setItems(cs, ((dialog, which) -> {/* DO NOTHING */}));
        }

        builder.setNeutralButton( "CREATE NEW", (dialog, which) -> dialog.dismiss() ); //Carry on with empty table
        builder.setNegativeButton( "CANCEL", (dialog, which) -> {
            dialog.cancel();
            startActivity(new Intent(AddLeagueData.this, MainActivity.class));
        } );

        //Run builder
        builder.show();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter( this, getSupportFragmentManager() );
        ViewPager viewPager = findViewById( R.id.view_pager );
        viewPager.setAdapter( sectionsPagerAdapter );
        TabLayout tabs = findViewById( R.id.tabs );
        tabs.setupWithViewPager( viewPager );
        FloatingActionButton add_button = findViewById(R.id.fAAdd);
        FloatingActionButton delete_button = findViewById(R.id.fADelete);
        FloatingActionButton save_button = findViewById(R.id.fASave);

        //Get tables and clear any existing data from them
        this.data_table = findViewById(R.id.tableLayout);
        for(int i = 1; i < this.data_table.getChildCount(); i++)
            this.deleteRow(i);

        this.schedule = findViewById(R.id.schedule);
        for (int i = 1; i < this.schedule.getChildCount(); i++)
            this.deleteMatch(i);
        this.schedule.setVisibility(GONE);

        tabs.getTabAt(0).setText("TEAMS");
        tabs.getTabAt(1).setText("SCHEDULE");

        tabs.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        data_table.setVisibility(VISIBLE);
                        break;
                    case 1:
                        schedule.setVisibility(VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        data_table.setVisibility(GONE);
                        break;
                    case 1:
                        schedule.setVisibility(GONE);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        } );

        add_button.setOnClickListener( v -> {
            if(this.data_table.getVisibility() == VISIBLE) {
                addRow();
            }
            else {
                addMatch();
            }
        } );

        delete_button.setOnClickListener( v -> {
            if(this.data_table.getVisibility() == VISIBLE) {
                if(data_table.getChildCount() > 1) {
                    for(int i = 1; i < data_table.getChildCount(); i++){
                        if(data_table.getChildAt(i).isSelected()) {
                            deleteRow( i );
                        }
                    }
                }
                else {
                    throw new IllegalStateException("Empty table");
                }
            }
            else {
                if(schedule.getChildCount() > 1) {
                    for(int i = 1; i < schedule.getChildCount(); i++) {
                        if(schedule.getChildAt(i).isSelected()) {
                            deleteMatch( i );
                        }
                    }
                }
                else {
                    throw new IllegalStateException("Empty table");
                }
            }
        } );

        save_button.setOnClickListener( v -> {
            if (data_table.getChildCount() > 1) {
                saveTable();
            } else {
                throw new IllegalStateException( "Empty table" );
            }
        } );
    }

    protected void updateTable(String selected) {
        SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("LeagueData.db", MODE_PRIVATE, null);

        //First update data table
        Cursor leagueCursor = sqLiteDatabase.rawQuery("SELECT t.* FROM LEAGUE_" + selected + " t LIMIT 501;", null);

        if(leagueCursor.moveToFirst()) {
            while(leagueCursor.moveToNext()) {
                TableRow row = new TableRow(this);

                for(int i = 0; i < leagueCursor.getColumnCount(); i++) {
                    TextView txt = new TextView(this);
                    txt.setText(leagueCursor.getString(i));
                    row.addView(txt);
                }

                this.data_table.addView(row);
            }

            //Next update schedule
            Cursor matchCursor = sqLiteDatabase.rawQuery("SELECT t.* FROM SCHEDULE_" + selected + " t LIMIT 501;", null);

            if(matchCursor.moveToFirst()) {
                while(matchCursor.moveToNext()) {
                    TableRow row = new TableRow(this);

                    for(int i = 0; i < matchCursor.getColumnCount(); i++) {
                        TextView txt = new TextView(this);
                        txt.setText(matchCursor.getString(i));
                        row.addView(txt);
                    }

                    this.schedule.addView(row);
                }
            }
        }
    }

    protected void addRow() {
        //Adds new row to data table
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add team");

        LayoutInflater inflater = getLayoutInflater();

        final View customLayout = inflater.inflate(R.layout.add_team, null);

        builder.setView(customLayout);

        builder.setPositiveButton( "Ok", (dialog, which) -> {
            //These items are initially visible
            EditText txt_teamname = customLayout.findViewById(R.id.txt_teamname);
            EditText txt_points5 = customLayout.findViewById(R.id.txt_points5);
            CheckBox checkBox = customLayout.findViewById(R.id.checkbox);

            //These items are initially invisible
            EditText txt_matchesplayed = customLayout.findViewById(R.id.txt_matches_played);
            EditText txt_wins = customLayout.findViewById(R.id.txt_wins);
            EditText txt_draws = customLayout.findViewById(R.id.txt_draws);
            EditText txt_loses = customLayout.findViewById(R.id.txt_loses);
            EditText txt_totalpoints = customLayout.findViewById(R.id.txt_pointstotal);
            EditText txt_gfor = customLayout.findViewById(R.id.txt_goalsfor);
            EditText txt_gag = customLayout.findViewById(R.id.txt_goalsag);

            TableRow newRow = new TableRow(getApplicationContext());

            TextView team_name = new TextView(getApplicationContext());
            team_name.setText(txt_teamname.getText().toString());
            newRow.addView(team_name);

            TextView matches_played = new TextView(getApplicationContext());
            matches_played.setText(txt_matchesplayed.getText().toString());
            newRow.addView(matches_played);

            TextView wins = new TextView(getApplicationContext());
            TextView draws = new TextView(getApplicationContext());
            TextView loses = new TextView(getApplicationContext());
            TextView total_points = new TextView(getApplicationContext());
            TextView goals_for = new TextView(getApplicationContext());
            TextView goals_against = new TextView(getApplicationContext());
            TextView goal_diff = new TextView(getApplicationContext());

            if(checkBox.isChecked()) {
                wins.setText(txt_wins.getText().toString());
                draws.setText(txt_draws.getText().toString());
                loses.setText(txt_loses.getText().toString());
                total_points.setText(txt_totalpoints.getText().toString());
                goals_for.setText(txt_gfor.getText().toString());
                goals_against.setText( txt_gag.getText().toString());
                goal_diff.setText(String.valueOf(
                        Integer.parseInt(txt_gfor.getText().toString()) -
                        Integer.parseInt(txt_gag.getText().toString())));
            }
            else {
                wins.setText(String.valueOf(0));
                draws.setText(String.valueOf(0));
                loses.setText(String.valueOf(0));
                total_points.setText(String.valueOf(0));
                goals_for.setText(String.valueOf(0));
                goals_against.setText(String.valueOf(0));
                goal_diff.setText(String.valueOf(0));
            }

            newRow.addView(wins);
            newRow.addView(draws);
            newRow.addView(loses);
            newRow.addView(total_points);
            newRow.addView(goals_for);
            newRow.addView(goals_against);
            newRow.addView(goal_diff);

            TextView points5 = new TextView(getApplicationContext());
            points5.setText(txt_points5.getText().toString());
            newRow.addView(points5);

            newRow.setClickable(true);

            newRow.setOnClickListener( v -> v.setSelected(true) );

            data_table.addView(newRow);
            dialog.dismiss();
        } );

        builder.setNegativeButton( "Cancel", (dialog, which) -> dialog.cancel() );

        builder.show();
    }

    protected void addMatch() {
        final String[] home_team = new String[1];
        final String[] away_team = new String[1];

        //Adds new row to data table
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add team");

        LayoutInflater inflater = getLayoutInflater();

        final View customLayout = inflater.inflate(R.layout.add_match, null);

        builder.setView(customLayout);

        Spinner home_spin = customLayout.findViewById(R.id.home_spinner);
        Spinner away_spin = customLayout.findViewById(R.id.away_spinner);
        EditText pick_date = customLayout.findViewById(R.id.enterDate);
        CheckBox completed = customLayout.findViewById(R.id.completed);
        EditText homeScore = customLayout.findViewById(R.id.home_score);
        EditText awayScore = customLayout.findViewById(R.id.away_score);

        homeScore.setVisibility(GONE);
        awayScore.setVisibility(GONE);

        //Populate spinner with teams from table
        CharSequence[] list_teams = new CharSequence[this.data_table.getChildCount() - 1];

        for(int i = 0; i < this.data_table.getChildCount() - 1; i++) {
            TableRow tableRow = (TableRow) this.data_table.getChildAt(i+1);
            list_teams[i] = ((TextView) tableRow.getChildAt(0)).getText();
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list_teams);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        home_spin.setAdapter(adapter);
        away_spin.setAdapter(adapter);

        home_spin.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView txt = (TextView) view;
                home_team[0] = txt.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

        away_spin.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView txt = (TextView) view;
                away_team[0] = txt.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );


        completed.setOnCheckedChangeListener( (buttonView, isChecked) -> {
            if(isChecked) {
                homeScore.setVisibility(VISIBLE);
                awayScore.setVisibility(VISIBLE);
            }
            else {
                homeScore.setVisibility(GONE);
                awayScore.setVisibility(GONE);
            }
        } );

        builder.setPositiveButton("OK", ((dialog, which) -> {

            TableRow tableRow = new TableRow(this);

            TextView hteam = new TextView(this);
            hteam.setText(home_team[0]);
            tableRow.addView(hteam);

            TextView ateam = new TextView(this);
            ateam.setText(away_team[0]);
            tableRow.addView(ateam);

            TextView date_txt = new TextView(this);
            date_txt.setText(pick_date.getText());
            tableRow.addView(date_txt);

            TextView checked = new TextView(this);
            TextView hscore = new TextView(this);
            TextView ascore = new TextView(this);

            if(completed.isChecked()) {
                checked.setText( "1" );
                hscore.setText(homeScore.getText());
                ascore.setText(awayScore.getText());

                int h = Integer.parseInt(homeScore.getText().toString());
                int a = Integer.parseInt(awayScore.getText().toString());

                if(h > a) {
                    //Update data table
                    this.updateRow( home_team[0], 3, h, a);
                    this.updateRow( away_team[0], 0, a, h);
                }
                else if(h < a) {
                    this.updateRow( home_team[0], 0, h, a);
                    this.updateRow( away_team[0], 3, a, h);
                }
                else {
                    //Update data table
                    this.updateRow( home_team[0], 1, h, a);
                    this.updateRow( away_team[0], 1, a, h);
                }
            }
            else {
                checked.setText( "0" );
                hscore.setText( null );
                ascore.setText( null );
            }

            tableRow.addView(checked);
            tableRow.addView(hscore);
            tableRow.addView(ascore);

            schedule.addView(tableRow);

            dialog.dismiss();
        }))
            .setNegativeButton("CANCEL", ((dialog, which) -> dialog.cancel()));

        builder.show();
    }

    protected void deleteMatch(int i) {
        this.schedule.removeViewAt(i);
    }

    protected void saveSchedule(String timestamp) {
        if(timestamp != null) {
            //Get database
            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("LeagueData.db", MODE_PRIVATE, null);

            //Check league table exists
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM League_" + timestamp + ";", null);
            if(cursor.moveToFirst()) { //League table exists in database

                //Check if schedule table exists
                Cursor cursor2 = sqLiteDatabase.rawQuery("SELECT * FROM SCHEDULE_" + timestamp + ";", null);
                if(!cursor2.moveToFirst()) { //Schedule not in database
                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SCHEDULE_" + timestamp +
                            "(home TEXT, away TEXT, date NUMERIC, completed NUMERIC, hscore INTEGER, ascore INTEGER);");
                }

                Cursor cursor3 = sqLiteDatabase.rawQuery("SELECT home, away FROM SCHEDULE_" + timestamp + ";", null);

                Function3<String, String, Cursor, Boolean> fn = (hstr, astr, curs) -> {
                    Boolean bres = false;
                    while(curs.moveToNext()) {
                        if(curs.getString(0) == hstr && curs.getString(1) == astr) {
                            return true;
                        }
                    }
                    return bres;
                };

                for(int i = 1; i < this.schedule.getChildCount(); i++) {
                    String hstr = String.valueOf( ((TextView)((TableRow) this.schedule.getChildAt(i)).getChildAt(0)).getText() );
                    String astr = String.valueOf( ((TextView)((TableRow) this.schedule.getChildAt(i)).getChildAt(1)).getText() );
                    long date = Long.parseLong( String.valueOf( ((TextView)((TableRow) this.schedule.getChildAt(i)).getChildAt(3)).getText() ) );
                    int checked = Integer.parseInt( String.valueOf( ((TextView)((TableRow) this.schedule.getChildAt(i)).getChildAt(4)).getText() ) );
                    int hscore = Integer.parseInt( String.valueOf( ((TextView)((TableRow) this.schedule.getChildAt(i)).getChildAt(5)).getText() ) );
                    int ascore = Integer.parseInt( String.valueOf( ((TextView)((TableRow) this.schedule.getChildAt(i)).getChildAt(6)).getText() ) );
                    if(!fn.invoke(hstr, astr, cursor3)) {
                        sqLiteDatabase.execSQL("INSERT INTO SCHEDULE_" + timestamp + "(home, away, date, completed, hscore, ascore) VALUES (" +
                                hstr + ", " + astr + ", " + date + ", " + checked + ", " + hscore + ", " + ascore + ");");
                    }
                    else {
                        sqLiteDatabase.execSQL("UPDATE TABLE SCHEDULE_" + timestamp + "WHERE home=" +
                                hstr + "AND away=" + astr +"VALUES (" + hstr + ", " + astr + ", " +
                                date + ", " + checked + ", " + hscore + ", " + ascore + ");");
                    }
                }
            }
            else { //League table does not exists
                throw new IllegalStateException("Time stamp does not match any existing data tables.");
            }
        }
        else {
            throw new IllegalStateException("Time stamp does not match any existing data tables.");
        }
    }

    protected void updateRow(String team_name, int points, int gfor, int gag) {
        //Search table for team name
        TableRow tableRow = null;
        int i = 1;
        while(i < this.data_table.getChildCount()) {
            if(((TableRow)this.data_table.getChildAt(i)).getChildAt(0).toString() == team_name) {
                tableRow = (TableRow)this.data_table.getChildAt(i);
            }

            i++;
        }

        if(tableRow != null) {
            int total_points = Integer.parseInt(tableRow.getChildAt(4).toString());
            total_points += points;
            ((TextView) tableRow.getChildAt(4)).setText(String.valueOf(total_points));

            if(points == 3) {
                int wins = Integer.parseInt(tableRow.getChildAt(1).toString());
                wins++;
                ((TextView) tableRow.getChildAt(1)).setText(String.valueOf(wins));
            }
            else if(points == 0) {
                int loses = Integer.parseInt(tableRow.getChildAt(3).toString());
                loses++;
                ((TextView) tableRow.getChildAt(3)).setText(String.valueOf(loses));
            }
            else {
                int draws = Integer.parseInt(tableRow.getChildAt(2).toString());
                draws++;
                ((TextView) tableRow.getChildAt(2)).setText(String.valueOf(draws));
            }

            int newgfor = Integer.parseInt(tableRow.getChildAt(5).toString());
            newgfor += gfor;
            ((TextView) tableRow.getChildAt(5)).setText(String.valueOf(newgfor));

            int newgag = Integer.parseInt(tableRow.getChildAt(6).toString());
            newgag += gag;
            ((TextView) tableRow.getChildAt(6)).setText(String.valueOf(newgag));

            ((TextView) tableRow.getChildAt(7)).setText(String.valueOf(newgfor - newgag));
        }
        else {
            throw new IllegalStateException("Team does not exist.");
        }
    }

    protected void deleteRow(int i) {
        //Deletes row from data table
        this.data_table.removeViewAt(i);
    }

    protected void saveTable() {
        //Saves table to database

        //Get current timestamp
        Date time_now = new Date();
        this.time_stamp = time_now.toString().replaceAll("\\s+", "").replaceAll(":", "").replaceAll(Pattern.quote("+"), "");

        //Get database
        SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("LeagueData.db", MODE_PRIVATE, null);

        //Create Table
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "
                + "League_" + time_stamp
                + " (teamName TEXT, matchesPlayed INTEGER, wins INTEGER, draws INTEGER, loses INTEGER, " +
                "totalPoints INTEGER, goalsFor INTEGER, goalsAgainst INTEGER, goalsDiff INTEGER, " +
                "pointsFive Integer, class TEXT);");

        //Populate table with data
        for(int i = 1; i < this.data_table.getChildCount(); i++) {
            TableRow tableRow = (TableRow) this.data_table.getChildAt(i);

            int goalDiff = Integer.parseInt(String.valueOf(((TextView)tableRow.getChildAt(7)).getText()))
                    - Integer.parseInt(String.valueOf(((TextView)tableRow.getChildAt(8)).getText()));

            sqLiteDatabase.execSQL("INSERT INTO " + "League_" + time_stamp + "(teamName, matchesPlayed" +
                    "wins, draws, loses, totalPoints, goalsFor, goalsAgainst, goalsDiff, pointsFive, class) VALUES ("
                    + ((TextView)tableRow.getChildAt(0)).getText() + ", "
                    + ((TextView)tableRow.getChildAt(1)).getText() + ", "
                    + ((TextView)tableRow.getChildAt(2)).getText() + ", "
                    + ((TextView)tableRow.getChildAt(3)).getText() + ", "
                    + ((TextView)tableRow.getChildAt(4)).getText() + ", "
                    + ((TextView)tableRow.getChildAt(5)).getText() + ", "
                    + ((TextView)tableRow.getChildAt(6)).getText() + ", "
                    + ((TextView)tableRow.getChildAt(7)).getText() + ", "
                    + ((TextView)tableRow.getChildAt(8)).getText() + ", "
                    + goalDiff + ", "
                    + ((TextView)tableRow.getChildAt(9)).getText() + ", "
                    + "S);");
        }

        sqLiteDatabase.close();
        saveSchedule(this.time_stamp);

        startActivity( new Intent(AddLeagueData.this, MainActivity.class) );
    }
}