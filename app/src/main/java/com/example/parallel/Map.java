package com.example.parallel;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;


import com.google.android.gms.maps.model.Marker;


import java.io.IOException;
import java.util.List;



public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Marker marker;
    LocationListener locationListener;
    SearchView searchView;
    ImageButton locBtn;
    private  Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);

        searchView = findViewById(R.id.sv_location);
        locBtn = findViewById(R.id.locationBtn);
        final double[] latitude = new double[1];
        final double[] longitude = new double[1];

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            //return;
        } else {

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    //zoomLevel to magnify user location on map
                    float zoomLevel = 15f;

                    //get the latitude
                    latitude[0] = location.getLatitude();

                    //get the longitude
                    longitude[0] = location.getLongitude();

                    //Instantiate the class Geocoder, converting lat long to meaningful addresses locations
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude[0], longitude[0], 1);
                        String strResult = addressList.get(0).getLocality() + " : ";
                        strResult += addressList.get(0).getCountryName();

                        LatLng latLng = new LatLng(latitude[0], longitude[0]);
                        if (marker != null) {
                            marker.remove();
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(strResult));
                            mMap.setMaxZoomPreference(20);
                            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                        } else {
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(strResult));
                            mMap.setMaxZoomPreference(20);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
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



            };

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                float zoomLevel = 15f;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(Map.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        locBtn.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("MissingPermission")
            @Override

            public void onClick(View v) {

                float zoomLevel = 15f;
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude[0], longitude[0], 1);
                    String strResult = addressList.get(0).getLocality() + " : ";
                    strResult += addressList.get(0).getCountryName();

                    LatLng latLng = new LatLng(latitude[0], longitude[0]);
                    if (marker != null) {
                        marker.remove();
                        marker = mMap.addMarker(new MarkerOptions().position(latLng).title(strResult));
                        mMap.setMaxZoomPreference(20);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                    } else {
                        marker = mMap.addMarker(new MarkerOptions().position(latLng).title(strResult));
                        mMap.setMaxZoomPreference(20);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });






/*
        //checking if the network provider is enabled
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    //zoomLevel to magnify user location on map
                    float zoomLevel = 15f;

                    //get the latitude
                    double latitude = location.getLatitude();

                    //get the longitude
                    double longitude = location.getLongitude();

                    //Instantiate the class LatLng
                    LatLng latLng = new LatLng(latitude, longitude);

                    //Instantiate the class Geocoder, converting lat long to meaningful addresses
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getLocality() + ",";
                        str += addressList.get(0).getCountryName();

                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

 */
        /*
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    //zoomLevel to magnify user location on map
                    float zoomLevel = 15f;

                    //get the latitude
                    double latitude = location.getLatitude();

                    //get the longitude
                    double longitude = location.getLongitude();

                    //Instantiate the class LatLng
                    LatLng latLng = new LatLng(latitude, longitude);

                    //Instantiate the class Geocoder, converting lat long to meaningful addresses
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getLocality() + ",";
                        str += addressList.get(0).getCountryName();

                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

         */
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    //zoomLevel to magnify user location on map
                    float zoomLevel = 15f;

                    //get the latitude
                    double latitude = location.getLatitude();

                    //get the longitude
                    double longitude = location.getLongitude();

                    //Instantiate the class Geocoder, converting lat long to meaningful addresses locations
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String strResult = addressList.get(0).getLocality() + " : ";
                        strResult += addressList.get(0).getCountryName();

                        LatLng latLng = new LatLng(latitude, longitude);
                        if (marker != null) {
                            marker.remove();
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(strResult));
                            mMap.setMaxZoomPreference(20);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                        } else {
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(strResult));
                            mMap.setMaxZoomPreference(20);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
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

            };

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override

    public void onMapReady(GoogleMap googleMap) {



    }


    @Override
    protected void onStop(){
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }
}