//
// Created by User on 31/03/2020.
//

#include <jni.h>
#include "Arff_data.h"

#ifndef STATISTICALPREDICTIONMODELLINGAPPLICATION_CPP2JAVA_H
#define STATISTICALPREDICTIONMODELLINGAPPLICATION_CPP2JAVA_H

class cplpl
{
public:
    JNIEXPORT void JNICALL Java_Loading_call_cpp(JNIENV *, jstring, jstring);

    JNIEXPORT Season JNICALL season_to_display(JNIENV *, Season);
}

#endif //STATISTICALPREDICTIONMODELLINGAPPLICATION_CPP2JAVA_H
