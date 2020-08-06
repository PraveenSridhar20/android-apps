package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences;
    HashSet<String> hashSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=this.getSharedPreferences("com.example.notes",MODE_PRIVATE);
        listView=findViewById(R.id.listView);
        arrayList=new ArrayList<String>();
        hashSet=(HashSet<String>) sharedPreferences.getStringSet("notes1",null);

        if (hashSet!=null) {
            arrayList = new ArrayList<String>(hashSet);
        }
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),Notes_Activity.class);
                intent.putExtra("note",arrayList.get(i));
                intent.putExtra("index",i);
                startActivityForResult(intent,2);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int index=i;
                new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Warning!")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                arrayList.remove(index);
                                hashSet=new HashSet<String>(arrayList);
                                sharedPreferences.edit().putStringSet("notes1",hashSet).apply();
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("CANCEL",null).show();


                return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==RESULT_OK){
                Log.i("the intent data",data.getStringExtra("note"));
                arrayList.add(data.getStringExtra("note"));
                hashSet=new HashSet<String>(arrayList);
                sharedPreferences.edit().putStringSet("notes1",hashSet).apply();
                arrayAdapter.notifyDataSetChanged();
            }
        }
        else if (requestCode==2){
            if (resultCode==RESULT_OK) {
                if (data.getIntExtra("index",-1)!=-1){
                    arrayList.set(data.getIntExtra("index",-1),data.getStringExtra("note"));
                    hashSet=new HashSet<String>(arrayList);
                    sharedPreferences.edit().putStringSet("notes1",hashSet).apply();
                    arrayAdapter.notifyDataSetChanged();

                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.add_note:
                Intent intent=new Intent (getApplicationContext(),Notes_Activity.class );
                startActivityForResult(intent,1);
                break;
            default:
                return false;
        }
        return true;
    }
}