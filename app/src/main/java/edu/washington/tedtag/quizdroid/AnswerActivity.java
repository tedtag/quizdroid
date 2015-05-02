package edu.washington.tedtag.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.HashMap;


public class AnswerActivity extends ActionBarActivity {
    private QuizTopic thisTopic;
    private int[] quizStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Intent launchingIntent = getIntent();
        thisTopic = (QuizTopic) launchingIntent.getSerializableExtra("topic");
        quizStatistics = launchingIntent.getIntArrayExtra("statistics");
        String answer = launchingIntent.getStringExtra("answer");
        String correct = launchingIntent.getStringExtra("correct");

        Button btnAnswer = (Button) findViewById(R.id.answer_btn);
        TextView answerSubmitted = (TextView) findViewById(R.id.answer_submitted);
        TextView answerCorrect = (TextView) findViewById(R.id.answer_correct);
        TextView answerScore = (TextView) findViewById(R.id.answer_score);

        answerSubmitted.setText("You selected " + answer);
        answerCorrect.setText("The correct answer was " + correct);
        answerScore.setText("You have " + quizStatistics[1] + " out of " + quizStatistics[0] + " correct");

        if(thisTopic.getQuestions().size() == 1) {
            btnAnswer.setText("Finish");
            btnAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent next = new Intent(AnswerActivity.this, MainActivity.class);
                    startActivity(next);
                    finish();
                }
            });
        } else {
            thisTopic.getQuestions().remove(0);
            btnAnswer.setText("Next");
            btnAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent next = new Intent(AnswerActivity.this, QuestionActivity.class);
                    next.putExtra("topic", (Serializable) thisTopic);
                    next.putExtra("statistics", quizStatistics);
                    startActivity(next);
                    finish();
                }
            });
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answer, menu);
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
