package edu.washington.tedtag.quizdroid;

import java.util.ArrayList;

/**
 * Created by Ted on 5/1/2015.
 */
public class QuizTopic {
    private String name;
    private String description;

    public QuizTopic () {
        this.name = "";
        this.description = "";
//        this.questions = new ArrayList<>();
    }

    public QuizTopic (String name, String description) {
        this.name = name;
        this.description = description;
//        this.questions = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
