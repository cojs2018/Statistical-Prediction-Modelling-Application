package com.example.statisticalpredictionmodellingapplication;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    /*Test Season.java with the following directories
    @Test
    public void directories_doCompute() {
        Season testSeason = new Season();

        //Test valid pair of directories
        //Test data
        String training_set = "EPL-13-03-2020.arff";
        String test_set = "EPL-MATCHES.arff";
        //testSeason.getArff( training_set, test_set );
        //assertTrue(testSeason.matchResults.size() > 0);

        //Test valid pair in reverse order, should give empty data
        testSeason = new Season();
        //testSeason.getArff( test_set, training_set );
        //assertTrue(testSeason.matchResults.size() == 0);

        //Test each with invalid sets
        String invalid_set = "txt.txt";
        try {
            testSeason = new Season();
            testSeason.getArff( training_set, invalid_set );
        }
        catch(Exception exe) {
            assertTrue(!(exe.getMessage().isEmpty()));
        }

        try {
            testSeason = new Season();
            testSeason.getArff( invalid_set, test_set );
        }
        catch(Exception exe) {
            assertTrue(!(exe.getMessage().isEmpty()));
        }

        //Test each with null set
        String null_set = "";
        try {
            testSeason = new Season();
            testSeason.getArff( training_set, null_set );
        }
        catch(Exception exe) {
            assertSame(exe, new FileNotFoundException());
        }

        try {
            testSeason = new Season();
            testSeason.getArff( null_set, test_set );
        }
        catch(Exception exe) {
            assertSame(exe, new FileNotFoundException());
        }

        //Test invalid set with null set
        try {
            testSeason = new Season();
            testSeason.getArff( null_set, invalid_set );
        }
        catch(Exception exe) {
            assertSame(exe, new FileNotFoundException());
        }
    }*/
}