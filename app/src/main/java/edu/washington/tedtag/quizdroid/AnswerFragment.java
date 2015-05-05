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

public class AnswerFragment extends Fragment {
    private QuizTopic thisTopic;
    private int[] quizStatistics;

    public AnswerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // BoilerPlate for most fragment code we create
        final View rootView = inflater.inflate(R.layout.fragment_answer, container, false);

        thisTopic = (QuizTopic) getArguments().getSerializable("topic");
        quizStatistics = getArguments().getIntArray("statistics");
        String answer = getArguments().getString("answer");
        String correct = getArguments().getString("correct");

        Button btnAnswer = (Button) rootView.findViewById(R.id.answer_btn);
        TextView answerSubmitted = (TextView) rootView.findViewById(R.id.answer_submitted);
        TextView answerCorrect = (TextView) rootView.findViewById(R.id.answer_correct);
        TextView answerScore = (TextView) rootView.findViewById(R.id.answer_score);

        answerSubmitted.setText("You selected " + answer);
        answerCorrect.setText("The correct answer was " + correct);
        answerScore.setText("You have " + quizStatistics[1] + " out of " + quizStatistics[0] + " correct");

        if(thisTopic.getQuestions().size() == 1) {
            btnAnswer.setText("Finish");
            btnAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent next = new Intent(getActivity(), MainActivity.class);
                    startActivity(next);
                    getActivity().finish();
                }
            });
        } else {
            thisTopic.getQuestions().remove(0);
            btnAnswer.setText("Next");
            btnAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("topic", thisTopic);
                    bundle.putIntArray("statistics", quizStatistics);

                    QuestionFragment questionFrag = new QuestionFragment();
                    questionFrag.setArguments(bundle);

                    getFragmentManager().beginTransaction()
                            .setCustomAnimations( R.anim.slide_in_right, 0, 0, R.anim.slide_out_left)
                            .add(R.id.container, questionFrag)
                            .commit();

                    getFragmentManager().beginTransaction().remove(AnswerFragment.this).commit();
                }
            });
        }

        return rootView;
    }
}