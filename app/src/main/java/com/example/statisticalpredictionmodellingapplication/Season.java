package com.example.statisticalpredictionmodellingapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.statisticalpredictionmodellingapplication.Kotlin.Feasible;
import com.example.statisticalpredictionmodellingapplication.Kotlin.Matrix;
import com.softmoore.android.graphlib.Point;

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
    public void getData(String time_stamp) throws Exception {

        //Gets data from .arff files

        League newLeague = new League();

        Matrix matrix = new Matrix();

        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase("LeagueData.db", null, SQLiteDatabase.OPEN_READONLY);

        //Get league data
        Cursor leagueCursor = sqLiteDatabase.rawQuery("SELECT * FROM LEAGUE_" + time_stamp + ";",null);
        if(leagueCursor.moveToFirst()) {
            while(leagueCursor.moveToNext()) {
                Team team = new Team();

                team.team_name = leagueCursor.getString(0);
                team.matches_played = leagueCursor.getInt(1);
                team.total_wins = leagueCursor.getInt(2);
                team.total_draws = leagueCursor.getInt(3);
                team.total_loses = leagueCursor.getInt(4);
                team.total_points = leagueCursor.getInt(5);
                team.goals_for = leagueCursor.getInt(6);
                team.goals_against = leagueCursor.getInt(7);
                team.goals_difference = leagueCursor.getInt(8);
                team.points_from_5 = leagueCursor.getInt(9);

                newLeague.league_at_week.add(team);
            }

            //Get match data
            Cursor matchCursor = sqLiteDatabase.rawQuery("SELECT * FROM SCHEDULE_" + time_stamp + ";",null);
            if(matchCursor.moveToFirst()) {
                while(matchCursor.moveToNext()) {
                    if(matchCursor.getInt(4) == 0) { //Match not completed
                        String home_team = matchCursor.getString(0);
                        String away_team = matchCursor.getString(1);
                        long date = matchCursor.getLong(3);

                        Match match = new Match(home_team, away_team, date);
                    }
                }
            }
            else {
                throw new IllegalStateException("No existing data available!");
            }
        }
        else {
            throw new IllegalStateException("No existing data available!");
        }

        /*int pointer = 0;
        while(pointer < tdata.size()) {
            Team team = new Team();
            Instance instance = tdata.get(pointer);

            if(instance.attribute(0).isString()) {
                team.team_name = instance.toString(0);
            }

            team.matches_played = (int) instance.value(1);
            team.total_wins = (int) instance.value(2);
            team.total_draws = (int) instance.value(3);
            team.total_loses = (int) instance.value(4);
            team.total_points = (int) instance.value(5);
            team.goals_for = (int) instance.value(6);
            team.goals_against = (int) instance.value(7);
            team.goals_difference = (int) instance.value(8);
            team.points_from_5 = (int) instance.value(9);

            pointer++;
        }

        //Get data from test set
        csvLoader.setSource(new File(test_set_dir));
        Instances mdata = csvLoader.getDataSet();

        pointer = 0;
        while(pointer < mdata.size()) {
            Instance instance = mdata.get(pointer);

            String home_team = instance.stringValue(0);
            Function<Team, Boolean> getTeam = (t) -> (t.team_name == home_team);
            Team hTeam = extract(newLeague.league_at_week, getTeam);

            int k = 1;
            while(k < instance.numAttributes()) {
                if(!instance.isMissing(k)) {
                    String away_team = instance.stringValue(k);
                    getTeam = (t) -> (t.team_name == away_team);
                    Team aTeam = extract(newLeague.league_at_week, getTeam);

                    Match match = new Match();

                    match.home = hTeam;
                    match.away = aTeam;
                    match.week = k;

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
                k++;
            }
            pointer++;
        }*/

        //Sort match list based on week in ascending order
        BiFunction<Match, Match, Boolean> sort_season_by_week = (Match m0, Match m1) -> (m0.week < m1.week);
        this.matchResults.sort( (Comparator<? super Match>) sort_season_by_week );

        this.completeLeague();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getLeague() {
        resultsLeague = new Vector<>();

        League table0 = new League();

        table0.league_at_week.add(new Team("Liv",29,27,1,1,82,66,21,45,6));
        table0.league_at_week.add(new Team("Mct",28,18,3,7,57,68,31,37,12));
        table0.league_at_week.add(new Team("Lei",29,16,5,8,53,58,28,30,7));
        table0.league_at_week.add(new Team("Che",29,14,6,9,48,51,39,12,10));
        table0.league_at_week.add(new Team("Mtd",29,12,9,8,45,44,30,14,13));
        table0.league_at_week.add(new Team("Wlv",29,10,13,6,43,41,34,7,8));
        table0.league_at_week.add(new Team("Shf",28,11,10,7,43,30,25,5,13));
        table0.league_at_week.add(new Team("Ttm",29,11,8,10,41,47,40,7,2));
        table0.league_at_week.add(new Team("Ars",28,9,13,6,40,40,36,4,12));
        table0.league_at_week.add(new Team("Bnl",29,11,6,12,39,34,40,-6,9));
        table0.league_at_week.add(new Team("Cpl",29,10,9,10,39,26,32,-6,9));
        table0.league_at_week.add(new Team("Evt",29,10,7,12,37,37,46,-9,7));
        table0.league_at_week.add(new Team("Ncl",29,9,8,12,35,25,41,-16,7));
        table0.league_at_week.add(new Team("Spt",29,10,4,15,34,35,52,-17,3));
        table0.league_at_week.add(new Team("Brt",29,6,11,12,29,32,40,-8,4));
        table0.league_at_week.add(new Team("Whm",29,7,6,16,27,35,50,-15,4));
        table0.league_at_week.add(new Team("Wfd",29,6,9,14,27,27,44,-17,4));
        table0.league_at_week.add(new Team("Bmt",29,7,6,16,27,29,47,-18,4));
        table0.league_at_week.add(new Team("Avl",28,7,4,17,25,34,56,-22,0));
        table0.league_at_week.add(new Team("Nrc",29,5,6,18,21,25,52,-27,4));

        resultsLeague.add(table0);

        matchResults = new Vector<>();

       // matchResults.add(new Match("Mct", "Ars", 1));
       // matchResults.add(new Match("Avl", "Shf", 1));

        completeLeague();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void completeLeague() {
        //Computes the predicted results of each match
        Feasible solution = new Feasible();
        Matrix matrix = new Matrix();

        League newTable = this.resultsLeague.lastElement();

        for(Match match: this.matchResults){
            //Create new coefficient vector with all values set to 1
            Matrix vector_coeff_ = matrix.ones(5, 1);

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
            replace(newTable.league_at_week, getTeam, match.home);

            getTeam = (t) -> (t.team_name == match.away.team_name);
            int a = newTable.league_at_week.indexOf(getTeam);
            replace(newTable.league_at_week, getTeam, match.home);

            //Add new entry to results league
            if(match == this.matchResults.lastElement()) {
                this.resultsLeague.add(newTable);
            }
            else {
                if(match.week + 7 <= this.matchResults.elementAt(i + 1).week) { //Update table every 7 days
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

        public League() {
            this.league_at_week = new Vector<>();
        }
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

        public Team(String t_n, int mp, int w, int d, int l, int tp, int gf, int ga, int gd, int p5) {
            this.team_name = t_n;
            this.matches_played = mp;
            this.total_wins = w;
            this.total_draws = d;
            this.total_loses = l;
            this.total_points = tp;
            this.goals_for = gf;
            this.goals_against = ga;
            this.goals_difference = gd;
            this.points_from_5 = p5;
        }

        public Team() {

        }

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

        public long week;

        public Match() {

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public Match(String home_team, String away_team, long k) {
            Function<Team, Boolean> getTeam = (t) -> (t.team_name == home_team);
            Team hTeam = extract(resultsLeague.firstElement().league_at_week, getTeam);

            getTeam = (t) -> (t.team_name == away_team);
            int a = resultsLeague.firstElement().league_at_week.indexOf(getTeam);
            Team aTeam = extract(resultsLeague.firstElement().league_at_week, getTeam);

            this.home = hTeam;
            this.away = aTeam;
            this.week = k;

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

            Matrix matrix = new Matrix();
            this.Tableau = matrix.setMatrix(v, 5);

            v.clear();
            v.add(hTeam.total_points);
            v.add(aTeam.total_points);

            this.vector_tpbm_ = matrix.setMatrix(v, 1);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Team extract(Vector<Team> vector, Function<Team, Boolean> fn) {
        for(int i = 0; i < vector.size(); i++) {
            Team t = vector.elementAt(i);
            if(fn.apply(t)) {
                return t;
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void replace(Vector<Team> vector, Function<Team, Boolean> fn, Team team) {
        for(int i = 0; i < vector.size(); i++) {
            Team t = vector.elementAt(i);
            if(fn.apply(t)) {
                vector.setElementAt(team, i);
            }
        }
    }
}