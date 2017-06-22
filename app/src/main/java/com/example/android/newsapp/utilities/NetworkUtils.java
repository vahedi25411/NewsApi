package com.example.android.newsapp.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Vahedi on 6/20/17.
 */

public final class NetworkUtils {

    private static final String NEWS_BASE_URL = "https://newsapi.org/v1/articles";

    private static final String SOURCE = "the-next-web";
    private static final String SORT_BY = "latest";
    private static final String API_KEY = "";//API Key goes here

    final static String SOURCE_PARAM = "source";
    final static String SORT_PARAM = "sortBy";
    final static String API_KEY_PARAM = "apiKey";

    public static URL buildUrl(){
        Uri builtUri = Uri.parse(NEWS_BASE_URL).buildUpon()
                .appendQueryParameter(SOURCE_PARAM, SOURCE)
                .appendQueryParameter(SORT_PARAM, SORT_BY)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);

            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }
            else {
                return null;
            }

        }
        finally{
            urlConnection.disconnect();
        }
    }

}
