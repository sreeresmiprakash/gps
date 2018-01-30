package com.example.sreeresmi.gpsenable;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    // GPSTracker class
    GPSTracker gps;
    Gps gps1;
    Button btn;
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager=null;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {

            builder.setMessage("Enable GPS");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            builder.show();


        }
    }
    public void onClick(View v)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
            getLocationforaboveaAndroid5();

        } else{
            // do something for phones running an SDK before lollipop
            Toast.makeText(MainActivity.this, "below", Toast.LENGTH_SHORT).show();
            getLocationbelowAndroid5();
        }


    }


    void getLocationforaboveaAndroid5() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {

                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null){
                            double latti = location.getLatitude();
                            double longi = location.getLongitude();
                            String str_lati= String.valueOf(latti);
                            String str_longi=String.valueOf(longi);
                            System.out.println("Lattitude"+latti);
                            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + str_lati + "\nLong: " + str_longi, Toast.LENGTH_LONG).show();




                        } else {

                        }
                    }




        }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                getLocationforaboveaAndroid5();
                break;
        }
    }

    void getLocationbelowAndroid5()
    {
        // create class object
        gps1 = new Gps(MainActivity.this);

        // check if GPS enabled
        if(gps1.canGetLocation()){

            double latitude = gps1.getLatitude();
            double longitude = gps1.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps1.showSettingsAlert();
        }

    }
}
