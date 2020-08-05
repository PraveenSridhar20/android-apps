package com.example.newsreader;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetJsonFromURL extends AsyncTask<String,Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            URL url=new URL(strings[0]);
            HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
            InputStream inputStream=urlConnection.getInputStream();
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
            String s;
            String res="";
            while((s=bufferedReader.readLine())!=null){
                res+=s+"\n";
            }
            JSONObject jsonObject=new JSONObject(res);
            return jsonObject;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
