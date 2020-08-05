package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    TextView textView;
    URL url;
    HttpURLConnection httpURLConnection;
    JSONObject city;
    JSONArray weatherJSON;
    String jsonString="";
    boolean inCorrectCity=false;

    public class DownloadJSONData extends AsyncTask<String,Void,String>{
        public String doInBackground(String... urls){
            try {
                String result="";
                jsonString="";
                url=new URL(urls[0]);
                httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line=bufferedReader.readLine())!=null){
                    jsonString+=line+'\n';
                }
                Log.i("JSON String",jsonString);
                city=new JSONObject(jsonString);
                weatherJSON=new JSONArray(city.getString("weather"));
                for (int i=0;i<weatherJSON.length();i++){
                    result+=weatherJSON.getJSONObject(i).getString("main")+": "+weatherJSON.getJSONObject(i).getString("description")+"\n";
                }
                Log.i("Here is what you are looking for",result);
                return result;

            }
            catch (FileNotFoundException e){
                e.printStackTrace();
                inCorrectCity=true;

            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public class IncorrectCityException extends Exception{
        int id;
        public IncorrectCityException(){
            id=0;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);
        editText=findViewById(R.id.editText);
        textView=findViewById(R.id.textView2);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (editText.getText().toString().length()==0){
                        Toast.makeText(MainActivity.this, "Enter a city", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        DownloadJSONData downloadJSONData = new DownloadJSONData();
                        Log.i("The data as seen", editText.getText().toString());
                        String encodedCityName= URLEncoder.encode(editText.getText().toString(),"UTF-8");
                        String res = downloadJSONData.execute("https://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=439d4b804bc8187953eb36d2a8c26a02").get();
                        if (inCorrectCity){
                            inCorrectCity=false;
                            throw new IncorrectCityException();
                        }
                        if (res==null)
                            throw new Exception();

                        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
                        textView.setText(res);
                    }

                }
                catch (IncorrectCityException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Check the entered city", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Check your network", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}