package com.example.memorableplaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng latLng1,latLngToWrite;
    Intent intent;
    LocationManager locationManager;
    LocationListener locationListener;
    String addressLine="";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastKnownLoc=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastKnownLoc!=null) {
                        latLng1 = new LatLng(lastKnownLoc.getLatitude(), lastKnownLoc.getLongitude());
                    }
                    else{
                        latLng1=new LatLng(0,0);
                    }
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng1).title("Last Known Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        intent=getIntent();
        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLoc=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLoc!=null) {
                latLng1 = new LatLng(lastKnownLoc.getLatitude(), lastKnownLoc.getLongitude());
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                latLng1=latLng;
                latLngToWrite=latLng1;
                mMap.clear();
                Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
                try {
                    List<Address> addressList=geocoder.getFromLocation(latLng1.latitude,latLng1.longitude,1);
                    if (addressList!=null && addressList.size()>0) {
                        addressLine=addressList.get(0).getAddressLine(0);
                        Log.i("Address Pressed",addressList.get(0).getAddressLine(0));
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                }
                if (addressLine==""){
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm yyyy-MM-dd");
                    addressLine=simpleDateFormat.format(new Date());
                }
                mMap.addMarker(new MarkerOptions().position(latLng).title(addressLine));

            }
        });
        mMap = googleMap;
        if (intent.getParcelableExtra("pinDrop")==null) {
            LatLng position = new LatLng(0,0);
            if (latLng1!=null)
                position=latLng1;
            mMap.addMarker(new MarkerOptions().position(position).title("Last Known Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,15));
        }
        else{
            LatLng pinDrop=(LatLng)intent.getParcelableExtra("pinDrop");
            String locName=intent.getStringExtra("locName");
            mMap.addMarker(new MarkerOptions().position(pinDrop).title(locName));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pinDrop,15));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        if (latLngToWrite!=null) {
            intent.putExtra("loc", latLngToWrite);
            intent.putExtra("locName",addressLine);
        }
        setResult(RESULT_OK,intent);
        if (latLngToWrite==null)
            setResult(RESULT_CANCELED,intent);
        finish();
    }
}