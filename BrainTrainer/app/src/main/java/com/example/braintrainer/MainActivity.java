package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String ops="+*";
    TextView question;
    Button changeQues;
    int op,val1,val2;
    Random random;
    ArrayList<Integer> arrayList;
    ArrayList<Integer> options;
    ArrayList<TextView> textViewArrayList;
    CountDownTimer countDownTimer;
    TextView timeView;
    TextView scoreView;
    boolean gameRunning=false;
    TextView res;
    int correct=0;
    int total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random=new Random();
        question=findViewById(R.id.textView17);
        changeQues=findViewById(R.id.startButton);
        arrayList=new ArrayList<Integer>();
        options=new ArrayList<Integer>();
        textViewArrayList=new ArrayList<TextView>();
        timeView=findViewById(R.id.timeView);
        scoreView=findViewById(R.id.scoreView);
        res=findViewById(R.id.result);

        countDownTimer=new CountDownTimer(31000,1000) {
            @Override
            public void onTick(long l) {
                l/=1000;
                //l++;
                timeView.setText(""+l);
            }

            @Override
            public void onFinish() {
                changeQues.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Time Up", Toast.LENGTH_SHORT).show();
                gameRunning=false;
            }
        };


        arrayList.add(0);
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);

        options.add(0);
        options.add(0);
        options.add(0);
        options.add(0);

        textViewArrayList.add((TextView)findViewById(R.id.textView11));
        textViewArrayList.add((TextView)findViewById(R.id.textView12));
        textViewArrayList.add((TextView)findViewById(R.id.textView13));
        textViewArrayList.add((TextView)findViewById(R.id.textView14));



       // generateQuestion();

        changeQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correct=0;
                total=0;
                scoreView.setText(""+correct+"/"+total);
                res.setText("");
                generateQuestion();
                countDownTimer.start();
                gameRunning=true;
                changeQues.setVisibility(View.GONE);
            }
        });


    }

    private void generateQuestion(){
        op=random.nextInt(2);//operator
        val1=random.nextInt(10)+1;
        val2=random.nextInt(10)+1;

        if (op==0)
            options.set(3,val1+val2);
        else
            options.set(3,val1*val2);//3 is the answer

        options.set(0,options.get(3));
        options.set(1,options.get(3));
        options.set(2,options.get(3));

        while (options.get(0)==options.get(3))
            options.set(0,random.nextInt(options.get(3))+random.nextInt(options.get(3)));
        while (options.get(1)==options.get(3))
            options.set(1,random.nextInt(options.get(3))+random.nextInt(options.get(3)));
        while (options.get(2)==options.get(3))
            options.set(2,random.nextInt(options.get(3))+random.nextInt(options.get(3)));

        Collections.shuffle(arrayList);

        for (int i=0;i<options.size();i++){
            textViewArrayList.get(arrayList.get(i)).setText(""+options.get(i));
            if (i==3)
                textViewArrayList.get(arrayList.get(i)).setTag("Correct");
            else
                textViewArrayList.get(arrayList.get(i)).setTag("Wrong");
        }

        question.setText(""+val1+ops.charAt(op)+val2);
    }

    public void onAnswerClick(View view){
        if (gameRunning) {
            if (view.getTag().equals("Correct")) {
               // Toast.makeText(this, "Correct answer", Toast.LENGTH_SHORT).show();
                correct++;
                res.setText("Correct :)");
            }
            else {
                res.setText("Incorrect :(");
            }

            total++;
            scoreView.setText(""+correct+"/"+total);
            generateQuestion();
        }
        else {
            Toast.makeText(this, "Press start to start a new game", Toast.LENGTH_SHORT).show();
        }
    }
}