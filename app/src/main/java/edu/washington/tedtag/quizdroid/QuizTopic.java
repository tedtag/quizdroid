package edu.washington.tedtag.quizdroid;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ted on 5/1/2015.
 */
public class QuizTopic implements Serializable {
    private String name;
    private String description;
    private ArrayList<QuizQuestion> questions;

    public QuizTopic () {
        this.name = "";
        this.description = "";
        this.questions = new ArrayList<>();
    }

    public QuizTopic (String name, String description) {
        this.name = name;
        this.description = description;
        this.questions = new ArrayList<>();
    }

    public QuizTopic (String name, String description, ArrayList<QuizQuestion> questions) {
        this.name = name;
        this.description = description;
        this.questions = questions;
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

    public void setQuestions(ArrayList<QuizQuestion> questions) {
        this.questions = questions;
    }

    public ArrayList<QuizQuestion> getQuestions() {
        return this.questions;
    }

    public void addQuestion(QuizQuestion question) {
        this.questions.add(question);
    }
}
