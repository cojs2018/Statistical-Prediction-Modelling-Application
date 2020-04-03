package com.example.statisticalpredictionmodellingapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
//import com.google.android.material.tabs.TabLayoutMediator;

import com.softmoore.android.graphlib.Function;
import com.softmoore.android.graphlib.Graph;
import com.softmoore.android.graphlib.GraphView;
import com.softmoore.android.graphlib.Label;
import com.softmoore.android.graphlib.Point;

import java.util.Vector;

public class Results extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
    }

    public class CollectionResultsFragment extends Fragment {
        ResultsCollectionAdapter resultsCollectionAdapter;
        ViewPager viewPager;

        @Nullable
        //@Override
        public View onCreate(@NonNull LayoutInflater inflator, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
            return inflator.inflate(R.layout.activity_results, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            resultsCollectionAdapter = new ResultsCollectionAdapter(this);
            viewPager = view.findViewById(R.id.pager);
            //viewPager.setAdapter(resultsCollectionAdapter);

            TabLayout tabLayout = view.findViewById(R.id.tab_layout);
            //new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText("OBJECT" + (position + 1))).attach();
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

        //@Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            Season ssn = new Season();
            
            TableLayout tableRes = view.findViewById(R.id.table_res);



            Vector<Season.Team> leagueResults = 
                    ssn.resultsLeague.elementAt(x).league_at_week;

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
    }

    public class ResultsSeekBar {

        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            ResultsTableLayout results_table = new ResultsTableLayout();

            results_table.x = 0;

            Season ssn = new Season();

            //Set out seekBar
            SeekBar seekbar = view.findViewById(R.id.seek_bar);
            seekbar.setMax(ssn.resultsLeague.size() - 1);

            seekbar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    results_table.x = progress;
                    results_table.onViewCreated(view, savedInstanceState);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            } );
        }
    }

    public class ResultsGraphView {



        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
            //Declare the three buttons
            Button button_horizontal = view.findViewById(R.id.axis_data_horizontal);
            Button button_vertical = view.findViewById(R.id.axis_data_vertical);
            Button button_line = view.findViewById(R.id.line_choice);

            Context context = getApplicationContext();

            //Declare graph and graph view
            //Graph graph = new Graph.Builder().build();
            GraphView graphView = view.findViewById(R.id.graph_view);

            Season ssn = new Season();

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

                    CharSequence charSeq[] = new CharSequence[ssn.resultsLeague.elementAt(0).league_at_week.size()];
                    boolean checkedItems[] = new boolean[ssn.resultsLeague.elementAt(0).league_at_week.size()];
                    for(int i = 0; i < ssn.resultsLeague.elementAt(0).league_at_week.size(); i++){
                        charSeq[i] = ssn.resultsLeague.elementAt(0).league_at_week.elementAt(i).team_name;
                        checkedItems[i] = true;
                    }

                    builder.setMultiChoiceItems( charSeq, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            //ToDo: edit which lines are shown in graph
                            String teamName = String.valueOf( ssn.resultsLeague.elementAt(0).league_at_week.elementAt(which));

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
                        Vector<Point> newLine = ssn.getPoints(x[0], y[0], setOfTeamNames.elementAt(i));

                        graph = new Graph.Builder().addLineGraph(newLine).build();
                    }

                    graphView.setGraph(graph);
                }
            } );
        }
    }
}
