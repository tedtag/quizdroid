package edu.washington.tedtag.quizdroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public String[] topicList = {};
    public QuizTopicCollection quizData = new QuizTopicCollection();

    private ListView categoryList;
    private PendingIntent alarmIntent = null;
    BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override public void onReceive(final Context c, Intent i) {
            Toast.makeText(MainActivity.this, getPreferences(c.MODE_PRIVATE).getString("sync_url", "http://tednewardsandbox.site44.com/questions.json"), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int interval = Integer.parseInt(getPreferences(MODE_PRIVATE).getString("sync_frequency", "5"));

        registerReceiver(alarmReceiver, new IntentFilter("edu.washington.tedtag.QUIZDROID"));
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent();
        i.setAction("edu.washington.tedtag.QUIZDROID");
        alarmIntent = PendingIntent.getBroadcast(this, 0, i, 0);
        am.setRepeating(AlarmManager.RTC,
                System.currentTimeMillis() + interval * 1000 * 60,
                interval * 1000 * 60, alarmIntent);

        try {
            quizData.generateFromJSON(this.getResources().openRawResource(R.raw.data));
        } catch(IOException e){
            Toast.makeText(MainActivity.this, "Error: IO Exception", Toast.LENGTH_SHORT).show();
        }

        topicList = quizData.getAllTopicNames();

        categoryList = (ListView) findViewById(R.id.lst_category);
        ArrayAdapter<String> items = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topicList);
        categoryList.setAdapter(items);

        categoryList.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Go bring some other activity around the item selected
                Toast.makeText(MainActivity.this, "You selected " + topicList[position], Toast.LENGTH_SHORT).show();
                Intent next = new Intent(MainActivity.this, TopicActivity.class);
                next.putExtra("topic", (Serializable) quizData.getTopic(position));
                startActivity(next);
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
            Intent next = new Intent(MainActivity.this, PreferencesActivity.class);
            startActivity(next);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
