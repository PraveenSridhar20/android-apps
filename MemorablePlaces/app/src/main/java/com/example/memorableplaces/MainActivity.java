package com.example.memorableplaces;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> places;
    ArrayList<LatLng> locations;
    ArrayList<String> lats;
    ArrayList<String> longs;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=this.getSharedPreferences("com.example.memorableplaces",MODE_PRIVATE);
        places=new ArrayList<String>();
        locations=new ArrayList<LatLng>();
        lats=new ArrayList<String>();
        longs=new ArrayList<String>();
        try {
            places= (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("names",ObjectSerializer.serialize(new ArrayList<String>())));
            lats=   (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats",ObjectSerializer.serialize(new ArrayList<String>())));
            longs=   (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longs",ObjectSerializer.serialize(new ArrayList<String>())));

            for (int i=0;i<lats.size();i++){
                locations.add(new LatLng(Double.parseDouble(lats.get(i)),Double.parseDouble(longs.get(i))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (places.size()==0) {
            places.add("Add new place");
        }
        listView=findViewById(R.id.listView1);
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,places);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
                if (i==0){
                    startActivityForResult(intent,1);
                }
                else{
                    intent.putExtra("pinDrop",locations.get(i-1));
                    intent.putExtra("locName",places.get(i));
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("The result code",""+resultCode);
        if (requestCode==1){
            if (resultCode==RESULT_OK){
                LatLng latLng=new LatLng(0,0);
                places.add(data.getStringExtra("locName"));
                locations.add((LatLng)data.getParcelableExtra("loc"));
                lats.add(Double.toString(locations.get(locations.size()-1).latitude));
                longs.add(Double.toString(locations.get(locations.size()-1).longitude));
                arrayAdapter.notifyDataSetChanged();
                try {
                   // sharedPreferences.edit().putString("locations",ObjectSerializer.serialize(locations)).apply();
                    sharedPreferences.edit().putString("names",ObjectSerializer.serialize(places)).apply();
                    sharedPreferences.edit().putString("lats",ObjectSerializer.serialize(lats)).apply();
                    sharedPreferences.edit().putString("longs",ObjectSerializer.serialize(longs)).apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Location saved", Toast.LENGTH_SHORT).show();
            }
        }
    }
}