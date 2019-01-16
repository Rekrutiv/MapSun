package com.rekrutiv.MapSunsetSunrise;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.luckycatlabs.sunrisesunset.dto.Location;
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;

import java.util.Calendar;
import java.util.TimeZone;

import com.rekrutiv.MapSunsetSunrise.R;


public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private GpsTracker gpsTracker;
    private TextView tvLatitude,tvLongitude,tvSunrise,tvSunset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSunrise = (TextView)findViewById(R.id.sunrise);
        tvSunset = (TextView)findViewById(R.id.sunset);
        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            TimeZone timezone = TimeZone.getDefault();
            String timezoneId = timezone.getID();

            Location location = new Location(String.valueOf(latitude), String.valueOf(longitude));
            SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, timezoneId);
            String officialSunrise = calculator.getOfficialSunriseForDate(Calendar.getInstance());
            String officialSunset = calculator.getOfficialSunsetForDate(Calendar.getInstance());
            Toast.makeText(MainActivity.this, ""+officialSunrise+" "+officialSunset, Toast.LENGTH_SHORT).show();
            //Calendar officialSunsetCal = calculator.getOfficialSunsetCalendarForDate(Calendar.getInstance());



             tvSunrise.setText("Sunrise : " + officialSunrise);
             tvSunset.setText("Sunset  : " + officialSunset);
             tvSunrise.setTextColor(Color.parseColor("#FFFAFA"));
             tvSunset.setTextColor(Color.parseColor("#6C3483"));

         }else{
            gpsTracker.showSettingsAlert();
        }

        if(isServicesOK()){
            init();
        }
    }

    private void init(){
        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");

            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    }

























