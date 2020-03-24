
#include <algorithm>

#include "ARFFtoData.h"

using namespace ARFF;
using namespace MATH;
using namespace std;

League arff_to_league()
{ //Converts arff files to league

	League new_league;
	
	//Create an input stream
	ifstream input_stream;

	//Get location of first arff file (PLEASE NOTE: file used in this case is a test file)
	const char* training_file = "...//Statistical-Prediction-Modelling-File//EPL-13-03-2020";
	
	//Get location of second arff file containing list of future matches (PLEASE NOTE: file used in this case is a test file)
	const char* match_file = "...//Statistical-Prediction-Modelling-File//EPL-Matches";

	//Lambda function to test if string y is a subsring of string y
	auto str_contains = [](string x, string y) {
		size_t index = x.find(y);
		return (index != string::npos);
	};

	//Connect input_stream to first arff file
	input_stream.open(training_file, ifstream::in);

	while (input_stream.good()) { //Get list of teams from first file
		string team_line;
		team_line << input_stream;

		if (!str_contains(team_line, "@")) { //If line does not contain an @ or % symbol then this is data
			Team team_in_list;

			size_t start = 0;
			size_t index = team_line.find(',');
			team_in_list.team_name = team_line.substr(start, index - start);

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.total_wins = team_line.substr(start, index - start);

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.matches_played = team_line.substr(start, index - start);

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.total_draws = team_line.substr(start, index - start);

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.total_loses = team_line.substr(start, index - start);

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.total_points = team_line.substr(start, index - start);

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.goals_for = team_line.substr(start, index - start);

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.goals_against = team_line.substr(start, index - start);

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.goal_difference = team_line.substr(start, index - start);

			start = index + 1;
			index = team_line.find(',', start);
			team_in_list.points_from_5 = team_line.substr(start, index - start);

			new_league.all_teams.push_back(team_in_list);
		}
	}

	//Close current connection and connect input strueam to second arff file
	input_stream.close();
	input_stream.open(match_file, ifstream::in);

	while (input_stream.good()) {
		string match_list;
		match_list << input_stream;

		if (!str_contains(match_list, "@")) {
			size_t start = 0;
			size_t index = match_list.find(',');

			int week = 29;
			string home_team = match_list.substr(start, index - start);

			vector<Team>::iterator it_h = find_if(new_league.all_teams.begin(), new_league.all_teams(), [&home_team](const Team& t) {return (t.team_name == home_team); });

			while (index != string::npos) {
				start = index + 1;
				index = match_list.find(',', start);

				string away_team = match_list.substr(start, index - start);

				if (away_team != "?") {
					Match new_match;

					vector<Team>::iterator it_a = find_if(new_league.all_teams.begin(), new_league.all_teams(), [&away_team](const Team& t) {return (t.team_name == away_team); });

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
	sort(new_league.all_matches.begin(), new_league.all_matches.end(), day_sort);
}