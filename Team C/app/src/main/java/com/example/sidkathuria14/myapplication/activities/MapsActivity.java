package com.example.sidkathuria14.myapplication.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener

{
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mMap;
    public static final String TAG = "maps";
    GoogleApiClient googleApiClient;
    public static final int MY_LOCATION_REQUEST_CODE = 111;
    boolean bPermissionGranted;EditText etMessage,etDefault;String message,defaultMessage;Spinner spinner;
    Location currentLocation;
    GoogleMap googleMap;
    LocationManager locMan;LocationListener locLis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }



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
                etMessage.setText(etMessage.getText().toString() +   " ( " + lat + " , " + lng + " )");
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
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mMap.getMyLocation().getLatitude(),
//                mMap.getMyLocation().getLongitude())));
//        currentLocation = new Location(new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude()),"");
//        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//        Log.d(TAG, "onMapReady: " + String.valueOf(currentLocation.getLatitude()));



    }


public void function(GoogleMap googleMap){
    mMap = googleMap;
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
//        }
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//Location myLocation = mMap.getMyLocation();
//        LatLng latLng = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
//        // Add a marker in Sydney and move the camera
//        mMap.setMyLocationEnabled(true);
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == MY_LOCATION_REQUEST_CODE) {
//            if (permissions.length == 1 &&
//                    permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                mMap.setMyLocationEnabled(true);
//            } else {
//                // Permission was denied. Display an error message.
//                Toast.makeText(this, "GPS needs to be switched on for this application", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }



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

//    @Override
//    public void onLocationChanged(Location location) {
//        Log.d(TAG, "onLocationChanged: ");
//    }
//
//    @Override
//    public void onStatusChanged(String s, int i, Bundle bundle) {
//        Log.d(TAG, "onStatusChanged: ");
//    }
//
//    @Override
//    public void onProviderEnabled(String s) {
//        Log.d(TAG, "onProviderEnabled: ");
//    }
//
//    @Override
//    public void onProviderDisabled(String s) {
//        Log.d(TAG, "onProviderDisabled: ");
//    }
}
