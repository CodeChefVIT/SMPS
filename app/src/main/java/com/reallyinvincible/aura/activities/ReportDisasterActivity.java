package com.reallyinvincible.aura.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.reallyinvincible.aura.models.Information;
import com.reallyinvincible.aura.R;
import com.reallyinvincible.aura.utils.UtilConstants;

import java.util.Random;

public class ReportDisasterActivity extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Dexter.withActivity(ReportDisasterActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
        setContentView(R.layout.activity_report_disaster);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("DisasterInformation");
        getLocation();
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        Toast.makeText(ReportDisasterActivity.this, "Fetching data", Toast.LENGTH_SHORT).show();
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Toast.makeText(ReportDisasterActivity.this, "Error Fetching Location. \nPlease Try Again!", Toast.LENGTH_SHORT).show();
                        if (location != null) {
                            // Logic to handle location object
                            String arr[] = UtilConstants.arr;
                            Random random = new Random();
                            int numDisaster = random.nextInt(arr.length);
                            int numIntensity = random.nextInt(arr.length);

                            SharedPreferences sharedPreferences = getSharedPreferences("Information", MODE_PRIVATE);
                            String phoneNumber = sharedPreferences.getString("UserPhoneNumber", "0");

                            Information information = new Information(location.getLatitude(), location.getLongitude(), phoneNumber, arr[numDisaster], numIntensity);
                            ((TextView) findViewById(R.id.tv_location_coordinates)).setText(String.valueOf(location.getLongitude()) + ", " + String.valueOf(location.getLatitude()));
                            mDatabaseReference.push().setValue(information);
                        }
                    }
                });
    }
}


/*

 */