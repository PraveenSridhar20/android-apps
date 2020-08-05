package com.example.guessthehero;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    String htmlStr="";
    ArrayList<String> imageUrls;//indexing 1
    ArrayList<String> heroNames;//indexing 1
    Pattern pattern;
    Matcher matcher;
    ArrayList<Button> buttons;
    ArrayList<Integer> arrayList;
    Button retry;
    DownloadHTML downloadHTML;
    ImageDownload imageDownload;
    int corrLoc=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=findViewById(R.id.imageView);
        downloadHTML=new DownloadHTML();
        imageUrls=new ArrayList<String>();
        heroNames=new ArrayList<String>();
        retry=findViewById(R.id.retryconn);
        buttons=new ArrayList<Button>();
        arrayList=new ArrayList<Integer>();

        arrayList.add(0);
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);


        buttons.add((Button) findViewById(R.id.button));
        buttons.add((Button) findViewById(R.id.button2));
        buttons.add((Button) findViewById(R.id.button3));
        buttons.add((Button) findViewById(R.id.button4));

        //retry connection if it fails at the start of the app
        try {
            retry.setVisibility(View.GONE);
            htmlStr=downloadHTML.execute("https://comicvine.gamespot.com/profile/neonpheonix/lists/top-100-superheroes/58789/").get();
            if (htmlStr==null){
                throw new Exception();
            }
            pattern=Pattern.compile("<img src=\"(.*?)\" />");
            matcher=pattern.matcher(htmlStr);

            while(matcher.find()){
                imageUrls.add(matcher.group(1));
                Log.i("tags",imageUrls.get(imageUrls.size()-1));
            }

            pattern=Pattern.compile("<h3>(\\d+). (.*?)</h3>");
            matcher=pattern.matcher(htmlStr);

            while(matcher.find()){
                heroNames.add(matcher.group(2));
                if (heroNames.size()==29)
                    heroNames.set(28,"April O'Neil");
                Log.i("tags",heroNames.get(heroNames.size()-1));
            }

            imageDownload=new ImageDownload();
            Bitmap bitmap1= null;
            Collections.shuffle(arrayList);

            Random random=new Random();
            int i=random.nextInt(100)+1;
            bitmap1 = imageDownload.execute(imageUrls.get(i)).get();
            if (bitmap1==null){
                i--;
                throw new Exception();
            }

            for (int j=0;j<4;j++) {
                buttons.get(arrayList.get(j)).setText(heroNames.get(i - 1));
                if (j==0) {
                    buttons.get(arrayList.get(j)).setTag("correct");
                    corrLoc=arrayList.get(j);
                }
                else
                    buttons.get(arrayList.get(j)).setTag("incorrect");
                i=random.nextInt(100)+1;
            }
            imageView.setImageBitmap(bitmap1);

        }
        catch(Exception e){
            retry.setVisibility(View.VISIBLE);
            e.printStackTrace();
            Toast.makeText(this, "Retry connection", Toast.LENGTH_SHORT).show();
            for (int j=0;j<4;j++)
                buttons.get(j).setVisibility(View.GONE);
        }


        //in case connection fails when retrying to connect again at the start of the app
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    retry.setVisibility(View.GONE);
                    downloadHTML=new DownloadHTML();
                    htmlStr=downloadHTML.execute("https://comicvine.gamespot.com/profile/neonpheonix/lists/top-100-superheroes/58789/").get();
                    if (htmlStr==null) {
                        throw new Exception();
                    }
                    pattern=Pattern.compile("<img src=\"(.*?)\" />");
                    matcher=pattern.matcher(htmlStr);

                    while(matcher.find()){
                        imageUrls.add(matcher.group(1));
                        Log.i("tags",imageUrls.get(imageUrls.size()-1));
                    }

                    pattern=Pattern.compile("<h3>(\\d+). (.*?)</h3>");
                    matcher=pattern.matcher(htmlStr);

                    while(matcher.find()){
                        heroNames.add(matcher.group(2));
                        Log.i("tags",heroNames.get(heroNames.size()-1));
                    }

                    imageDownload=new ImageDownload();
                    Bitmap bitmap1= null;
                    Collections.shuffle(arrayList);

                    Random random=new Random();
                    int i=random.nextInt(100)+1;
                    bitmap1 = imageDownload.execute(imageUrls.get(i)).get();
                    if (bitmap1==null){
                        i--;
                        throw new Exception();
                    }

                    for (int j=0;j<4;j++) {
                        buttons.get(arrayList.get(j)).setText(heroNames.get(i - 1));
                        if (j==0) {
                            buttons.get(arrayList.get(j)).setTag("correct");
                            corrLoc=arrayList.get(j);
                        }
                        else
                            buttons.get(arrayList.get(j)).setTag("incorrect");
                        i=random.nextInt(100)+1;
                    }
                    imageView.setImageBitmap(bitmap1);
                    for (int j=0;j<4;j++)
                        buttons.get(j).setVisibility(View.VISIBLE);

                }
                catch(Exception e){
                    retry.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Retry connection", Toast.LENGTH_SHORT).show();
                    for (int j=0;j<4;j++)
                        buttons.get(j).setVisibility(View.GONE);
                }
            }
        });

    }

    //button click listener for options
    public void onClick(View view){

        imageDownload=new ImageDownload();
        Bitmap bitmap1= null;
        Collections.shuffle(arrayList);

        try {
            Random random=new Random();
            int i=random.nextInt(100)+1;
            int corr=i;
            bitmap1 = imageDownload.execute(imageUrls.get(i)).get();
            if (bitmap1==null){
                i--;
                throw new Exception();
            }
            if ((view.getTag()).equals("correct"))
                Toast.makeText(MainActivity.this, "Correct", Toast.LENGTH_SHORT).show();
            else if ((view.getTag().equals("incorrect")))
                Toast.makeText(this, "Incorrect! The answer was "+buttons.get(corrLoc).getText(), Toast.LENGTH_SHORT).show();

            for (int j=0;j<4;j++) {
                buttons.get(arrayList.get(j)).setText(heroNames.get(i - 1));
                if (j==0) {
                    buttons.get(arrayList.get(j)).setTag("correct");
                    corrLoc=arrayList.get(j);
                }
                else
                    buttons.get(arrayList.get(j)).setTag("incorrect");
                i=random.nextInt(100)+1;
                while(i==corr){
                    i=random.nextInt(100)+1;
                }
            }
            imageView.setImageBitmap(bitmap1);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Image Download Failed\nPlease check your internet", Toast.LENGTH_SHORT).show();
        }
    }

}