package edu.washington.tedtag.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public String[] topicList = {};
    public QuizTopicCollection quizData = new QuizTopicCollection();

    private ListView categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            quizData.generateFromJSON(this.getResources().openRawResource(R.raw.data));
        } catch(IOException e){
            Toast.makeText(MainActivity.this, "Error: IO Exception", Toast.LENGTH_SHORT).show();
        }

        topicList = quizData.generateTopicList();

        categoryList = (ListView) findViewById(R.id.lst_category);
        ArrayAdapter<String> items = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topicList);
        categoryList.setAdapter(items);

        categoryList.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Go bring some other activity around the item selected
                Toast.makeText(MainActivity.this, "You selected position " + position + " which is " + topicList[position], Toast.LENGTH_SHORT).show();
                Intent next = new Intent(MainActivity.this, TopicActivity.class);
                // next.putExtra("thisTopic", (String[]) topicDetails.get(position));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
