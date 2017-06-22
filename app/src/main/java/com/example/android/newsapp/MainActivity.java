package com.example.android.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.newsapp.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    static final String TAG="mainactivity";
    private TextView showDataTextView;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showDataTextView = (TextView) findViewById(R.id.news_api_data);
        progress = (ProgressBar) findViewById(R.id.progressBar);

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_refresh){
            showDataTextView.setText("");
            loadData();
        }
        return true;
    }

    private void loadData(){
        new FetchDataTask().execute();
    }

    public class FetchDataTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;
            URL url = NetworkUtils.buildUrl();
            Log.d(TAG,"URL :"+url.toString());

            try{
                result = NetworkUtils.getResponseFromHttpUrl(url);
            }catch (IOException e){
                e.printStackTrace();
            }

            return result;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progress.setVisibility(View.GONE);
            if (s==null){
                showDataTextView.setText("Sorry, no data was received!");
            }
            else
            {
                showDataTextView.setText(s);
            }
        }
    }
}
