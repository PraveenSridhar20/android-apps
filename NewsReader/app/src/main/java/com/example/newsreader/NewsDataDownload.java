package com.example.newsreader;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsDataDownload extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... strings) {
        try{
            URL url=new URL(strings[0]);
            HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
            InputStream inputStream=urlConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line;
            String result="";
            while ((line=bufferedReader.readLine())!=null){
                result+=line+"\n";
            }

            return result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
