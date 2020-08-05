package com.example.praveen.tictactoe;


import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean player1=true;
    int[][] grid={{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}};
    boolean gameIsOver=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.playAgain);
        button.setVisibility(View.GONE);
    }

    public void onClick(View view){

        String s=view.getTag().toString();
        ImageView imageView=(ImageView)view;

        int xCoord=s.charAt(s.length()-2)-'0';
        int yCoord=s.charAt(s.length()-1)-'0';
        xCoord--;
        yCoord--;
        if (gameIsOver){
            Toast.makeText(this, "Game is over! Press the button to replay", Toast.LENGTH_SHORT).show();
        }
        else if (grid[xCoord][yCoord]!=-1){
            Toast.makeText(this, "That tile is already full!", Toast.LENGTH_SHORT).show();
        }

        else {
            if (player1==true)
                imageView.setImageResource(R.drawable.red);
            else
                imageView.setImageResource(R.drawable.yellow);
            imageView.setTranslationY(-1000);
            imageView.setAlpha(1f);
            imageView.animate().translationY(0).setDuration(250);


            if (player1)
                grid[xCoord][yCoord] = 1;
            else
                grid[xCoord][yCoord] = 0;

            int x = 1;
            if (player1)
                player1 = false;
            else {
                player1 = true;
                x = 2;
            }
            //rowCheck
            for (int i = 0; i < 3; i++) {
                boolean flag = false;
                for (int j = 0; j < 2; j++) {

                    if ((grid[i][j] != grid[i][j + 1]) || grid[i][j] == -1) {
                        flag = true;
                    }
                }
                if (!flag) {
                    Toast.makeText(this, "Player " + x + " wins", Toast.LENGTH_SHORT).show();
                    gameIsOver = true;
                }
            }
            //colCheck
            for (int j = 0; j < 3; j++) {
                boolean flag = false;
                for (int i = 0; i < 2; i++) {

                    if ((grid[i][j] != grid[i + 1][j]) || grid[i][j] == -1) {
                        flag = true;
                    }
                }
                if (!flag) {
                    Toast.makeText(this, "Player " + x + " wins", Toast.LENGTH_SHORT).show();
                    gameIsOver = true;
                }
            }

            if (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2] && grid[0][0] != -1) {
                Toast.makeText(this, "Player " + x + " wins", Toast.LENGTH_SHORT).show();
                gameIsOver = true;
            }

            if (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0] && grid[2][0] != -1) {
                Toast.makeText(this, "Player " + x + " wins", Toast.LENGTH_SHORT).show();
                gameIsOver = true;
            }

            boolean flag = false;
            //draw
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (grid[i][j] == -1) {
                        flag = true;
                        break;
                    }
                }
            }

            if (!flag && !gameIsOver) {
                gameIsOver = true;
                Toast.makeText(this, "Draw", Toast.LENGTH_SHORT).show();
            }
            if (gameIsOver) {
                Button button = findViewById(R.id.playAgain);
                button.setVisibility(View.VISIBLE);
            }
        }
    }


    public void onButtonClick(View v) {
        player1=true;
        gameIsOver=false;
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                grid[i][j]=-1;
            }
        }
        GridLayout gridLayout=findViewById(R.id.gridLayout);
        for (int i=0;i<gridLayout.getChildCount();i++){
            ImageView child=(ImageView)gridLayout.getChildAt(i);
            child.setAlpha(0f);
        }
        Button button=findViewById(R.id.playAgain);
        button.setVisibility(View.GONE);
    }

}
