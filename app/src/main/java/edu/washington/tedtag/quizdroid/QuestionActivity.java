package edu.washington.tedtag.quizdroid;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;


public class QuestionActivity extends ActionBarActivity {
    private QuizTopic thisTopic;
    private int[] quizStatistics;
    private Button submit;
    private int selectedId;
    private HashMap<Integer, String> choiceMap = new HashMap<>();
    private HashMap<String, String> reverseMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent launchingIntent = getIntent();
        thisTopic = (QuizTopic) launchingIntent.getSerializableExtra("topic");
        quizStatistics = launchingIntent.getIntArrayExtra("statistics");
        final QuizQuestion thisQuestion = thisTopic.getQuestions().get(0);

        submit = (Button) findViewById(R.id.question_btn_submit);
        TextView questionTitle = (TextView) findViewById(R.id.question_title);
        TextView questionLabel = (TextView) findViewById(R.id.question_label);
        TextView questionText = (TextView) findViewById(R.id.question_text);
        RadioGroup group= (RadioGroup) findViewById(R.id.radio_group);
        RadioButton choiceA = (RadioButton) findViewById(R.id.radio_choice_a);
        RadioButton choiceB = (RadioButton) findViewById(R.id.radio_choice_b);
        RadioButton choiceC = (RadioButton) findViewById(R.id.radio_choice_c);
        RadioButton choiceD = (RadioButton) findViewById(R.id.radio_choice_d);

        questionTitle.setText(thisTopic.getName() + " Quiz");
        questionLabel.setText("Question #" + (quizStatistics[0] + 1));
        questionText.setText(thisQuestion.getQuestion());
        choiceA.setText(thisQuestion.getChoices().get(1)[0]);
        choiceB.setText(thisQuestion.getChoices().get(1)[1]);
        choiceC.setText(thisQuestion.getChoices().get(1)[2]);
        choiceD.setText(thisQuestion.getChoices().get(1)[3]);

        // todo: change method of identifying choices
        // Hack-y way of obtaining the answer from the int ID
        choiceMap.put(choiceA.getId(), "a");
        choiceMap.put(choiceB.getId(), "b");
        choiceMap.put(choiceC.getId(), "c");
        choiceMap.put(choiceD.getId(), "d");
        reverseMap.put("a", (String) choiceA.getText());
        reverseMap.put("b", (String) choiceB.getText());
        reverseMap.put("c", (String) choiceC.getText());
        reverseMap.put("d", (String) choiceD.getText());

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                submit.setEnabled(true);
                selectedId = checkedId;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thisQuestion.getAnswer().equals(choiceMap.get(selectedId))) {
                    quizStatistics[1]++;
                }
                quizStatistics[0]++;

                RadioButton selectedButton = (RadioButton) findViewById(selectedId);
                Intent next = new Intent(QuestionActivity.this, AnswerActivity.class);
                next.putExtra("topic", (Serializable) thisTopic);
                next.putExtra("statistics", quizStatistics);
                next.putExtra("answer", selectedButton.getText());
                next.putExtra("correct", reverseMap.get(thisQuestion.getAnswer()));
                startActivity(next);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
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
