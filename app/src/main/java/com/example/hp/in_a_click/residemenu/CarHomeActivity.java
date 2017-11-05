package com.example.hp.in_a_click.residemenu;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.hp.in_a_click.model.CarItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.hp.in_a_click.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CarHomeActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        init();
        addCarsToFirebaseDatabase();


    }

    String[] arrLats = {"29.848214 ", "29.848298"},
    arrLots = {"31.336997", "31.336633"};
    private void addCarsToFirebaseDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cars");
        for(int i = 0; i < arrLats.length; i++){
            databaseReference.push().setValue(
                    new CarItem(i+"", arrLats[i], arrLots[i])
            );
        }

    }


    private void init() {

        Intent intent = getIntent();
        String carOrHome = intent.getStringExtra("carOrHome");
        if (carOrHome.equalsIgnoreCase("car")) {
            initCar();
        } else {
            initHome();
        }
    }

    private void initHome() {


    }

    private void initCar() {




    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }
}
