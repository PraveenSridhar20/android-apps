package com.example.guessthehero;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownload extends AsyncTask<String,Void,Bitmap> {
    @Override
    protected Bitmap doInBackground(String... strings){
        Bitmap bitmap=null;
        URL url=null;
        HttpURLConnection urlConnection;
        try{
            url=new URL(strings[0]);
            urlConnection=(HttpURLConnection)url.openConnection();
            //urlConnection.connect();
            InputStream inputStream=urlConnection.getInputStream();
            bitmap= BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
