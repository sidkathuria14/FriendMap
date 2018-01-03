package com.example.sidkathuria14.myapplication.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sidkathuria14.myapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class MapsActivity extends SigninActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
//        , LocationListener

{
    List<Address> addressList;


    Geocoder geocoder;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public   String latlng;
    private GoogleMap mMap;
    public static final String TAG = "maps";
    GoogleApiClient googleApiClient;
    public static final int MY_LOCATION_REQUEST_CODE = 111;
    boolean bPermissionGranted;EditText etMessage,etDefault;String message,defaultMessage = "Hey there! I am currently at - ";Spinner spinner;
    Location currentLocation;
    GoogleMap googleMap;
    LocationManager locMan;LocationListener locLis;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");


            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }

geocoder= new Geocoder(this, Locale.getDefault());

            etMessage = (EditText) findViewById(R.id.etMessage);
//etMessage.setText(getLocation());

            ((Button) findViewById(R.id.quicksend)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                    builder.setTitle("choose a friend");
                    builder.setPositiveButton("select", null);
                    builder.setNegativeButton("cancel", null);
                    AlertDialog dialog = builder.create();
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
                    dialog.show();
                }
            });

            ((Button) findViewById(R.id.btn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
//                whatsappIntent.setType("text/plain");
//                whatsappIntent.setPackage("com.whatsapp");
//                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
//                try {
//                    startActivity(whatsappIntent);
//                } catch (android.content.ActivityNotFoundException ex) {
////            Toast("Whatsapp have not been installed.");
//                    Toast.makeText(MapsActivity.this, "Whatsapp has not been installed.", Toast.LENGTH_SHORT).show();
//                }
                    message = etMessage.getText().toString();
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(Intent.createChooser(share, "Share using"));
                }

            });

        locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
        locLis = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String lat,lng;
                Log.d(TAG, "lat: " + location.getLatitude());
                Log.d(TAG, "lng: " + location.getLongitude());
                Log.d(TAG, "prov: " + location.getProvider());
                Log.d(TAG, "accuracy: " + location.getAccuracy());
                Log.d(TAG, "alt: " + location.getAltitude());
                Log.d(TAG, "speed: " + location.getSpeed());
                lat = String.valueOf(location.getLatitude());
                lng = String.valueOf(location.getLongitude());
                latlng = "("+lat + "," + lng +")";
                try {
                    addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                    if(addressList.size() >0){
                        Log.d(TAG, "onLocationChanged: " + addressList.get(0));
                    }

                } catch(IOException ioe){

                }
                etMessage.setText(defaultMessage +
//                        " ( " + lat + " , " + lng + " )" +
                        "  " + addressList.get(0).getAddressLine(0));
//                        latlng );
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(
//            mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude())
                                location.getLatitude(),location.getLongitude())
                        ,7.0f)));
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
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) &&

                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 234);
        } else {
            startLocationTracking();
        }

    }
//    public String test(String str){
//
//    }
//   public String getLocation(){
//       String loc;
//       locLis = new LocationListener() {
//          String loc1;
//           @Override
//           public void onLocationChanged(Location location) {
//               Log.d(TAG, "lat: " + location.getLatitude());
//               Log.d(TAG, "lng: " + location.getLongitude());
//               Log.d(TAG, "prov: " + location.getProvider());
//               Log.d(TAG, "accuracy: " + location.getAccuracy());
//               Log.d(TAG, "alt: " + location.getAltitude());
//               Log.d(TAG, "speed: " + location.getSpeed());
//           loc1 = String.valueOf(location.getLatitude());
//               etMessage.setText(loc1);
//
//           }
//
//           @Override
//           public void onStatusChanged(String s, int i, Bundle bundle) {
//
//           }
//
//           @Override
//           public void onProviderEnabled(String s) {
//
//           }
//
//           @Override
//           public void onProviderDisabled(String s) {
//
//           }
//       };
////       Log.d(TAG, "getLocation: string latitude " + loc );
////       return loc;
//    }
public void getAddress(double lat,double lng){

}
    @SuppressWarnings("MissingPermission")
    void startLocationTracking () {

        locMan.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                10000,
                10,
                locLis
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationTracking();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //User has previously accepted this permission
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            //Not in api-23, no need to prompt
            mMap.setMyLocationEnabled(true);
        }
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//        CameraPosition.builder(new LatLng(mMap.getMyLocation().getLatitude(),
//                mMap.getMyLocation().getLongitude()),0.4f,20°,);
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(
////                new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude())));
//                new LatLng(latlng[])  ));
//        currentLocation = new Location(new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude()),"");
//        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//        Log.d(TAG, "onMapReady: " + String.valueOf(currentLocation.getLatitude()));
//mMap.moveCamera(CameraUpdateFactory.zoomBy(1.8f));
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude()),2.0f)));
        Log.d(TAG, "onMapReady: " + latlng);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(latLng,7.0f)));

            }
        });

    }



    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //  TODO: Prompt with explanation!

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay!
////                    function(googleMap);
//                    if (ActivityCompat.checkSelfPermission(this,
//                            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                        mMap.setMyLocationEnabled(true);
//                    }
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addFriend :
startActivity(new Intent(MapsActivity.this,AddFriend.class));
                return true;
            case R.id.geofencing:
startActivity(new Intent(MapsActivity.this, GeoFencingActivity.class));
                return true;
            case R.id.signOut:
                signOut();
            default: return super.onOptionsItemSelected(item);
        }


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: "  + String.valueOf(currentLocation.getLatitude()));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: ");
    }



}
