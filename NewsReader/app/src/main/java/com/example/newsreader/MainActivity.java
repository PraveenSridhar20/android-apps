package com.example.newsreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView);
        textView.setVisibility(View.GONE);
        listView=findViewById(R.id.listView);

        ArrayList<String> titles=new ArrayList<String>();
        final ArrayList<String> jsonURLs=new ArrayList<String>();
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,titles);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(), OpenWebView.class);
                intent.putExtra("url",jsonURLs.get(i));
                startActivity(intent);
            }
        });

        NewsDataDownload newsDataDownload=new NewsDataDownload();
        try {
            String s=newsDataDownload.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();
            JSONArray jsonArray=new JSONArray(s);

            int newsArticles=10;
            for (int i=0;(i<jsonArray.length())&&(i<newsArticles);i++) {
                String jsonURl="https://hacker-news.firebaseio.com/v0/item/"+jsonArray.getString(i)+".json?print=pretty";
                GetJsonFromURL getJsonFromURL=new GetJsonFromURL();
                JSONObject jsonObject=getJsonFromURL.execute(jsonURl).get();
                try {
                    jsonURLs.add(jsonObject.getString("url"));
                    titles.add(jsonObject.getString("title"));
                }
                catch (Exception e){
                    newsArticles++;
                    e.printStackTrace();
                }
                Log.i("Top articles", titles.get(titles.size()-1));
                arrayAdapter.notifyDataSetChanged();
            }


        } catch (Exception e) {
            textView.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }
}