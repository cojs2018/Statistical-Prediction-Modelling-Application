package com.example.statisticalpredictionmodellingapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayoutMediator;

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
        @Override
        public View onCreate(@NonNull LayoutInflater inflator, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflator.inflate(R.layout.activity_results, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            resultsCollectionAdapter = new ResultsCollectionAdapter(this);
            viewPager = view.findViewById(R.id.pager);
            viewPager.setAdapter(resultsCollectionAdapter);

            TableLayout tabLayout = view.findViewById(R.id.tab_layout);
            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText("OBJECT" + (position + 1))).attach();
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
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_collection_object, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            Bundle args = getArguments();
            ((TextView) view.findViewById(android.R.id.text1)).setText(Integer.toString(args.getInt(ARG_OBJECT)));
        }
    }
}
