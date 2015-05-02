package edu.washington.tedtag.quizdroid;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Ted on 5/1/2015.
 */
public class QuizTopicCollection {
    private ArrayList<QuizTopic> topics;

    public QuizTopicCollection () {
        this.topics = new ArrayList<>();
    }

    public QuizTopicCollection (ArrayList<QuizTopic> t) {
        // TODO: Add error checking for passed topics
        this.topics = new ArrayList<>(t);
    }

    /* @returns a quiz topic at index i */
    public QuizTopic getTopic(int i) {
        return this.topics.get(i);
    }

    /* @returns a list of topic names */
    public String[] generateTopicList () {
        String[] topics = new String[this.topics.size()];

        for (int i = 0; i < this.topics.size(); i++) {
            topics[i] = this.topics.get(i).getName();
        }

        return topics;
    }

    /* Sets the list of topics through an external, structured JSON file
     * @param InputStream is: input stream of raw JSON file */
    public void generateFromJSON (InputStream is) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
        try {
            reader.beginObject();
            String name = reader.nextName();
            if (name.equals("Data")) {
                this.topics = generateTopicsFromJSONArray(reader);
            }
            reader.endObject();
        } finally{
            reader.close();
        }
    }

    private ArrayList<QuizTopic> generateTopicsFromJSONArray (JsonReader reader) throws IOException {
        ArrayList<QuizTopic> quizTopics = new ArrayList<>();
        String currentName;
        String currentDescription;
        ArrayList<QuizQuestion> currentQuestions;

        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            currentName = "";
            currentDescription = "";
            currentQuestions = new ArrayList<QuizQuestion>();

            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("Name")) {
                    currentName = reader.nextString();
                } else if (name.equals("Description")) {
                    currentDescription = reader.nextString();
                } else if (name.equals("Questions")) {
                    currentQuestions = generateQuestionsFromJSONArray(reader);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            quizTopics.add(new QuizTopic(currentName, currentDescription, currentQuestions));
        }
        reader.endArray();

        return quizTopics;
    }

    private ArrayList<QuizQuestion> generateQuestionsFromJSONArray (JsonReader reader) throws IOException {
        ArrayList<QuizQuestion> questionList = new ArrayList<>();
        String currentQuestion;
        ArrayList<String[]> currentChoices;
        String currentAnswer;

        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            currentQuestion = "";
            currentChoices = new ArrayList<String[]>();
            currentAnswer = "";

            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("Question")) {
                    currentQuestion = reader.nextString();
                } else if (name.equals("Choices")) {
                    // todo: figure out how to dynamically add choices
                    String[] labels = {"a", "b", "c", "d"};
                    String[] values = {"", "", "", ""};

                    reader.beginObject();
                    while (reader.hasNext()) {
                        String choice = reader.nextName();
                        if (choice.equals("a")) {
                            values[0] = reader.nextString();
                        } else if (choice.equals("b")) {
                            values[1] = reader.nextString();
                        } else if (choice.equals("c")) {
                            values[2] = reader.nextString();
                        } else if (choice.equals("d")) {
                            values[3] = reader.nextString();
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                    currentChoices.add(labels);
                    currentChoices.add(values);
                } else if (name.equals("Answer")) {
                    currentAnswer = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            questionList.add(new QuizQuestion(currentQuestion, currentChoices, currentAnswer));
        }
        reader.endArray();

        return questionList;
    }
}
