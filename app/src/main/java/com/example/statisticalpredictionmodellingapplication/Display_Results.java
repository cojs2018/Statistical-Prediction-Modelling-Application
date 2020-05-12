package com.example.statisticalpredictionmodellingapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.statisticalpredictionmodellingapplication.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.softmoore.android.graphlib.Graph;
import com.softmoore.android.graphlib.GraphView;
import com.softmoore.android.graphlib.Point;

import java.util.Vector;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Display_Results extends AppCompatActivity {

    public static final int PICKFILE_RESULT_CODE = 8778;

    public String training_set = null;
    public String test_set = null;

    public TableLayout table;
    public SeekBar seekbar;
    public GraphView graphView;
    public Button button_horizontal;
    public Button button_vertical;
    public Button button_line;
    public LinearLayout lllist;
    public TableLayout list_table;
    public Season season;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_display__results );

        try {
            /*Get list of data_tables from database
             * 1. Query database for a list of data tables. */
            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("LeagueData.db", MODE_PRIVATE, null);
            Cursor tablesCursor = sqLiteDatabase.rawQuery("SELECT * FROM sqlite_master " +
                    "Where type='table' AND intr(name, 'LEAGUE') > 0" +
                    "ORDER BY name;", null);

            //2. Call new alert dialog
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setTitle("Update data set");

            //3. If cursor has return a list of items, show these as items on screen, else say there are no items available
            if(tablesCursor.moveToFirst()) {
                Vector<String> tableList = new Vector<>();
                while(tablesCursor.moveToNext()) {
                    tableList.add(tablesCursor.getString(1).substring(7));
                }

                builder.setItems( tableList.toArray( new CharSequence[tableList.size()]), (dialog, which) -> {
                    this.season = new Season();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        try {
                            season.getData(tableList.elementAt(which));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else {
                CharSequence cs[] = new CharSequence[1];
                cs[1] = "These are no table in database";
                builder.setItems(cs, ((dialog, which) -> {/* DO NOTHING */}));
            }

            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter( this, getSupportFragmentManager() );
            ViewPager viewPager = findViewById( R.id.view_pager );
            viewPager.setAdapter( sectionsPagerAdapter );
            TabLayout tabs = findViewById( R.id.tabs );
            tabs.setupWithViewPager( viewPager );

            //Add tabs to tab-layout
            tabs.getTabAt(0).setText("TABLE");
            tabs.getTabAt(1).setText("GRAPH");
            tabs.addTab(tabs.newTab().setText("LIST"));

            this.table = findViewById(R.id.table);

            table.setVisibility( GONE );

            this.graphView = findViewById(R.id.pred_graph);
            this.lllist = findViewById(R.id.linlaylist);

            //Declare the three buttons
            this.button_horizontal = findViewById(R.id.horizontal);
            this.button_vertical = findViewById(R.id.vertical);
            this.button_line = findViewById(R.id.select);

            graphView.setVisibility( GONE );
            button_horizontal.setVisibility( GONE );
            button_vertical.setVisibility( GONE );
            button_line.setVisibility( GONE );

            this.list_table = findViewById(R.id.list_table);
            lllist.setVisibility( GONE );
            list_table.setVisibility(GONE);

            //Add state listener
            tabs.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch(tab.getPosition()) {
                        case 0:
                            showTable();
                            break;
                        case 1:
                            showGraph();
                            break;
                        case 2:
                            showList();
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    switch(tab.getPosition()) {
                        case 0:
                            if(table != null)
                                table.setVisibility( GONE );
                            break;
                        case 1:
                            if(graphView != null) {
                                graphView.setVisibility( GONE );
                                button_horizontal.setVisibility( GONE );
                                button_vertical.setVisibility( GONE );
                                button_line.setVisibility( GONE );
                            }
                            break;
                        case 2:
                            if(lllist != null) {
                                lllist.setVisibility( GONE );
                                list_table.setVisibility(GONE);
                            }
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            } );

            FloatingActionButton fab = findViewById( R.id.fab );

            fab.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make( view, "Replace with your own action", Snackbar.LENGTH_LONG )
                            .setAction( "Action", null ).show();
                }
            } );
        }
        catch(Exception exe) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setTitle("ERROR: Exception!");
            builder.setMessage(exe.getMessage());
            builder.show();
        }
    }

    protected void showTable() {
        this.table = findViewById(R.id.table);

        TableRow header_row = new TableRow(this);
        header_row.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT));

        TextView rank = new TextView(this);
        rank.setText("rk");
        header_row.addView(rank);

        TextView team_name = new TextView(this);
        team_name.setText("tn");
        header_row.addView(team_name);

        TextView matches_played = new TextView(this);
        matches_played.setText("mp");
        header_row.addView(matches_played);

        TextView wins = new TextView(this);
        wins.setText("w");
        header_row.addView(wins);

        TextView draws = new TextView(this);
        draws.setText("d");
        header_row.addView(draws);

        TextView loses = new TextView(this);
        loses.setText("l");
        header_row.addView(loses);

        TextView total_points = new TextView(this);
        total_points.setText("tp");
        header_row.addView(total_points);

        TextView gfor = new TextView(this);
        gfor.setText("gf");
        header_row.addView(gfor);

        TextView gagainst = new TextView(this);
        gagainst.setText("ga");
        header_row.addView(gagainst);

        TextView gdiff = new TextView(this);
        gdiff.setText("gd");
        header_row.addView(gdiff);

        TextView points5 = new TextView(this);
        points5.setText("p5");
        header_row.addView(points5);

        table.addView(header_row);

        //Set up first data display
        this.tableAddData(0);

        seekbar = findViewById(R.id.seekbar);
        seekbar.setMax(this.season.resultsLeague.size() - 1);
        seekbar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    tableAddData(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        } );

        tableAddData(0);
        if(table.getVisibility() == GONE) {
            table.setVisibility( VISIBLE);
        }
    }

    protected void tableAddData(int entry) {
        //Remove any existing items beforehand
        if(this.table.getChildCount() > 1) {
            for(int i = this.table.getChildCount() - 1; i > 0; i--) {
                this.table.removeViewAt(i);
            }
        }

        Vector<Season.Team> leagueResults =
                season.resultsLeague.elementAt(entry).league_at_week;

        //Print results in table
        for (int i = 0; i < leagueResults.size(); i++) {

            //Create new row for each entry
            TableRow newRow = new TableRow( this );

            TableRow.LayoutParams layoutParams =
                    new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT );
            newRow.setLayoutParams( layoutParams );

            TextView rk = new TextView( this );
            rk.setText( String.valueOf(i) );
            newRow.addView( rk );

            TextView name = new TextView( this );
            name.setText( leagueResults.elementAt( i ).team_name );
            newRow.addView( name );

            TextView mp = new TextView( this );
            mp.setText( String.valueOf(leagueResults.elementAt( i ).matches_played) );
            newRow.addView( mp );

            TextView w = new TextView( this );
            w.setText( String.valueOf(leagueResults.elementAt( i ).total_wins) );
            newRow.addView( w );

            TextView d = new TextView( this );
            d.setText( String.valueOf(leagueResults.elementAt( i ).total_draws) );
            newRow.addView( d );

            TextView l = new TextView( this );
            l.setText( String.valueOf(leagueResults.elementAt( i ).total_loses) );
            newRow.addView( l );

            TextView tp = new TextView( this );
            tp.setText( String.valueOf(leagueResults.elementAt( i ).total_points) );
            newRow.addView( tp );

            TextView gf = new TextView( this );
            gf.setText( String.valueOf(leagueResults.elementAt( i ).goals_for) );
            newRow.addView( gf );

            TextView ga = new TextView( this );
            ga.setText( String.valueOf(leagueResults.elementAt( i ).goals_against) );
            newRow.addView( ga );

            TextView gd = new TextView( this );
            gd.setText( String.valueOf(leagueResults.elementAt( i ).goals_difference) );
            newRow.addView( gd );

            TextView p5 = new TextView( this );
            p5.setText( String.valueOf(leagueResults.elementAt( i ).points_from_5) );
            newRow.addView( p5 );

            this.table.addView( newRow );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void showGraph() {
        //Declare graph and graph view
        Graph graph = new Graph.Builder().build();

        if(this.graphView.getVisibility() == GONE) {
            this.graphView.setVisibility(VISIBLE);
        }

        if(this.button_horizontal.getVisibility() == GONE) {
            this.button_horizontal.setVisibility(VISIBLE);
        }

        if(this.button_vertical.getVisibility() == GONE) {
            this.button_vertical.setVisibility(VISIBLE);
        }

        if(this.button_line.getVisibility() == GONE) {
            this.button_line.setVisibility(VISIBLE);
        }

        final int[] x = new int[1];
        final int[] y = new int[1];

        final Vector<String> setOfTeamNames = new Vector<>();

        this.button_horizontal.setOnClickListener( v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

            builder.setTitle("Horizontal Axis");

            builder.setItems( new CharSequence[]{"Matches played", "Total wins",
                            "Total draws", "Total loses", "Total points", "Goals For", "Goals Against",
                            "Goal difference", "Points from 5"},
                    (dialog, which) -> y[0] = which );

            builder.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    y[0] = position;
                    getGraph(x[0], y[0], setOfTeamNames);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            } );

            builder.show();
        } );

        this.button_vertical.setOnClickListener( v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

            builder.setTitle("Vertical Axis");

            builder.setItems( new CharSequence[]{"Matches played", "Total wins",
                            "Total draws", "Total loses", "Total points", "Goals For", "Goals Against",
                            "Goal difference", "Points from 5"},
                    (dialog, which) -> x[0] = which );

            builder.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    x[0] = position;
                    getGraph(x[0], y[0], setOfTeamNames);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            } );

            builder.show();
        } );

        this.button_line.setOnClickListener( v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder( v.getContext() );

            builder.setTitle( "Select Teams" );

            CharSequence charSeq[] = new CharSequence[season.resultsLeague.elementAt( 0 ).league_at_week.size()];
            boolean checkedItems[] = new boolean[season.resultsLeague.elementAt( 0 ).league_at_week.size()];
            for (int i = 0; i < season.resultsLeague.elementAt( 0 ).league_at_week.size(); i++) {
                charSeq[i] = season.resultsLeague.elementAt( 0 ).league_at_week.elementAt( i ).team_name;
                checkedItems[i] = true;
            }

            builder.setMultiChoiceItems( charSeq, checkedItems, (dialog, which, isChecked) -> {
                //ToDo: edit which lines are shown in graph
                String teamName = String.valueOf( season.resultsLeague.elementAt( 0 ).league_at_week.elementAt( which ) );

                if (isChecked) {
                    setOfTeamNames.add( teamName );
                } else {
                    setOfTeamNames.remove( teamName );
                }
            } );

            builder.setPositiveButton( "APPLY", (dialog, which) -> getGraph(x[0], y[0], setOfTeamNames) );
            builder.setNegativeButton("CANCEL", ((dialog, which) -> dialog.cancel()));

            builder.show();
        });

        this.graphView.setGraph(graph);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void getGraph(int x, int y, Vector<String> team_names) {
        Graph graph = null;

        for(int i = 0; i < team_names.size(); i++) {
            Vector<Point> newLine = this.season.getPoints(x, y, team_names.elementAt(i));

            graph = new Graph.Builder().addLineGraph(newLine).build();
        }

        this.graphView.setGraph(graph);
    }

    protected void showList() {
        this.lllist = findViewById(R.id.linlaylist);

        if(this.lllist.getVisibility() == GONE) {
            this.lllist.setVisibility(VISIBLE);
        }

        Season.League finalResult = season.resultsLeague.lastElement();

        int n = finalResult.league_at_week.size();

        for(int i = 0; i < n; i++) {
            TableRow item = new TableRow(this);

            TextView position = new TextView(this);
            position.setText(String.valueOf(i));
            item.addView(position, 0);

            TextView teamName = new TextView(this);
            teamName.setText(finalResult.league_at_week.elementAt(i).team_name);
            item.addView(teamName, 1);

            if(i < 4) {
                //Promoted set 1
                this.list_table.addView(item, i+1);
            }
            else if(i >= 4 && i < 7) {
                //Promoted set 2
                this.list_table.addView(item, i+2);
            }
            else if(i > n - 4) {
                //Relegated
                this.list_table.addView(item);
            }
        }

        for(int j = 0; j < season.matchResults.size(); j++) {
            TableRow matchItem = new TableRow(this);

            TextView homeTeam = new TextView(this);
            homeTeam.setText(season.matchResults.elementAt(j).home.team_name);
            matchItem.addView(homeTeam);

            /*Matrix score = season.matchResults.elementAt(j).result.coefficients.index_times(
                    season.matchResults.elementAt(j).result.xvar);

            TextView homeScore = new TextView(context);
            homeScore.setText(score.get(0, 0));
            matchItem.addView(homeScore, 1);

            TextView vs = new TextView(context);
            vs.setText(" - ");
            matchItem.addView(vs, 2);

            TextView awayScore = new TextView(context);
            awayScore.setText(score.get(1, 0));
            matchItem.addView(awayScore, 3);

            TextView awayTeam = new TextView(context);
            awayTeam.setText(season.matchResults.elementAt(j).away.team_name);
            matchItem.addView(awayTeam, 4);*/

            this.list_table.addView(matchItem);
        }
    }
}