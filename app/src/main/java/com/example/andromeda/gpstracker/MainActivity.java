package com.example.andromeda.gpstracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    TextView textView, map, send;
    EditText editText;
    FusedLocationProviderClient mFusedLocationClient;
    String locationData, geocode, number, smsBody;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);
        map = findViewById(R.id.button_map);
        send = findViewById(R.id.button_send);
        editText = findViewById(R.id.editText);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            geocode = location.getLatitude() + "," + location.getLongitude();
                            locationData = "geo:0,0?q=" + geocode + "(Help)";
                            textView.setText("Latitude : " + location.getLatitude() + "\n" + "Longitude : " + location.getLongitude());
                        } else {
                            textView.setText("Location Data is Not Available");
                        }
                    }
                });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Opening Maps", Toast.LENGTH_SHORT).show();
                Uri gmmIntentUri = Uri.parse(locationData);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("+880")) {
                    number = editText.getText().toString();
                    smsBody = "Help Me ! I am Here : https://www.google.com/maps/?q=" + geocode;
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(number, null, smsBody, null, null);
                    Toast.makeText(MainActivity.this, "Message Sent to " + number, Toast.LENGTH_SHORT).show();
                } else {
                    number = "+8801791616240";///////////////////////////////////////////////////////////////////////////////////////////// put number here
                    smsBody = "Help Me ! I am Here : https://www.google.com/maps/?q=" + geocode; ////////////////////////////////////////// edit sms body
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(number, null, smsBody, null, null);
                    Toast.makeText(MainActivity.this, "Message Sent to Default Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_MUTE)){
            number = "+8801791616240";
            smsBody = "Help Me ! I am Here : https://www.google.com/maps/?q=" + geocode;
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(number, null, smsBody, null, null);
            Toast.makeText(MainActivity.this, "Message Sent to Default Number", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }
}
