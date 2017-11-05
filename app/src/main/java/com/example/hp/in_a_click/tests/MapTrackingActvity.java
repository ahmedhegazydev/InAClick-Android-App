package com.example.hp.in_a_click.tests;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.hp.in_a_click.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class MapTrackingActvity extends FragmentActivity implements OnMapReadyCallback {

    String secondUserEmail = "";
    double lat = 0, lon = 0;
    DatabaseReference refLocations = null;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_tracking_actvity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();

    }

    private void init() {

        if (getIntent() != null) {
            secondUserEmail = getIntent().getStringExtra("email");
            lat = getIntent().getDoubleExtra("lat", 0);
            lon = getIntent().getDoubleExtra("lon", 0);


        }

        refLocations = FirebaseDatabase.getInstance().getReference("Locations");

        if (!TextUtils.isEmpty(secondUserEmail)) {
            Toast.makeText(this, "loadLocationForThisUser", Toast.LENGTH_SHORT).show();
            loadLocationForThisUser();
        }


    }

    private void loadLocationForThisUser() {
        Query query = refLocations.orderByChild("userEmail").equalTo(secondUserEmail);
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Tracking tracking = dataSnapshot1.getValue(Tracking.class);

                    LatLng latLngFriendLocation = new LatLng(Double.valueOf(tracking.getUserLocationLat()), Double.valueOf(tracking.getUserLocationLon()));


                    //current user
                    Location locationCurrentUser = new Location("");
                    locationCurrentUser.setLatitude(lat);
                    locationCurrentUser.setLongitude(lon);

                    //friend location
                    Location locationFriend = new Location("");
                    locationFriend.setLatitude(Double.parseDouble(tracking.getUserLocationLat()));
                    locationFriend.setLongitude(Double.parseDouble(tracking.getUserLocationLon()));




                    mMap.addMarker(new MarkerOptions()
                            .position(latLngFriendLocation)
                            .title(tracking.getUserEmail())
                            .snippet("Distance " + new DecimalFormat("#.#").format(distance(locationCurrentUser, locationFriend)))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 12.0f));


                }


                LatLng current = new LatLng(lat, lon);
                mMap.addMarker(new MarkerOptions().position(current).title(FirebaseAuth.getInstance().getCurrentUser().getEmail()));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private double distance(Location locationCurrentUser, Location locationFriend) {


        double theta = locationCurrentUser.getLongitude() - locationFriend.getLongitude();
        double dist = Math.sin(deg2rad(locationCurrentUser.getLatitude())) * Math.sin(deg2rad(locationFriend.getLatitude()))
                * Math.cos(deg2rad(locationCurrentUser.getLatitude())) * Math.cos(deg2rad(locationFriend.getLatitude()));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return dist;


    }

    private double rad2deg(double rad) {

        return (rad * 180.0 / Math.PI);

    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }
}
