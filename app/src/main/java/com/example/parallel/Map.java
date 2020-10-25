package com.example.parallel;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap mapAPI;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);

        mapFragment.getMapAsync(this);
    }

    // sets map location to ottawa, must change to implement location services 
    @Override
    public void onMapReady(GoogleMap googleMap){
        mapAPI = googleMap;

        LatLng Ottawa = new LatLng(45.411073, -75.696295);
        mapAPI.addMarker(new MarkerOptions().position(Ottawa).title("Ottawa"));
        mapAPI.moveCamera(CameraUpdateFactory.newLatLng(Ottawa));
    }
}
