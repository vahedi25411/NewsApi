package com.example.android.newsapp;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.newsapp.model.NewsItem;
import com.example.android.newsapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String TAG="mainactivity";
    private RecyclerView newsRecyclerView;
    private ProgressBar progress;
    private NewsAdapter newsAdapter;
    private TextView errorMessageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //showDataTextView = (TextView) findViewById(R.id.new .id.news_api_data);
        newsRecyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);

        errorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL, false);
        newsRecyclerView.setLayoutManager(layoutManager);
        newsRecyclerView.setHasFixedSize(true);

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
            loadData();
        }
        return true;
    }

    private void loadData(){

        errorMessageDisplay.setVisibility(View.INVISIBLE);
        newsRecyclerView.setVisibility(View.VISIBLE);
        new FetchDataTask().execute();
    }

    public class FetchDataTask extends AsyncTask<String, Void, ArrayList<NewsItem>> implements NewsAdapter.ItemClickListener{

        ArrayList<NewsItem> data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<NewsItem> doInBackground(String... params) {

            ArrayList<NewsItem> result = null;
            URL url = NetworkUtils.buildUrl();
            Log.d(TAG,"URL :"+url.toString());

            try{
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                result = NetworkUtils.parseJSON(json);
            }catch (IOException e){
                e.printStackTrace();
            }
            catch (JSONException e){
                e.printStackTrace();
            }

            return result;

        }

        @Override
        protected void onPostExecute(ArrayList<NewsItem> newsItems) {
            super.onPostExecute(newsItems);

            this.data = newsItems;
            progress.setVisibility(View.GONE);
            if (newsItems!=null){
                NewsAdapter newsAdapter = new NewsAdapter(newsItems,this);
                newsRecyclerView.setAdapter(newsAdapter);
            }
            else
            {
                newsRecyclerView.setVisibility(View.INVISIBLE);
                errorMessageDisplay.setVisibility(View.VISIBLE);
            }
            //showDataTextView.setText("Sorry, no data was received!");
        }

        @Override
        public void onListItemClick(int clickedItemIndex) {
            String pageUrl = data.get(clickedItemIndex).getUrl();

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pageUrl));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        }

    }
}
