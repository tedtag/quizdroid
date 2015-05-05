package edu.washington.tedtag.quizdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.HashMap;

public class QuestionFragment extends Fragment {
    private QuizTopic thisTopic;
    private int[] quizStatistics;
    private Button submit;
    private int selectedId;
    private HashMap<Integer, String> choiceMap = new HashMap<>();
    private HashMap<String, String> reverseMap = new HashMap<>();

    public QuestionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // BoilerPlate for most fragment code we create
        final View rootView = inflater.inflate(R.layout.fragment_question, container, false);

        thisTopic = (QuizTopic) getArguments().getSerializable("topic");
        quizStatistics = getArguments().getIntArray("statistics");
        final QuizQuestion thisQuestion = thisTopic.getQuestions().get(0);

        submit = (Button) rootView.findViewById(R.id.question_btn_submit);
        TextView questionTitle = (TextView) rootView.findViewById(R.id.question_title);
        TextView questionLabel = (TextView) rootView.findViewById(R.id.question_label);
        TextView questionText = (TextView) rootView.findViewById(R.id.question_text);
        RadioGroup group= (RadioGroup) rootView.findViewById(R.id.radio_group);
        RadioButton choiceA = (RadioButton) rootView.findViewById(R.id.radio_choice_a);
        RadioButton choiceB = (RadioButton) rootView.findViewById(R.id.radio_choice_b);
        RadioButton choiceC = (RadioButton) rootView.findViewById(R.id.radio_choice_c);
        RadioButton choiceD = (RadioButton) rootView.findViewById(R.id.radio_choice_d);

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
                RadioButton selectedButton = (RadioButton) rootView.findViewById(selectedId);

                Bundle bundle = new Bundle();
                bundle.putSerializable("topic", thisTopic);
                bundle.putIntArray("statistics", quizStatistics);
                bundle.putString("answer", (String) selectedButton.getText());
                bundle.putString("correct", reverseMap.get(thisQuestion.getAnswer()));

                AnswerFragment answerFrag = new AnswerFragment();
                answerFrag.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .add(R.id.container, answerFrag)
                        .commit();

                getFragmentManager().beginTransaction().remove(QuestionFragment.this).commit();
            }
        });

        return rootView;
    }
}