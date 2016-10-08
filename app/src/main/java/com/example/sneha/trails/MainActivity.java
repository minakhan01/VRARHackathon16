package com.example.sneha.trails;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter mBAdapter;
    BluetoothManager mBManager;
    BluetoothLeAdvertiser mBLEAdvertiser;
    LocationManager mLocationManager;
    static final int PERMISSION_RESULT_CODE = 1;
    Location currentLocation;
    Boolean IS_PATH_RUNNING = false;
    ArrayList<LocationData> currentPath;
    List<List<LocationData>> paths = new ArrayList<List<LocationData>>();
    double currentLatitude;
    double currentLongitude;
    double currentBearing;
    double targetLatitude;
    double targetLongitude;
    double targetBearing;
    double finalRotationAngle;

    static final int BEACON_ID = 1775;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBAdapter = mBManager.getAdapter();
        mBLEAdvertiser = mBAdapter.getBluetoothLeAdvertiser();

        int permissionCheck = ContextCompat.checkSelfPermission((Context)this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            startGPS();
        }
        else
        {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_RESULT_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mBAdapter == null || !mBAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            finish();
            return;
        }

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "No LE support on this device", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!mBAdapter.isMultipleAdvertisementSupported()) {
            Toast.makeText(this, "No advertising support on this device", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        startAdvertising();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAdvertising();
    }

    private void startAdvertising() {
        if (mBLEAdvertiser == null) return;
        Log.i("start_advertising", "entered start advertising..");


        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setConnectable(false)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .build();

        AdvertiseData data = new AdvertiseData.Builder()
                .addManufacturerData(BEACON_ID, buildGPSPacket())
                .build();

        mBLEAdvertiser.startAdvertising(settings, data, mAdvertiseCallback);
    }

    private void stopAdvertising() {
        if (mBLEAdvertiser == null) return;
        mBLEAdvertiser.stopAdvertising(mAdvertiseCallback);
    }

    private void restartAdvertising() {
        stopAdvertising();
        startAdvertising();
    }

    private AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            String msg = "Service Running";
            mHandler.sendMessage(Message.obtain(null, 0, msg));
        }

        @Override
        public void onStartFailure(int errorCode) {
            if (errorCode != ADVERTISE_FAILED_ALREADY_STARTED) {
                String msg = "Service failed to start: " + errorCode;
                mHandler.sendMessage(Message.obtain(null, 0, msg));
            } else {
                restartAdvertising();
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*
            UI feedback to the user would go here.
            */
        }
    };


    public void startGPS()
    {
        //Execute location service call if user has explicitly granted ACCESS_FINE_LOCATION..
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                UpdatePosition(location);
                TextView latitude = (TextView) findViewById(R.id.location_latitude);
                latitude.append(" " +location.getLatitude());

                TextView longitude = (TextView) findViewById(R.id.location_longitude);

                longitude.append(" " + location.getLongitude());
                if(IS_PATH_RUNNING){
                    Log.i("Path_started", "adding location to path");
                    LocationData lastLocation = currentPath.get(currentPath.size()-1);
                    if(areNotClose(lastLocation.latitude, lastLocation.longitude, location.getLatitude(), location.getLongitude())) {
                        addLocationDataObject(location.getLatitude(), location.getLongitude());
                    }
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };1
        try
        {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            currentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.i("GPS_Receiver", "startGPS: GPS Started..");
        }
        catch(SecurityException e)
        {

        }
    }

    public void UpdatePosition(Location location)
    {
        currentLocation = location;
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        currentBearing = location.getBearing();
        targetLatitude = currentLatitude + 0.01;
        targetLongitude = currentLongitude + 0.01;
        targetBearing = calculateTargetBearing(currentLatitude, currentLongitude, targetLatitude, targetLongitude);
        finalRotationAngle = calculateFinalBearing(currentBearing, targetBearing);
        restartAdvertising();
        Log.i("Update_Position", "restarted advertising and updated position..");
    }

    private static int mod(int a, int b) {
    return (a % b + b) % b;
  }

    private static String TAG = "Orientation app";

    public double calculateFinalBearing(double currentBearing, double targetBearing) {
      double magneticHeading360 = currentBearing;
      double targetOrientation360 = currentBearing;
      double angle = Math.min(mod((int)(magneticHeading360 - targetOrientation360), 360), mod((int) (targetOrientation360 - magneticHeading360), 360));
      boolean isRight = true;

      boolean bothSameSide = false;
      if ((magneticHeading360 > 180 && targetOrientation360 > 180) || (magneticHeading360 < 180 && targetOrientation360 < 180))
        bothSameSide = true;

      Log.d(TAG, "heading 360: " + magneticHeading360);
      Log.d(TAG, "target orientation 360: " + targetOrientation360);
      Log.d(TAG, "both on same side: " + bothSameSide);

      if (bothSameSide) {
        if (targetOrientation360 > magneticHeading360){
          isRight = true;
        }
        else{
          isRight = false;
        }

      } else if (180 < targetOrientation360 && targetOrientation360 < 270 && 0 < magneticHeading360 && magneticHeading360 < 90) {
        if ((targetOrientation360 - magneticHeading360) < 180) {
          isRight = true;
        }
        else {
          isRight = false;
        }
      }
      else if (0 < targetOrientation360 && targetOrientation360 < 90 && 180 < magneticHeading360 && magneticHeading360 < 270) {
        if ((magneticHeading360  - targetOrientation360 ) < 180) {
          isRight = false;
        }
        else {
          isRight = true;
        }
      }
      else if (270 < targetOrientation360 && targetOrientation360 < 360 && 90 < magneticHeading360 && magneticHeading360 < 180) {
        if ((targetOrientation360 - magneticHeading360) < 180) {
          isRight = true;
        }
        else {
          isRight = false;
        }
      }
      else if (270 < magneticHeading360 && magneticHeading360 < 360 && 90 < targetOrientation360 && targetOrientation360 < 180) {
        if ((magneticHeading360  - targetOrientation360 ) < 180) {
          isRight = false;
        }
        else {
          isRight = true;
        }
      }
      else if (270 < magneticHeading360 && magneticHeading360 < 360 && 0 < targetOrientation360 && targetOrientation360 < 90) {
        isRight = true;
      }
      else if (0 < magneticHeading360 && magneticHeading360 < 90 && 270 < targetOrientation360 && targetOrientation360 < 360) {
        isRight = false;
      }
      else if (90 < magneticHeading360 && magneticHeading360 < 180 && 180 < targetOrientation360 && targetOrientation360 < 270) {
        isRight = true;
      }
      else if (180 < magneticHeading360 && magneticHeading360 < 270 && 90 < targetOrientation360 && targetOrientation360 < 180) {
        isRight = false;
      }
      if (!isRight) {
        angle = angle*(-1);
      }
      return angle;
    }

    public double calculateTargetBearing(double currentLatitude, double currentLongitude, double targetLatitude, double targetLongitude) {

        double dLon = (targetLongitude-currentLongitude);
        double y = Math.sin(dLon) * Math.cos(targetLatitude);
        double x = Math.cos(currentLatitude)*Math.sin(targetLatitude) - Math.sin(currentLatitude)*Math.cos(targetLatitude)*Math.cos(dLon);
        double brng = Math.toDegrees((Math.atan2(y, x)));
        brng = (360 - ((brng + 360) % 360));
        return brng;

    }

    /* Updated method */
    private byte[] buildGPSPacket()
    {

        Location location = currentLocation;
        byte[] packet = new byte[24];
        if(location != null) {
            try {
                double latitude = location.getLatitude();
              Log.d(TAG, "sending finalRotationAngle in packet: "+finalRotationAngle);
                byte[] buffer = ByteBuffer.allocate(8).putDouble(finalRotationAngle).array();
                for (int i = 0, j =7; i < 8; i++, j--) packet[i] = buffer[j];

                double longitude = location.getLongitude();
                buffer = ByteBuffer.allocate(8).putDouble(longitude).array();
                for (int i = 8, j =7; i < 16; i++, j--) packet[i] = buffer[j];

                float bearing = 0;
                bearing = location.getBearing();
                buffer = ByteBuffer.allocate(4).putFloat(bearing).array();
                for (int i = 16, j =3; i < 20; i++, j--) packet[i] = buffer[j];

                float speed = 0;
                speed = location.getSpeed();
                buffer = ByteBuffer.allocate(4).putFloat(speed).array();
                for (int i = 20, j =3; i < 24; i++, j--) packet[i] = buffer[j];

            } catch (NumberFormatException e) {
                packet = new byte[24];
            }
        }
        Log.i("Build_packet", "Building GPS Packet , returning..");

        return packet;
    }

    public void addLocationDataObject(double latitude, double longitude){
        LocationData data = new LocationData();
        data.latitude = latitude;
        data.longitude =  longitude;
        currentPath.add(data);
    }

    public void startPath(View view){
        IS_PATH_RUNNING = TRUE;
        currentPath = new ArrayList<LocationData>();
        Log.i("Path_started", "Path BEGINS");
        if(currentLocation != null){
            addLocationDataObject(currentLatitude, currentLongitude);
        }
    }

    public void stopPath(View view){
        IS_PATH_RUNNING = FALSE;
        paths.add(currentPath);
        Log.i("Path_stopped", "Path ENDS");


        for (LocationData d: currentPath) {
            Log.i("currentPath","Latitude : " + d.latitude + "\nLongitude : " + d.longitude + "\n ");
        }
    }


    public boolean areNotClose(double lat1,double long1, double lat2, double long2){
        double R = 6371; // km
        double fixedDistance = 0.0006096;
        double dLat = Math.toRadians(lat2-lat1); //(lat2-lat1).toRad();
        double dLon = Math.toRadians(long2-long1);//(lon2-lon1).toRad();
        double latitude1 = Math.toRadians(lat1); //lat1.toRad();
        double latitude2 = Math.toRadians(lat2); //lat2.toRad();

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(latitude1) * Math.cos(latitude2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;

        if(distance > fixedDistance) {
            return true;
        }
        return false;
    }


}
