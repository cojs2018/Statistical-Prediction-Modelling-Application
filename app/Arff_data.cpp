//
// Created by User on 30/03/2020.
//

#include "Arff_data.h"

#include <algorithm>

using namespace ARFF;
using namespace Eigen;
using namespace Feasible;
using namespace std;

League ARFF::arff_to_league(const char* training, const char* test)
{//Converts arff files to league

	League new_league;

	//Create an input stream
	ifstream input_stream;

	//Lambda function to test if string y is a subsring of string y
	auto str_contains = [](string x, string y) {
		size_t index = x.find(y);
		return (index != string::npos);
	};

	//Connect input_stream to first arff file
	input_stream.open(training, ifstream::in);

	while (input_stream.good()) { //Get list of teams from first file
		string team_line;
		team_line << input_stream.getline;

		if (!str_contains(team_line, "@")) { //If line does not contain an @ or % symbol then this is data
			Team team_in_list;

			size_t start = 0;
			size_t index = team_line.find(',');
			team_in_list.team_name = team_line.substr(start, index - start);

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.total_wins = stoi(team_line.substr(start, index - start));

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.matches_played = stoi(team_line.substr(start, index - start));

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.total_draws = stoi(team_line.substr(start, index - start));

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.total_loses = stoi(team_line.substr(start, index - start));

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.total_points = stoi(team_line.substr(start, index - start));

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.goals_for = stoi(team_line.substr(start, index - start));

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.goals_against = stoi(team_line.substr(start, index - start));

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.goal_difference = stoi(team_line.substr(start, index - start));

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.points_from_5 = stoi(team_line.substr(start, index - start));

			new_league.all_teams.push_back(team_in_list);
		}
	}

	//Close current connection and connect input strueam to second arff file
	input_stream.close();
	input_stream.open(test, ifstream::in);

	while (input_stream.good()) {
		string match_list;
		match_list << input_stream.getline;

		if (!str_contains(match_list, "@")) {
			size_t start = 0;
			size_t index = match_list.find(',');

			int week = 29;
			string home_team = match_list.substr(start, index - start);

			vector<Team>::iterator it_h = find_if(new_league.all_teams.begin(), new_league.all_teams.end(), [&home_team](Team t) {return (t.team_name == home_team); });

			while (index != string::npos) {
				start = index + 1;
				index = match_list.find(',', start);

				string away_team = match_list.substr(start, index - start);

				if (away_team != "?") {
					Match new_match;

					vector<Team>::iterator it_a = find_if(new_league.all_teams.begin(), new_league.all_teams.end(), [&away_team](Team t) {return (t.team_name == away_team); });

					new_match.home = *it_h;
					new_match.away = *it_a;

					//Convert team data into matrix
					vector<int> home_data = { new_match.home.total_wins , new_match.home.total_draws, new_match.home.total_loses, new_match.home.goal_difference, new_match.home.points_from_5 };
					vector<int> away_data = { new_match.away.total_wins , new_match.away.total_draws, new_match.away.total_loses, new_match.away.goal_difference, new_match.away.points_from_5 };

					vector<vector<int>> D = { home_data, away_data };
					new_match.tableau = new Matrix(D);

					vector<int> home_points = { new_match.home.total_points };
					vector<int> away_points = { new_match.away.total_points };

					vector<vector<int>> P = { home_points, away_points };
					new_match.tpbm = new Matrix(P);

					new_match.match_day = week;

					week++;
				}
			}
		}
	}

	//Finally sort all_matches in ascending order of match_day
	auto day_sort = [](Match m1, Match m2) {return (m1.match_day < m2.match_day); };
	sort_heap(new_league.all_matches.begin(), new_league.all_matches.end(), day_sort);

	//Return league
	return new_league;
}

Season ARFF::League::complete_league()
{//Computes the statistical prediction of the result of each match

	Match mvar0;

	Season year;

	program prog;

	for (Match mvar : this->all_matches) {

		if (mvar0.match_day < mvar.match_day) { //ToDo: create method of saving league table after each week
			year.this_season.push_back(this);
		}

		//Create new coefficent matrix with value of 1 for each team
		Vector2i coeff;
		coeff << 1,
		         1;

		//Get the result as a feasible solution through linear programming
		mvar.result = prog.linear(coeff, tableau, tpbm);

		//Process the result
		solution res = mvar.result;

		if (res.X.at(0, 0) > 1.25 * res.X.at(1, 0)) { //home team wins and gains 3 points, away team loses and gains 0
			//Update teams
			mvar.home.total_points += 3;
			mvar.home.total_wins++;
			if (mvar.home.points_from_5 <= 12)
				mvar.home.points_from_5 += 3;

			mvar.away.total_loses++;
			if (mvar.away.points_from_5 >= 3)
				mvar.away.points_from_5 -= 3;
		}
		else if (res.X.at(0.0) < 0.75 * res.X.at(1, 0)) { //home team loses and gains 0 points, away team wains and gains 3
			//Update teams
			mvar.away.total_points += 3;
			mvar.away.total_wins++;
			if (mvar.away.points_from_5 <= 12)
				mvar.away.points_from_5 += 3;

			mvar.home.total_loses++;
			if (mvar.home.points_from_5 >= 3)
				mvar.home.points_from_5 -= 3;
		}
		else { //it is a draw, both teams gain 1 point each
			mvar.home.total_points++;
			mvar.home.total_draws++;
			if (mvar.home.points_from_5 > 2) {
				mvar.home.points_from_5 -= 2;
			}
			else {
				mvar.home.points_from_5++;
			}

			mvar.away.total_points++;
			mvar.away.total_draws++;
			if (mvar.away.points_from_5 > 2) {
				mvar.away.points_from_5 -= 2;
			}
			else {
				mvar.away.points_from_5++;
			}

			//Update league
			for (Team t : this->all_teams) {
				if (t.team_name == mvar.home.team_name)
					t = mvar.home;
				else if (t.team_name == mvar.away.team_name)
					t = mvar.away;
			}

			//Update all other matches
			for (Match m : this->all_matches) {
				if (m.home.team_name == mvar.home.team_name)
					m.home = mvar.home;
				else if (m.away.team_name == mvar.home.team_name)
					m.away = mvar.home;

				if (m.home.team_name == mvar.away.team_name)
					m.home = mvar.away;
				else if (m.away.team_name == mvar.away.team_name)
					m.away = mvar.away;
			}
		}

		mvar0 = mvar;

		this->all_matches.erase(this->all_matches.begin());
	}

	return year;

}

