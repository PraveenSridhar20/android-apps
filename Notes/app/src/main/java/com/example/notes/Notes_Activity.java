package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

public class Notes_Activity extends AppCompatActivity {
    EditText editText;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_);
        intent=getIntent();
        editText=findViewById(R.id.editTextTextMultiLine);
        if (intent.getStringExtra("note")!=null){
            editText.setText(intent.getStringExtra("note"));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent1=new Intent();
        intent1.putExtra("note",editText.getText().toString());
        intent1.putExtra("index",intent.getIntExtra("index",-1));
        setResult(RESULT_OK,intent1);
        finish();
    }
}