package com.example.statisticalpredictionmodellingapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
//import com.google.android.material.tabs.TabLayoutMediator;

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

        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceStete) {
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
                    results_table.onViewCreated(view, savedInstanceStete);
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
}
