//
// Created by User on 30/03/2020.
//

#include <fstream>
#include <iostream>
#include <string>
#include <vector>

#include <Eigen/Dense>
#include "LinProgSol.h"

#ifndef STATISTICALPREDICTIONMODELLINGAPPLICATION_ARFF_DATA_H
#define STATISTICALPREDICTIONMODELLINGAPPLICATION_ARFF_DATA_H

using namespace Eigen;
using namespace Feasible;
using namespace std;

namespace ARFF
{

	League arff_to_league(const char*, const char*);

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

		MatrixXi tableau(2, 6);
		Vector2i tpbm; //total points before match

		int match_day;

		solution result;
	};

	class League
	{
	public:
		vector<Team> all_teams;
		vector<Match> all_matches;

		Season complete_league();
	};

	class Season
	{
	public:
		vector<League> this_season;
	};
}

#endif //STATISTICALPREDICTIONMODELLINGAPPLICATION_ARFF_DATA_H
