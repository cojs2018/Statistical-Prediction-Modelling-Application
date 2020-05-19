package com.example.statisticalpredictionmodellingapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.softmoore.android.graphlib.Graph;
import com.softmoore.android.graphlib.GraphView;
import com.softmoore.android.graphlib.Point;

import java.util.Vector;

public class Results extends AppCompatActivity {
    Season season;

    public ResultsTableLayout resultsTableLayout;
    public ResultsSeekBar resultsSeekBar;
    public ResultsGraphView resultsGraphView;
    public ResultsListViews resultsListViews;

    //Default constructor
    public Results() {/*Unused*/}

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_results );

        View view = new View( this );

        try {
            //ACTION_GET_CONTENT is the intent to choose a file via the system browser.
            Intent intent0 = new Intent( Intent.ACTION_GET_CONTENT );
            //Filter to only show results that can be opened
            intent0.addCategory( Intent.CATEGORY_OPENABLE );
            //Only show files of type .arff
            intent0.setType( "*/*" );
            //startActivityForResult( intent0, PICKFILE_RESULT_CODE );

            Intent intent1 = new Intent( Intent.ACTION_GET_CONTENT );
            intent1.addCategory( Intent.CATEGORY_OPENABLE );
            intent1.setType( "*/*" );
            //startActivityForResult( intent1, PICKFILE_RESULT_CODE );

            Season season = new Season();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                season.getLeague();
            }

            CollectionResultsFragment collectionResultsFragment = new CollectionResultsFragment(season, view, savedInstanceState);

            LinearLayout linlayout = view.findViewById(R.id.linearLayout3);

            //collectionResultsFragment.enable(view, savedInstanceState);
        }
        catch(Exception exe) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
            builder.setTitle("ERROR: Exception!");
            builder.setMessage(exe.getMessage());
        }
    }

    //Default constructor
    public Results(Season season) {
        //CollectionResultsFragment collectionResultsFragment = new CollectionResultsFragment(season);
    }


    public class CollectionResultsFragment extends Fragment {
        Season season;

        ResultsCollectionAdapter resultsCollectionAdapter;
        ViewPager2 viewPager;

        public CollectionResultsFragment(Season season, View view, Bundle savedInstanceState) {
            this.season = season;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflator, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
            return inflator.inflate(R.layout.activity_results, container, false);
        }

        public void enable(View view, Bundle savedInstanceState) {
            resultsCollectionAdapter = new ResultsCollectionAdapter(this);
            viewPager = view.findViewById(R.id.pager);
            viewPager.setAdapter( resultsCollectionAdapter);

            TabLayout tabLayout = view.findViewById(R.id.tab_layout);

            TabLayout.Tab tableTab = new TabLayout.Tab();
            tableTab.setText("TABLE");
            tabLayout.addTab(tableTab, 0, true); //Default plane
            tabLayout.addView(view.findViewById(R.id.tab_table));

            TabLayout.Tab graphTab = new TabLayout.Tab();
            graphTab.setText("GRAPH");
            tabLayout.addTab(graphTab, 1, false);
            tabLayout.addView(view.findViewById(R.id.tab_graph));

            TabLayout.Tab resultsTab = new TabLayout.Tab();
            resultsTab.setText("RESULTS");
            tabLayout.addTab(resultsTab, 2, false);
            tabLayout.addView(view.findViewById(R.id.tab_results));

            tabLayout.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    /* Here there are three tabs determining which items are shown on screen.
                     * 1. The first and default tab will be displaying the table view.
                     * 2. The second will be showing the graph data.
                     * 3. The third will show the results individually in a list view
                     */

                    if(tabLayout.getSelectedTabPosition() == 0) {
                        //Display first plane
                        resultsTableLayout = new ResultsTableLayout();
                        resultsTableLayout.x = 0; //Set table at initial data;
                        resultsTableLayout.tableRes.setVisibility(View.VISIBLE);
                        resultsTableLayout.enable(season, view, savedInstanceState);

                        resultsSeekBar = new ResultsSeekBar();
                        resultsSeekBar.seekbar.setVisibility(View.VISIBLE);
                        resultsSeekBar.enable(season, view, savedInstanceState);
                    }
                    else if (tabLayout.getSelectedTabPosition() == 1) {
                        //Display second plane
                        resultsGraphView = new ResultsGraphView();
                        resultsGraphView.graphView.setVisibility(View.VISIBLE);
                        resultsGraphView.enable(season, view, savedInstanceState);
                    }
                    else if (tabLayout.getSelectedTabPosition() == 2) {
                        //Display third plane
                        resultsListViews = new ResultsListViews();
                        resultsListViews.linearLayout.setVisibility(View.VISIBLE);
                        resultsListViews.enable(season, view, savedInstanceState);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    if(tab.getPosition() == 0) {
                        //Make table invisible
                        resultsTableLayout.destroy();
                        resultsSeekBar.destroy();
                    }
                    else if (tab.getPosition() == 1) {
                        //Make graph invisible
                        resultsGraphView.destroy();
                    }
                    else if (tab.getPosition() == 2) {
                        //Make thrid plane invisible
                        resultsListViews.destroy();
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    this.onTabSelected(tab);
                }
            } );
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            resultsCollectionAdapter = new ResultsCollectionAdapter(this);
            viewPager = view.findViewById(R.id.pager);
            viewPager.setAdapter( resultsCollectionAdapter);

            TabLayout tabLayout = view.findViewById(R.id.tab_layout);

            TabLayout.Tab tableTab = new TabLayout.Tab();
            tableTab.setText("TABLE");
            tabLayout.addTab(tableTab, 0, true); //Default plane
            tabLayout.addView(view.findViewById(R.id.tab_table));

            TabLayout.Tab graphTab = new TabLayout.Tab();
            graphTab.setText("GRAPH");
            tabLayout.addTab(graphTab, 1, false);
            tabLayout.addView(view.findViewById(R.id.tab_graph));

            TabLayout.Tab resultsTab = new TabLayout.Tab();
            resultsTab.setText("RESULTS");
            tabLayout.addTab(resultsTab, 2, false);
            tabLayout.addView(view.findViewById(R.id.tab_results));

            tabLayout.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    /* Here there are three tabs determining which items are shown on screen.
                     * 1. The first and default tab will be displaying the table view.
                     * 2. The second will be showing the graph data.
                     * 3. The third will show the results individually in a list view
                     */

                    if(tabLayout.getSelectedTabPosition() == 0) {
                        //Display first plane
                        resultsTableLayout = new ResultsTableLayout();
                        resultsTableLayout.x = 0; //Set table at initial data;
                        resultsTableLayout.tableRes.setVisibility(View.VISIBLE);
                        resultsTableLayout.enable(season, view, savedInstanceState);

                        resultsSeekBar = new ResultsSeekBar();
                        resultsSeekBar.seekbar.setVisibility(View.VISIBLE);
                        resultsSeekBar.enable(season, view, savedInstanceState);
                    }
                    else if (tabLayout.getSelectedTabPosition() == 1) {
                        //Display second plane
                        resultsGraphView = new ResultsGraphView();
                        resultsGraphView.graphView.setVisibility(View.VISIBLE);
                        resultsGraphView.enable(season, view, savedInstanceState);
                    }
                    else if (tabLayout.getSelectedTabPosition() == 2) {
                        //Display third plane
                        resultsListViews = new ResultsListViews();
                        resultsListViews.linearLayout.setVisibility(View.VISIBLE);
                        resultsListViews.enable(season, view, savedInstanceState);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    if(tab.getPosition() == 0) {
                        //Make table invisible
                        resultsTableLayout.destroy();
                        resultsSeekBar.destroy();
                    }
                    else if (tab.getPosition() == 1) {
                        //Make graph invisible
                        resultsGraphView.destroy();
                    }
                    else if (tab.getPosition() == 2) {
                        //Make thrid plane invisible
                        resultsListViews.destroy();
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    this.onTabSelected(tab);
                }
            } );
        }
    }

    public class ResultsCollectionAdapter extends FragmentStateAdapter {
        public ResultsCollectionAdapter(Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = new ResultsObjectFragment();
            Bundle args = new Bundle();
            args.putInt(ResultsObjectFragment.ARG_OBJECT, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemCount(){
            return 100;
        }
    }

    public class ResultsObjectFragment extends Fragment {
        public static final String ARG_OBJECT = "object";

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_collection_object, container, 
                    false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            Bundle args = getArguments();
            ((TextView) view.findViewById(android.R.id.text1)
            ).setText(Integer.toString(args.getInt(ARG_OBJECT)));
        }
    }


    public class ResultsTableLayout {

        public int x;

        public Season season;

        public TableLayout tableRes;

        public void enable(Season ssn, @NonNull View view, @Nullable Bundle savedInstanceState) {
            this.season = ssn;
            tableRes = view.findViewById(R.id.table_res);

            if(tableRes.getVisibility() != View.VISIBLE) {
                tableRes.setVisibility(View.VISIBLE);
            }

            Vector<Season.Team> leagueResults = 
                    season.resultsLeague.elementAt(x).league_at_week;

            //Print results in table
            for (int i = 0; i < leagueResults.size(); i++) {
                Context context = getApplicationContext();

                //Create new row for each entry
                TableRow newRow = new TableRow(context);

                TableRow.LayoutParams layoutParams = 
                        new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                newRow.setLayoutParams(layoutParams);

                TextView rk = new TextView(context);
                rk.setText(i);
                newRow.addView(rk, 0);

                TextView name = new TextView(context);
                name.setText(leagueResults.elementAt(i).team_name);
                newRow.addView(name, 1);

                TextView mp = new TextView(context);
                mp.setText(leagueResults.elementAt(i).matches_played);
                newRow.addView(mp, 2);

                TextView w = new TextView(context);
                w.setText(leagueResults.elementAt(i).total_wins);
                newRow.addView(w, 3);

                TextView d = new TextView(context);
                d.setText(leagueResults.elementAt(i).total_draws);
                newRow.addView(d, 4);

                TextView l = new TextView(context);
                l.setText(leagueResults.elementAt(i).total_loses);
                newRow.addView(l, 5);

                TextView tp = new TextView(context);
                tp.setText(leagueResults.elementAt(i).total_points);
                newRow.addView(tp,6);

                TextView gf = new TextView(context);
                gf.setText(leagueResults.elementAt(i).goals_for);
                newRow.addView(gf, 7);

                TextView ga = new TextView(context);
                ga.setText(leagueResults.elementAt(i).goals_against);
                newRow.addView(ga, 8);

                TextView gd = new TextView(context);
                gd.setText(leagueResults.elementAt(i).goals_difference);
                newRow.addView(gd, 9);

                TextView p5 = new TextView(context);
                p5.setText(leagueResults.elementAt(i).points_from_5);
                newRow.addView(p5, 10);

                tableRes.addView(newRow);
            }
        }

        public void destroy(){
            tableRes.setVisibility(View.GONE);
        }
    }

    public class ResultsSeekBar {

        public Season season;

        public SeekBar seekbar;

        public void enable(Season ssn, @NonNull View view, @Nullable Bundle savedInstanceState) {
            this.season = ssn;

            ResultsTableLayout results_table = new ResultsTableLayout();

            results_table.x = 0;

            //Set out seekBar
            seekbar = view.findViewById(R.id.seek_bar);
            seekbar.setMax(season.resultsLeague.size() - 1);

            seekbar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    results_table.x = progress;
                    results_table.enable(season, view, savedInstanceState);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            } );
        }

        public void destroy() {
            seekbar.setVisibility(View.GONE);
        }
    }

    public class ResultsGraphView {

        public Season season;

        public GraphView graphView;

        public void enable(Season ssn, @NonNull View view, @Nullable Bundle savedInstanceState){
            this.season = ssn;

            //Declare the three buttons
            Button button_horizontal = view.findViewById(R.id.axis_data_horizontal);
            Button button_vertical = view.findViewById(R.id.axis_data_vertical);
            Button button_line = view.findViewById(R.id.line_choice);

            Context context = getApplicationContext();

            //Declare graph and graph view
            //Graph graph = new Graph.Builder().build();
            graphView = view.findViewById(R.id.graph_view);

            final int[] x = new int[1];
            final int[] y = new int[1];

            final Vector<String> setOfTeamNames = new Vector<>();

            button_horizontal.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Horizontal Axis");

                    builder.setItems( new CharSequence[]{"Matches played", "Total wins",
                                    "Total draws", "Total loses", "Total points", "Goals For", "Goals Against",
                                    "Goal difference", "Points from 5"},
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    y[0] = which;
                                }
                            } );
                }
            } );

            button_vertical.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Vertical Axis");

                    builder.setItems( new CharSequence[]{"Matches played", "Total wins",
                                    "Total draws", "Total loses", "Total points", "Goals For", "Goals Against",
                                    "Goal difference", "Points from 5"},
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    x[0] = which;
                                }
                            } );
                }
            } );

            button_line.setOnClickListener( new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Select Teams");

                    CharSequence charSeq[] = new CharSequence[season.resultsLeague.elementAt(0).league_at_week.size()];
                    boolean checkedItems[] = new boolean[season.resultsLeague.elementAt(0).league_at_week.size()];
                    for(int i = 0; i < season.resultsLeague.elementAt(0).league_at_week.size(); i++){
                        charSeq[i] = season.resultsLeague.elementAt(0).league_at_week.elementAt(i).team_name;
                        checkedItems[i] = true;
                    }

                    builder.setMultiChoiceItems( charSeq, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            //ToDo: edit which lines are shown in graph
                            String teamName = String.valueOf( season.resultsLeague.elementAt(0).league_at_week.elementAt(which));

                            if(isChecked){
                                setOfTeamNames.add(teamName);
                            }
                            else {
                                setOfTeamNames.remove(teamName);
                            }
                        }
                    } );

                    //Construct graph
                    Graph graph = new Graph.Builder().build();

                    for(int i = 0; i < setOfTeamNames.size(); i++) {
                        Vector<Point> newLine = season.getPoints(x[0], y[0], setOfTeamNames.elementAt(i));

                        graph = new Graph.Builder().addLineGraph(newLine).build();
                    }

                    graphView.setGraph(graph);
                }
            } );
        }

        public void destroy() {
            graphView.setVisibility(View.GONE);
        }
    }

    public class ResultsListViews {
        public Season season;

        public LinearLayout linearLayout;

        public void enable(Season ssn, @NonNull View view, @Nullable Bundle savedInstanceState){
            this.season = ssn;
            linearLayout = view.findViewById(R.id.linear_layout);

            TableLayout classification0 = view.findViewById(R.id.C0_list);
            TableLayout classification1 = view.findViewById(R.id.C1_list);
            TableLayout classification2 = view.findViewById(R.id.C2_list);
            TableLayout matchResults = view.findViewById(R.id.match_results);

            Season.League finalResult = season.resultsLeague.lastElement();

            int n = finalResult.league_at_week.size();

            Context context = view.getContext();

            for(int i = 0; i < n; i++) {
                TableRow item = new TableRow(context);

                TextView position = new TextView(context);
                position.setText(i);
                item.addView(position, 0);

                TextView teamName = new TextView(context);
                teamName.setText(finalResult.league_at_week.elementAt(i).team_name);
                item.addView(teamName, 1);

                if(i < 4) {
                    //Promoted set 1
                    classification0.addView(item);
                }
                else if(i >= 4 && i < 7) {
                    //Promoted set 2
                    classification1.addView(item);
                }
                else if(i > n - 4) {
                    //Relegated
                    classification2.addView(item);
                }
            }

            for(int j = 0; j < season.matchResults.size(); j++) {
                TableRow matchItem = new TableRow(context);

                TextView homeTeam = new TextView(context);
                homeTeam.setText(season.matchResults.elementAt(j).home.team_name);
                matchItem.addView(homeTeam, 0);

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

                matchResults.addView(matchItem);
            }        }

        public void destroy() {
            linearLayout.setVisibility(View.GONE);
        }
    }
}
