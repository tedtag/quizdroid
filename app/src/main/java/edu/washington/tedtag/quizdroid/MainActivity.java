package edu.washington.tedtag.quizdroid;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public String[] topicList = {};
    public QuizTopicCollection quizData = new QuizTopicCollection();

    private DownloadManager dm;
    private long downloadReference;
    private ListView categoryList;
    private PendingIntent alarmIntent = null;
    BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override public void onReceive(final Context c, Intent i) {
            downloadQuizJson(c);
        }
    };

    private void downloadQuizJson (Context c) {
        if(Settings.System.getInt(c.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Airplane mode is on. Launch settings to turn it off?")
                    .setTitle("Airplane mode is on");

            builder.setPositiveButton("Launch", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                }
            });

            builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else if(!isNetworkAvailable()) {
            Toast.makeText(MainActivity.this, "Cannot load questions. No internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            Request request = new Request(
                    Uri.parse(getPreferences(c.MODE_PRIVATE).getString("sync_url", "http://tednewardsandbox.site44.com/questions.json")))
              .setAllowedOverRoaming(false)
              .setTitle("QuizDroid Data Download")
              .setDestinationInExternalFilesDir(MainActivity.this, Environment.DIRECTORY_DOWNLOADS, "questions.json");

            downloadReference = dm.enqueue(request);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int interval = Integer.parseInt(getPreferences(MODE_PRIVATE).getString("sync_frequency", "5"));
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        registerReceiver(alarmReceiver, new IntentFilter("edu.washington.tedtag.QUIZDROID"));
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent();
        i.setAction("edu.washington.tedtag.QUIZDROID");
        alarmIntent = PendingIntent.getBroadcast(this, 0, i, 0);
        am.setRepeating(AlarmManager.RTC,
                System.currentTimeMillis() + interval * 1000,
                interval * 1000, alarmIntent);

        //set filter to only when download is complete and register broadcast receiver
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context c, Intent i) {
            long referenceId = i.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadReference == referenceId) {
                try {
                    // I can't figure out how to access the path to the downloaded file.
                    // Ran out of time so turning this in as-is.
                    quizData.generateFromJSON(c.getResources().openRawResource(R.raw.data));
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "Error: IO Exception", Toast.LENGTH_SHORT).show();
                }

                topicList = quizData.getAllTopicNames();

                categoryList = (ListView) findViewById(R.id.lst_category);
                ArrayAdapter<String> items = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, topicList);
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
        }
    };

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
