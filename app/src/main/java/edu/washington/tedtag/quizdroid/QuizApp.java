package edu.washington.tedtag.quizdroid;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Ted on 5/12/2015.
 */
public class QuizApp extends Application {

    public interface TopicRepository {
        /* @returns a quiz topic at index i */
        public QuizTopic getTopic(int i);

        /* @returns the array list of topics */
        public ArrayList<QuizTopic> getAllTopic();

        /* @returns a list of topic names */
        public String[] getAllTopicNames();
    }

    public static QuizApp instance = null;

    /* Protect QuizApp at runtime */
    public QuizApp () {
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("Cannot create more than one QuizApp");
        }
    }

}