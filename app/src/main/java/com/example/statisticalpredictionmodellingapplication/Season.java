package com.example.statisticalpredictionmodellingapplication;

import java.util.Vector;

public class Season {

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
    }
}