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

        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            currentName = "";
            currentDescription = "";

            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("Name")) {
                    currentName = reader.nextString();
                } else if (name.equals("Description")) {
                    currentDescription = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            quizTopics.add(new QuizTopic(currentName, currentDescription));
        }
        reader.endArray();

        return quizTopics;
    }
}
