package edu.washington.tedtag.quizdroid;

import java.util.ArrayList;

/**
 * Created by Ted on 5/1/2015.
 */
public class QuizQuestion {
    private String question;
    private ArrayList<String[]> choices;
    private String answer;

    public QuizQuestion() {
        this.question = "";
        this.choices = new ArrayList<>();
        this.answer = "";
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setChoices(String[] labels, String[] value) {
        this.choices = new ArrayList<>();
        this.choices.add(labels);
        this.choices.add(value);
    }

    public ArrayList<String[]> getChoices() {
        return this.choices;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return this.answer;
    }
}
