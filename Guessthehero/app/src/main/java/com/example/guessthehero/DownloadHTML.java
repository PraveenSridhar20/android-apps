package com.example.guessthehero;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadHTML extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... urls) {
        URL url;
        String data="";
        HttpURLConnection httpURLConnection;
        try{
            url=new URL(urls[0]);
            httpURLConnection=(HttpURLConnection)url.openConnection();
            //httpURLConnection.connect();
            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
            String line=null;
            while ((line=reader.readLine())!=null){
                data+=line+"\n";
            }
            inputStream.close();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
