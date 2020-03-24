#pragma once

#include <fstream>
#include <iostream>
#include <string>
#include <vector>

#include "Matrix.h";

using namespace std;

namespace ARFF
{

	League arff_to_league();

	struct Team
	{
		string team_name;
		int matches_played;
		int total_wins;
		int total_draws;
		int total_loses;
		int total_points;
		int goals_for;
		int goals_against;
		int goal_difference;
		int points_from_5;

		string rank;
		bool banned;
	};

	class Match
	{
	public:
		Team home;
		Team away;

		Matrix tableau;
		Matrix tpbm; //total points before match

		int match_day;

		feasible_solution get_result();
	};

	class League
	{
	public:
		vector<Team> all_teams;
		vector<Match> all_matches;
	};
}