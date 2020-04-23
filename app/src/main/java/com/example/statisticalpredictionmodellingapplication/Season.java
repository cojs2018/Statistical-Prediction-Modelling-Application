package com.example.statisticalpredictionmodellingapplication;

import android.content.Context;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.statisticalpredictionmodellingapplication.Kotlin.Feasible;
import com.example.statisticalpredictionmodellingapplication.Kotlin.Matrix;
import com.softmoore.android.graphlib.Point;

import java.io.File;
import java.io.FileReader;
import java.util.Comparator;
import java.util.Vector;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class Season {

    //Default constructor
    public Season() {
        //Call an empty season
        resultsLeague = new Vector<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getArff(String training_set_dir, String test_set_dir) {

        //Gets data from .arff files
        File file;

        String tr_set = "\\internal storage\\Downloads\\Test\\EPL-13-03-2020.arff";
        String t_set = "\\internal storage\\Downloads\\Test\\EPL-MATCHES.arff";

        League newLeague = new League();

        Matrix matrix = new Matrix();

        try {
            //Get data from training set
            file = new File(tr_set); //training_set_dir);
            FileReader in = new FileReader(file);

            while(in.read() != -1) {
                char c[] = new char[50];
                in.read(c);
                String newLine = "";
                newLine.concat(c.toString());

                if(!(newLine.contains("@") || newLine.contains("%"))) {
                    Team team = new Team();

                    int start = 0;
                    int index = newLine.indexOf(",");
                    team.team_name = newLine.substring(start, index - start);

                    start = index + 1;
                    index = newLine.indexOf(",", start);
                    team.total_wins = Integer.parseInt( newLine.substring(start, index - start) );

                    start = index + 1;
                    index = newLine.indexOf(",", start);
                    team.total_draws = Integer.parseInt( newLine.substring(start, index - start) );

                    start = index + 1;
                    index = newLine.indexOf(",", start);
                    team.total_loses = Integer.parseInt( newLine.substring(start, index - start) );

                    start = index + 1;
                    index = newLine.indexOf(",", start);
                    team.total_points = Integer.parseInt( newLine.substring(start, index - start) );

                    start = index + 1;
                    index = newLine.indexOf(",", start);
                    team.goals_for = Integer.parseInt( newLine.substring(start, index - start) );

                    start = index + 1;
                    index = newLine.indexOf(",", start);
                    team.goals_against = Integer.parseInt( newLine.substring(start, index - start) );

                    start = index + 1;
                    index = newLine.indexOf(",", start);
                    team.goals_difference = Integer.parseInt( newLine.substring(start, index - start) );

                    start = index + 1;
                    index = newLine.indexOf(",", start);
                    team.points_from_5 = Integer.parseInt( newLine.substring(start, index - start) );

                    newLeague.league_at_week.add(team);
                }
            }

            in.close();

            //Get data from test set
            file = new File(t_set); //test_set_dir
            in = new FileReader(file);

            while(in.read() != -1) {
                char c[] = new char[760];
                in.read(c);
                String newLine = "";
                newLine.concat(c.toString());

                if(!(newLine.contains("@") || newLine.contains("%"))) {
                    int week = 0;
                    int start = 0;
                    int index = newLine.indexOf(",");

                    String home_team = newLine.substring(start, index - start);
                    Function<Team, Boolean> getTeam = (t) -> (t.team_name == home_team);
                    int h = newLeague.league_at_week.indexOf(getTeam);
                    Team hTeam = newLeague.league_at_week.elementAt(h);

                    while(start < newLine.length()) {
                        week++;
                        start = index + 1;
                        index = newLine.indexOf(",", start);

                        String away_team = newLine.substring(start, index - start);

                        if(away_team != "?") {
                            getTeam = (t) -> (t.team_name == away_team);
                            int a = newLeague.league_at_week.indexOf(getTeam);
                            Team aTeam = newLeague.league_at_week.elementAt(a);

                            Match match = new Match();

                            match.home = hTeam;
                            match.away = aTeam;
                            match.week = week;

                            Vector<Integer> v = new Vector<>();
                            v.add(hTeam.total_wins);
                            v.add(hTeam.total_draws);
                            v.add(hTeam.total_loses);
                            v.add(hTeam.goals_difference);
                            v.add(hTeam.points_from_5);
                            v.add(aTeam.total_wins);
                            v.add(aTeam.total_draws);
                            v.add(aTeam.total_loses);
                            v.add(aTeam.goals_difference);
                            v.add(aTeam.points_from_5);

                            match.Tableau = matrix.setMatrix(v, 5);

                            v.clear();
                            v.add(hTeam.total_points);
                            v.add(aTeam.total_points);

                            match.vector_tpbm_ = matrix.setMatrix(v, 1);

                            this.matchResults.add(match);
                        }
                    }
                }
            }

            //Sort match list based on week in ascending order
            BiFunction<Match, Match, Boolean> sort_season_by_week = (Match m0, Match m1) -> (m0.week < m1.week);
            this.matchResults.sort( (Comparator<? super Match>) sort_season_by_week );

            this.completeLeague();
        }
        catch (Exception exe) {
            View view = null;
            Context context = view.getContext();
        }
    }

    public void completeLeague() {
        //Computes the predicted results of each match
        Feasible solution = new Feasible();
        Matrix matrix = new Matrix();

        League newTable = this.resultsLeague.lastElement();

        for(Match match: this.matchResults){
            //Create new coefficient vector with all values set to 1
            Matrix vector_coeff_ = matrix.ones(2, 1);

            //Get the result as an optimal solution through linear programming
            match.result = solution.getSolution(vector_coeff_, match.Tableau, match.vector_tpbm_);

            //Process the result
            if(match.result.xvar.get(0, 0) > 1.25 * match.result.xvar.get(1, 0)) {
                //home team wins and gains 3 points, away team loses and gains 0
                //Update teams
                match.home.total_points += 3;
                match.home.total_wins++;
                if (match.home.points_from_5 <= 12)
                    match.home.points_from_5 += 3;

                match.away.total_loses++;
                if (match.away.points_from_5 >= 3)
                    match.away.points_from_5 -= 3;
            }
            else if(match.result.xvar.get(0, 0) < 0.75 * match.result.xvar.get(1, 0)) {
                //home team loses and gains 0 points, away team wains and gains 3
                //Update teams
                match.away.total_points += 3;
                match.away.total_wins++;
                if (match.away.points_from_5 <= 12)
                    match.away.points_from_5 += 3;

                match.home.total_loses++;
                if (match.home.points_from_5 >= 3)
                    match.home.points_from_5 -= 3;
            }
            else {
                //it is a draw, both teams gain 1 point each
                match.home.total_points++;
                match.home.total_draws++;
                if (match.home.points_from_5 > 2) {
                    match.home.points_from_5 -= 2;
                }
                else {
                    match.home.points_from_5++;
                }

                match.away.total_points++;
                match.away.total_draws++;
                if (match.away.points_from_5 > 2) {
                    match.away.points_from_5 -= 2;
                }
                else {
                    match.away.points_from_5++;
                }
            }

            //Update preliminary data in future matches
            int i = this.matchResults.indexOf(match);
            for(int j = i; j < this.matchResults.size(); j++) {
                Match entry = this.matchResults.elementAt(j);

                if(entry.home.team_name == match.home.team_name){
                    this.matchResults.elementAt(j).home = match.home;
                }
                else if(entry.away.team_name == match.home.team_name){
                    this.matchResults.elementAt(j).away = match.home;
                }

                if(entry.home.team_name == match.away.team_name){
                    this.matchResults.elementAt(j).home = match.away;
                }
                else if(entry.away.team_name == match.away.team_name){
                    this.matchResults.elementAt(j).away = match.away;
                }
            }

            //Update League
            Function<Team, Boolean> getTeam = (t) -> (t.team_name == match.home.team_name);
            int h = newTable.league_at_week.indexOf(getTeam);
            newTable.league_at_week.setElementAt(match.home, h);

            getTeam = (t) -> (t.team_name == match.away.team_name);
            int a = newTable.league_at_week.indexOf(getTeam);
            newTable.league_at_week.setElementAt(match.away, a);

            //Add new entry to results league
            if(match == this.matchResults.lastElement()) {
                this.resultsLeague.add(newTable);
            }
            else {
                if(match.week < this.matchResults.elementAt(i + 1).week) {
                    this.resultsLeague.add(newTable);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Vector<Point> getPoints(int xaxis, int yaxis, String teamstr){
        Vector<Point> toGraph = new Vector<>();

        //search vector, league_at_week for index of team name

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

    public Vector<Match> matchResults;

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

    public class Match {
        public Team home;
        public Team away;

        public Matrix Tableau;
        public Matrix vector_tpbm_; //Total points before match

        public Feasible result;

        public int week;
    }

}