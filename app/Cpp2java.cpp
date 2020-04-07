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
    newSeason = newLeague.complete_league();

    cplpl c;
    c.season_to_display(&env, newSeason);
}

public JNIEXPORT Season JNICALL cplpl::season_to_display(JNIEnv *env, Season s)
{ //Turns season data into displayable data;
    JavaVMOption options[1];
    JavaVM *jvm;
    JavaVMInitArgs vm_args;
    long status;
    jclass cls;
    jmethodID mid;

    options[0].OptionString = ".Djava.class.path=.";
    memset(&vm_args, 0, sizeof(vm_args));
    vm_args.version = JNI_VERSION_1_2;
    vm_args.nOptions = 1;
    vm_args.options = options;
    status = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);

    if (status != JNI_ERR) {
        cls = (*env)->FindClass(env, "Season");
        if (cls != 0) {
            return season;
        }
        else {
            throw "ERROR!";
        }
    }
    else{
        throw "ERROR!";
    }

    return null;
}
