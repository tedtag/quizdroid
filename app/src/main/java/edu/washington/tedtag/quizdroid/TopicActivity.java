package edu.washington.tedtag.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.Serializable;


public class TopicActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Intent launchingIntent = getIntent();
        QuizTopic thisTopic = (QuizTopic) launchingIntent.getSerializableExtra("topic");

        TextView topicName = (TextView) findViewById(R.id.topic_name);
        TextView topicDescription = (TextView) findViewById(R.id.topic_description);
        TextView questionCount = (TextView) findViewById(R.id.topic_question_count);

        topicName.setText("Topic: " + thisTopic.getName());
        topicDescription.setText("Description: " + thisTopic.getDescription());
        questionCount.setText("Number of questions: " + thisTopic.getQuestions().size());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
