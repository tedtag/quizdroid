package edu.washington.tedtag.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;

public class TopicFragment extends android.support.v4.app.Fragment {
    private QuizTopic thisTopic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // BoilerPlate for most fragment code we create
        View rootView = inflater.inflate(R.layout.fragment_topic, container, false);

        thisTopic = (QuizTopic) getArguments().getSerializable("topic");

        Button begin = (Button) rootView.findViewById(R.id.topic_btn_begin);
        TextView topicName = (TextView) rootView.findViewById(R.id.topic_name);
        TextView topicDescription = (TextView) rootView.findViewById(R.id.topic_description);
        TextView questionCount = (TextView) rootView.findViewById(R.id.topic_question_count);

        topicName.setText("Topic: " + thisTopic.getName());
        topicDescription.setText("Description: " + thisTopic.getDescription());
        questionCount.setText("Number of questions: " + thisTopic.getQuestions().size());

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("topic", thisTopic);
                bundle.putIntArray("statistics", new int[] {0,0});

                QuestionFragment questionFrag = new QuestionFragment();
                questionFrag.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .add(R.id.container, questionFrag)
                        .commit();

                getFragmentManager().beginTransaction().remove(TopicFragment.this).commit();
            }
        });

        return rootView;
    }
}