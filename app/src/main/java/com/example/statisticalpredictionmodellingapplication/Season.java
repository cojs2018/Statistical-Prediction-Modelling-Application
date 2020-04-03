package com.example.statisticalpredictionmodellingapplication;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Iterator;
import java.util.Vector;
import java.util.function.Predicate;

import com.google.android.gms.common.util.CollectionUtils;
import com.softmoore.android.graphlib.Point;

public class Season {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Vector<Point> getPoints(int xaxis, int yaxis, String teamstr){
        Vector<Point> toGraph = new Vector<>();

        //search vector, league_at_week for index of tem name

        int n = this.resultsLeague.size();
        for(int i = 0; i < n; i++) {
            final Predicate<Team> thisTeam = new Predicate<Team>() {
                @Override
                public boolean test(Team team) {
                    return team.team_name == teamstr;
                }
            };

            int j = this.resultsLeague.elementAt(i).league_at_week.indexOf(thisTeam);

            Team T = this.resultsLeague.elementAt(i).league_at_week.elementAt(j);

            int x = T.getAxis(xaxis);
            int y = T.getAxis(yaxis);

            Point newPoint = new Point(x, y);

            toGraph.add(newPoint);
        }
        return toGraph;
    }

    public Vector<League> resultsLeague;

    public class League {
        public Vector<Team> league_at_week;
    }

    public class Team {
        public String team_name;
        public int matches_played;
        public int total_wins;
        public int total_draws;
        public int total_loses;
        public int total_points;
        public int goals_for;
        public int goals_against;
        public int goals_difference;
        public int points_from_5;

        public int getAxis(int z) {
            int result = 0;

            switch (z) {
                case 0:
                    result = this.matches_played;
                    break;
                case 1:
                    result = this.total_wins;
                    break;
                case 2:
                    result = this.total_draws;
                    break;
                case 3:
                    result = this.total_loses;
                    break;
                case 4:
                    result = this.total_points;
                    break;
                case 5:
                    result = this.goals_for;
                    break;
                case 6:
                    result = this.goals_against;
                    break;
                case 7:
                    result = this.goals_difference;
                    break;
                case 8:
                    result = this.points_from_5;
                    break;
            }

            return result;
        }

    }
}