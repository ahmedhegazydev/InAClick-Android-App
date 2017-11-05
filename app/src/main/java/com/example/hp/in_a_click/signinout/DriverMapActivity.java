package com.example.hp.in_a_click.signinout;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.hp.in_a_click.common.Common;
import com.example.hp.in_a_click.remote.IGoogleAPI;
import com.example.hp.in_a_click.tests.ListLoginActivity;
import com.example.hp.in_a_click.tests.ListOnlineHolder;
import com.example.hp.in_a_click.tests.Tracking;
import com.example.hp.in_a_click.tests.User;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.hp.in_a_click.R;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        View.OnClickListener {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    final static int MY_PERMISSIONS_REQUEST_LOCATION = 1233;
    final static int PLAY_SERVICES_REF_REQUEST = 12345;
    final static int UPDATE_INTERVAL = 5000;
    final static int FATEST_INTERVAL = 3000;
    final static int DISTANCE = 1000;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 10;
    DatabaseReference onlineRef = null, currentUserRef = null, counterRef = null;
    FirebaseRecyclerAdapter<User, ListOnlineHolder> adapter = null;
    RecyclerView recyclerViewListOnlineUsers = null;
    RecyclerView.LayoutManager layoutManager = null;
    LocationRequest locationRequest = null;
    Location location = null;
    GoogleApiClient googleApiClient = null;
    LocationManager locationManager;
    String provider;
    DatabaseReference refLocations = null;
    GoogleMap mMap = null;
    Marker marker = null;
    GeoFire geoFire = null;
    MaterialAnimatedSwitch materialAnimatedSwitch = null;
    DatabaseReference refDrivers = null;
    SupportMapFragment mapFragment = null;
    ///////////////////////////////////For Tracking the destination
    ArrayList<LatLng> latLngs = null;
    EditText etDestination = null;
    Marker markerDestination = null;
    float v;
    Handler handler = null;
    double lat, lon;
    LatLng startPos, endPos, currentPos;
    int index, next;
    Button btnFindDest;
    String dest;
    PolylineOptions polylineOptionsBlack, polylineOptionsGray;
    Polyline polylineBlack, polylineGray;
    IGoogleAPI mService = null;
    private boolean mRequestingLocationUpdates;
    private String mLastUpdateTime;
    private Marker mMarker;
    /////////////////////Showing popup window above the marker
    private PopupWindow mPopupWindow;
    private int mWidth;
    private int mHeight;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (index < latLngs.size() - 1){
                index ++;
                next = index + 1;

            }
            if (index < latLngs.size() - 1){
                startPos = latLngs.get(index);
                endPos = latLngs.get(next);

            }



            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
            valueAnimator.setDuration(3000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    


                }
            });



        }
    };

    /**
     * Decodes an encoded path string into a sequence of LatLngs.
     */
    public static ArrayList<LatLng> decode(final String encodedPath) {
        int len = encodedPath.length();

        // For speed we preallocate to an upper bound on the final length, then
        // truncate the array before returning.
        final ArrayList<LatLng> path = new ArrayList<LatLng>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_for_driver);
        mapFragment.getMapAsync(this);

        init();
        // buildGoogleApiClient();
        checkLocationPermission();
        getTheFinalDestination();


    }

    private void getTheFinalDestination() {
        btnFindDest = findViewById(R.id.btnFindDest);
        btnFindDest.setOnClickListener(this);
        etDestination = findViewById(R.id.etDestination);
        latLngs = new ArrayList<LatLng>();
        mService = Common.getGooglApi();


    }

    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            //Toast.makeText(this, "requestPermissions", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Permission exist", Toast.LENGTH_SHORT).show();
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
                if (materialAnimatedSwitch.isChecked()) {
                    displayLocation();
                }


            }
        }

//        if (checkPlayServices()) {
//            buildGoogleApiClient();
//            createLocationRequest();
//            displayLocation();
//
//
//        } else {
//
//        }


    }

    private void init() {
        materialAnimatedSwitch = findViewById(R.id.location_switch);
        materialAnimatedSwitch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isDriverOnline) {
                if (isDriverOnline) {
                    startLocationUpdates();
                    displayLocation();
                    Snackbar.make(mapFragment.getView(), "The Driver Is Online Now", Snackbar.LENGTH_SHORT).show();
                } else {
                    stopLocationUpdates();
                    marker.remove();
                    if (mPopupWindow != null) {
                        if (mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                        }
                    }
                    Snackbar.make(mapFragment.getView(), "The Driver Is Offline Now", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        refDrivers = FirebaseDatabase.getInstance().getReference("Drivers");
        geoFire = new GeoFire(refDrivers);


    }

    private boolean checkPlayServices() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_REF_REQUEST).show();
            } else {
                Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }

            return false;
        }

        return true;

    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {

            if (materialAnimatedSwitch.isChecked()) {
                final double lat = location.getLatitude();
                final double lon = location.getLongitude();

                geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(lat, lon), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        //Toast.makeText(DriverMapActivity.this, "onComplete", Toast.LENGTH_SHORT).show();
//
                        if (marker != null) {
                            marker.remove();
                            //marker.setVisible(false);
                        }

                        marker = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                .title("Online Driver")
                                .snippet("Driver1")
                                .position(new LatLng(lat, lon))
                        );

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 20.0f));

                        rotateMarker(marker, -360, mMap);


                    }
                });

            } else {

            }


        } else {
            Toast.makeText(this, "Couldn't get current location", Toast.LENGTH_SHORT).show();
        }

//
//        if (mRequestingLocationUpdates) {
//            startLocationUpdates();
//        }


    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //setButtonsEnabledState();
//                    startLocationUpdates();
//                } else {
//                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                        mRequestingLocationUpdates = false;
//
//                    } else {
//                        showRationaleDialog();
//                    }
//                }
//                break;
//            }
//        }
//    }

    private void showRationaleDialog() {
        new AlertDialog.Builder(this)
                .setPositiveButton("許可する", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(DriverMapActivity.this,
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                })
                .setNegativeButton("しない", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mRequestingLocationUpdates = false;
                    }
                })
                .setCancelable(false)
                .setMessage("このアプリは位置情報の利用を許可する必要があります。")
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (checkPlayServices()) {
                        buildGoogleApiClient();
                        createLocationRequest();
                        if (materialAnimatedSwitch.isChecked()) {
                            displayLocation();
                        }


                    }
                }
                // break;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(DriverMapActivity.this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }

    private void createLocationRequest() {


        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISTANCE);
    }

    private void buildGoogleApiClient() {

//        // Create an instance of GoogleAPIClient.
//        if (googleApiClient == null) {
//            googleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//        }
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        ;
        googleApiClient.connect();
        createLocationRequest();


    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.i(TAG, "onLocationChanged");
        this.location = location;
        //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        //updateUI();
        //Toast.makeText(this, getResources().getString(R.string.location_updated_message), Toast.LENGTH_SHORT).show();
        displayLocation();


    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        // Log.i(TAG, "Connection suspended");
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        //Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        ///Log.i(TAG, "onConnected");
        displayLocation();
        startLocationUpdates();


    }

    private void rotateMarker(final Marker marker, final int i, GoogleMap mMap) {

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = marker.getRotation();
        final long duration = 1500;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {

                long elapsed = SystemClock.uptimeMillis() - start;
                float t;
                float rot;
                t = interpolator.getInterpolation((float) elapsed / duration);
                rot = t * i + (i - t) * startRotation;
                marker.setRotation(-rot > 180 ? rot / 2 : rot);
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (googleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        stopLocationUpdates();
        googleApiClient.disconnect();

        super.onStop();
    }

    protected void stopLocationUpdates() {
        //Log.i(TAG, "stopLocationUpdates");
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            return;
        }

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }


//    private void startLocationUpdates() {
//        // Log.i(TAG, "startLocationUpdates");
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//        // 現在位置の取得の前に位置情報の設定が有効になっているか確認する
//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
//                final Status status = locationSettingsResult.getStatus();
//
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        // 設定が有効になっているので現在位置を取得する
//                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, DriverMapActivity.this);
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        // 設定が有効になっていないのでダイアログを表示する
//                        try {
//                            status.startResolutionForResult(DriverMapActivity.this, REQUEST_CHECK_SETTINGS);
//                        } catch (IntentSender.SendIntentException e) {
//                            // Ignore the error.
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        // Location settings are not satisfied. However, we have no way
//                        // to fix the settings so we won't show the dialog.
//                        break;
//                }
//            }
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    private void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);


    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        // inflate our view here
        View popupView;
        popupView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.about_driver, null);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        popupView.measure(size.x, size.y);

        mWidth = popupView.getMeasuredWidth();
        mHeight = popupView.getMeasuredHeight();
        mPopupWindow = popupWindow;

        //showing the PopupWindow
        updatePopup();


        return true;
    }

    private void updatePopup() {
        if (marker != null && mPopupWindow != null) {
            // marker is visible
            if (mMap.getProjection().getVisibleRegion().latLngBounds.contains(marker.getPosition())) {
                if (!mPopupWindow.isShowing()) {
                    mPopupWindow.showAtLocation(findViewById(R.id.map_for_driver), Gravity.NO_GRAVITY, 0, 0);
                }
                Point p = mMap.getProjection().toScreenLocation(marker.getPosition());
                //mPopupWindow.update(p.x - mWidth / 2, p.y - mHeight + 100, -1, -1);
                //make the popup window above marker
                mPopupWindow.update(p.x - mWidth / 2, p.y - (mHeight + 10), -1, -1);
            } else { // marker outside screen
                mPopupWindow.dismiss();
            }
        }
    }

    @Override
    public void onCameraIdle() {

    }

    //    Called repeatedly as the camera continues to move after an onCameraMoveStarted call.
//    This may be called as often as once every frame and should not perform expensive operations.
//    This is called on the Android UI thread.

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {
        updatePopup();
    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnFindDest)) {
            getDirection();


        }
    }

    private void getDirection() {

        dest = etDestination.getText().toString();
        dest = dest.replace(" ", "+");

        currentPos = new LatLng(location.getLatitude(), location.getLongitude());
        String requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                "mode=driving" +
                "&transit_routing_preference=less_driving" +
                "&origin=" + currentPos.latitude + "," + currentPos.longitude +
                "&destination=" + dest +
                "&key" + getResources().getString(R.string.google_direction_api);

        mService.getPath(requestApi).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject route = jsonArray.getJSONObject(i);
                        JSONObject poly = route.getJSONObject("overview_polyline");
                        String polyLine = poly.getString("points");

                        latLngs = decode(polyLine);
                    }
                    //Adjusting bounds
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (int j = 0; j < latLngs.size(); j++) {
                        builder.include(latLngs.get(j));
                    }
                    LatLngBounds bounds = builder.build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                    mMap.animateCamera(cameraUpdate);

                    polylineOptionsBlack = new PolylineOptions();
                    polylineOptionsBlack.color(Color.BLACK);
                    polylineOptionsBlack.width(5);
                    polylineOptionsBlack.startCap(new SquareCap());
                    polylineOptionsBlack.endCap(new SquareCap());
                    polylineOptionsBlack.jointType(JointType.ROUND);
//                    polylineOptionsBlack.addAll(latLngs);
                    polylineBlack = mMap.addPolyline(polylineOptionsBlack);


                    polylineOptionsGray = new PolylineOptions();
                    polylineOptionsGray.color(Color.GRAY);
                    polylineOptionsGray.width(5);
                    polylineOptionsGray.startCap(new SquareCap());
                    polylineOptionsGray.endCap(new SquareCap());
                    polylineOptionsGray.jointType(JointType.ROUND);
                    polylineOptionsGray.addAll(latLngs);
                    polylineGray = mMap.addPolyline(polylineOptionsGray);

                    mMap.addMarker(new MarkerOptions().position(latLngs.get(latLngs.size() - 1)));


                    final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
                    valueAnimator.setInterpolator(new LinearInterpolator());
                    valueAnimator.setDuration(2000);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            List<LatLng> points = polylineGray.getPoints();

                            int percentValue;
                            int size;
                            int newPoints;

                            size = points.size();
                            percentValue = (int) valueAnimator.getAnimatedValue();
                            newPoints = (int) (size * (percentValue / 100.0f));
                            List<LatLng> latLng = latLngs.subList(0, newPoints);
                            polylineGray.setPoints(latLng);

                        }
                    });
                    valueAnimator.start();


                    marker = mMap.addMarker(new MarkerOptions().position(currentPos).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));

                    handler = new Handler();
                    index = -1;
                    next= 1;
                    handler.post(runnable);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


}