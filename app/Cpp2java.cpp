//
// Created by User on 31/03/2020.
//


#include "Cpp2java.h"

using namespace ARFF;

public JNIEXPORT void JNICALL cplpl::Java_Loading_call_cpp(JNIEnv *env, jstring training, jstring test)
 { //Gets directories of training and test set from java and computes season

    League newLeague;
    Season newSeason;

    newleague.arff_to_league(training, test);
    newSeason = newLeague.complete_league;

    cplpl c;
    c.season_to_arff(newSeason);
}

public JNIEXPORT void JNICALL cplpl::season_to_display(Season s)
{ //Turns season data into displayable data;

}
