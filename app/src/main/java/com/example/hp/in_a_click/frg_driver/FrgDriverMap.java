package com.example.hp.in_a_click.frg_driver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.hp.in_a_click.R;
import com.example.hp.in_a_click.adapters.AutoCompleteAdapter;
import com.example.hp.in_a_click.adapters.CoverFlowAdapter;
import com.example.hp.in_a_click.frg_user.FrgUserMap;
import com.example.hp.in_a_click.model.GameEntity;
import com.example.hp.in_a_click.model.UserDriver;
import com.example.hp.in_a_click.residemenu.MenuActivity;
import com.example.hp.in_a_click.signinout.DriverSignInOutActivity;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.special.ResideMenu.ResideMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


public class FrgDriverMap extends Fragment implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnMapLoadedCallback,
        GoogleMap.OnMapClickListener,
        AutoCompleteAdapter.PlaceAutoCompleteInterface, View.OnClickListener {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    final static int MY_PERMISSIONS_REQUEST_LOCATION = 1233;
    final static int PLAY_SERVICES_REF_REQUEST = 12345;
    final static int DISTANCE = 1000;
    final static String TAG = "InAClick";
    private static final int REQUEST_CHECK_SETTINGS = 10;
    MapView mMapView;
    LocationRequest locationRequest = null;
    GoogleApiClient googleApiClient;
    Location lastLocation = null;
    Circle circleCurrentLocation = null;
    SwitchCompat switchOnlineOffline;
    GeoFire geoFire = null;
    Marker markerCurrentLocation = null;
    DatabaseReference refDriversGeoFire = null;
    Animation animIn, animOut;
    Button btnOpenMenu = null;
    ProgressBar pbChangeDriverStatus;
    double lat, lon;
    private View parentView;
    private ResideMenu resideMenu;
    private GoogleMap googleMap;
    private TextSwitcher textSwitcherStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.frg_driver_map_home, container, false);
        initSwitchGeoFire();
        initUserMap(savedInstanceState);
        checkLocationPermission();
        return parentView;
    }

    private void initSwitchGeoFire() {
        refDriversGeoFire = FirebaseDatabase.getInstance().getReference("Drivers");
        geoFire = new GeoFire(refDriversGeoFire);
        //-----------------------------------------------------------
        switchOnlineOffline = parentView.findViewById(R.id.switchDriverStatus);
        switchOnlineOffline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startLocationUpdates();
                    displayLocation();

                    //Snackbar.make(getView(), "Online", Snackbar.LENGTH_SHORT).show();
                    pbChangeDriverStatus.setVisibility(View.VISIBLE);
                    //changStatusDriverOnMap(true);
                } else {
                    if (markerCurrentLocation != null)
                        markerCurrentLocation.remove();
                    if (circleCurrentLocation != null)
                        circleCurrentLocation.remove();
                    stopLocationUpdates();

                    //Snackbar.make(getView(), "Offline", Snackbar.LENGTH_SHORT).show();
                    pbChangeDriverStatus.setVisibility(View.VISIBLE);
                    changStatusDriverOnMap(false);
                }
            }
        });
        /////////
        textSwitcherStatus = parentView.findViewById(R.id.textSwitcherStatus);
        textSwitcherStatus.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                // create new textView and set the properties like clolr, size etc
                TextView textView = new TextView(getContext());
                textView.setGravity(/*Gravity.TOP |*/ Gravity.CENTER_HORIZONTAL);

                textView.setTextSize(20);
                //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                textView.setTextColor(getResources().getColor(R.color.md_red_400));
                //textView.setTextColor(Color.BLUE);

                //textView.setTypeface(Typeface.DEFAULT_BOLD);

                return textView;
            }
        });
        // Declare the in and out animations and initialize them
        animIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        animOut = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);
        // set the animation type of textSwitcher
        textSwitcherStatus.setInAnimation(animIn);
        textSwitcherStatus.setOutAnimation(animOut);
        textSwitcherStatus.setText("Online");

        //------------------------------
        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        btnOpenMenu = parentView.findViewById(R.id.btnOpenMenu);
        btnOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        ///------------------------
        pbChangeDriverStatus = parentView.findViewById(R.id.pbChangeStatusDriver);
        pbChangeDriverStatus.setVisibility(View.GONE);


    }

    private void changStatusDriverOnMap(final boolean statusOnMap) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Workers");
        final DatabaseReference refDriversGeoFireDatabase = reference.child(DriverSignInOutActivity.TAG_DRIVER)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        refDriversGeoFireDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    UserDriver userDriver = dataSnapshot.getValue(UserDriver.class);

                    //edit the current user driver if exists
                    assert userDriver != null;
                    refDriversGeoFireDatabase.setValue(
                            new UserDriver(userDriver.getUserEmail(),
                                    userDriver.getUserPass(),
                                    userDriver.getUserName(),
                                    userDriver.getUserPhone(),
                                    userDriver.getCity(),
//                                            ((Step5AddCat) item5.getFragment()).getSpinnerModelNumber().getSelectedItem().toString(),
//                                            ((Step5AddCat) item5.getFragment()).getSpinnerCatName().getSelectedItem().toString(),
                                    "2010",
                                    "ونش",
                                    statusOnMap
                            )
                    ).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                if (statusOnMap) {
                                    textSwitcherStatus.setText("Online");
                                    //Toast.makeText(getContext(), "Online", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(getContext(), "Offline", Toast.LENGTH_SHORT).show();
                                    textSwitcherStatus.setText("Offline");
                                }
                                //change the driver status from offline to online on map
                                if (pbChangeDriverStatus != null) {
                                    if (pbChangeDriverStatus.getVisibility() == ProgressBar.VISIBLE)
                                        pbChangeDriverStatus.setVisibility(View.GONE);

                                }
                            }
                        }
                    });
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initUserMap(Bundle savedInstanceState) {

        mMapView = parentView.findViewById(R.id.mapViewDriver);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                handleMap(googleMap);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    private void handleMap(GoogleMap googleMap) {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // For showing a move to my location button
        // Enabling MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);//to-hide-navigation-and-gps-pointer-buttons-when-i-click-the-marker-on-th
        googleMap.getUiSettings().setCompassEnabled(false);

        //setting zoomin for map
        // googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));//set default zoom for map

        //add listeners
        //googleMap.setOnMapLoadedCallback(this);
        googleMap.setOnCameraMoveListener(this);
        googleMap.setOnCameraMoveStartedListener(this);
        googleMap.setOnCameraMoveCanceledListener(this);
        googleMap.setOnCameraIdleListener(this);
        //googleMap.setOnMapClickListener(this);


//        centerMarker = googleMap.addMarker(new MarkerOptions().position(googleMap.getCameraPosition().target)
//                .title("Center of Map")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.set_location_on_map)));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (checkPlayServices()) {
                        buildGoogleApiClient();
                        createLocationRequest();
                        if (switchOnlineOffline.isChecked())
                            displayLocation();

                    }
                }
                // break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                stopLocationUpdates();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            //Toast.makeText(this, "requestPermissions", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Permission exist", Toast.LENGTH_SHORT).show();
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
                if (switchOnlineOffline.isChecked())
                    displayLocation();
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

    private boolean checkPlayServices() {
// Getting Google Play availability status
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            //Google Play Services are available
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_REF_REQUEST).show();
            } else {
                //Toast.makeText(getActivity(), "This device is not supported", Toast.LENGTH_SHORT).show();
                // Google Play Services are not available
                Toast.makeText(getActivity(), "Google Play Services are not available", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }

            return false;
        }

        return true;

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
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(FrgDriverMap.this)
                .addOnConnectionFailedListener(FrgDriverMap.this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build()
        ;
        googleApiClient.connect();
        createLocationRequest();


    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            return;
        }
        //deprecated
        //https://stackoverflow.com/questions/46481789/android-locationservices-fusedlocationapi-deprecated
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//        if (lastLocation != null) {
//            setCurrentLocMarkerOnMap(lastLocation);
//        }

        //2018
        //instead
//        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                // Got last known location. In some rare situations this can be null.
//                if (location != null) {
//                    // Logic to handle location object
//                    setCurrentLocMarkerOnMap(location);
//                }
//            }
//        });

        if (lastLocation != null) {
            if (switchOnlineOffline.isChecked()) {
                lat = lastLocation.getLatitude();
                lon = lastLocation.getLongitude();
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(lat, lon), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {

                            if (markerCurrentLocation != null) {
                                markerCurrentLocation.remove();
                            }
                            getDriverInfo();


                        }
                    });
                } else {
                    Toast.makeText(getContext()
                            , "Null", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }

    private void getDriverInfo() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Workers");
        DatabaseReference refDriversGeoFireDatabase = reference.child(DriverSignInOutActivity.TAG_DRIVER).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        refDriversGeoFireDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    UserDriver userDriver = dataSnapshot.getValue(UserDriver.class);

                    assert userDriver != null;
                    markerCurrentLocation = googleMap.addMarker(new MarkerOptions()
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))//by default
                                    .icon(createAppropIconForThisDriver(userDriver.getCarCatName()))
                                    .title(userDriver.getUserName())
                                    .snippet(userDriver.getCarModelNumber() + userDriver.getCarCatName())
                                    .position(new LatLng(lat, lon))


                    );
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15.0f));
                    //rotateMarker(currentMarker, -360, mMap);
                    Log.e("getCarCatName", userDriver.getCarCatName());
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private BitmapDescriptor createAppropIconForThisDriver(String carCatName) {
        if (carCatName == "ونش")
            return BitmapDescriptorFactory.fromBitmap(scaleDown(BitmapFactory.decodeResource(getResources(), R.drawable.h6), 40, true));
        return null;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }


    private void setCurrentLocMarkerOnMap(Location location) {

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        // Showing the current location in Google Map
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        //Log.i("latitude", "==========" + latitude);
        ////locationTextView holds the address string
        //locationTextView.setText(getCompleteAddressString2(latitude, longitude));
        // create marker
        if (markerCurrentLocation != null) {
            if (markerCurrentLocation.isVisible()) {
                markerCurrentLocation.remove();
                markerCurrentLocation = googleMap.addMarker(new MarkerOptions().position(
                        new LatLng(location.getLatitude(), location.getLongitude()))
                        .flat(true)
                        .title("My Location"));
            } else {

            }
        } else {
            markerCurrentLocation = googleMap.addMarker(new MarkerOptions().position(
                    new LatLng(location.getLatitude(), location.getLongitude()))
                    .flat(true)
                    .title("My Location"));
        }


        // adding marker
//        if (!markerCurrentLocation.isVisible()){
//            googleMap.addMarker(markerCurrentLocation);
//        }

        //draw-transparent-circle-on-top-of-the-google-map
//        CircleOptions circle = new CircleOptions();
//        circle.center(new LatLng(location.getLatitude(), location.getLongitude()))
//                .radius(200)//in meters
//                .strokeColor(Color.BLUE)//border color
//                .strokeWidth(.5f)//border width
//                .fillColor(0x200000ff)
//        ;//inside circle
        //.fillColor(Color.TRANSPARENT);
        if (circleCurrentLocation == null) {
            circleCurrentLocation = googleMap.addCircle(new CircleOptions().center(new LatLng(location.getLatitude(), location.getLongitude()))
                    .radius(200)//in meters
                    .strokeColor(Color.BLUE)//border color
                    .strokeWidth(.5f)//border width
                    .fillColor(0x200000ff));//GoogleMap googleMap(initialize accordingly)
        } else {
            if (circleCurrentLocation.isVisible()) {
                circleCurrentLocation.remove();
                circleCurrentLocation = googleMap.addCircle(new CircleOptions().center(new LatLng(location.getLatitude(), location.getLongitude()))
                        .radius(200)//in meters
                        .strokeColor(Color.BLUE)//border color
                        .strokeWidth(.5f)//border width
                        .fillColor(0x200000ff));//GoogleMap googleMap(initialize accordingly)

            }
        }


    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = googleMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        ///Log.i(TAG, "onConnected");
        displayLocation();
        startLocationUpdates();


    }

    private void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);


    }

    protected void stopLocationUpdates() {
        //Log.i(TAG, "stopLocationUpdates");
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            return;
        }

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
//                .setResultCallback(new ResultCallbacks<Status>() {
//            @Override
//            public void onSuccess(@NonNull Status status) {
//                if (pbChangeDriverStatus != null) pbChangeDriverStatus.setVisibility(View.GONE);
//                textSwitcherStatus.setText("Offline");
//            }
//
//            @Override
//            public void onFailure(@NonNull Status status) {
//
//            }
//        });
    }

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

    @Override
    public void onStop() {


        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                stopLocationUpdates();
            }
        }

        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.i(TAG, "onLocationChanged");
        this.lastLocation = location;
        //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        //updateUI();
        //Toast.makeText(this, getResources().getString(R.string.location_updated_message), Toast.LENGTH_SHORT).show();
        displayLocation();
    }

    @Override
    public void onCameraIdle() {
        //LatLng mapCenterLatLng = googleMap.getCameraPosition().target;///get the center of map
        LatLng mapCenterLatLng = googleMap.getProjection().getVisibleRegion().latLngBounds.getCenter();//get the center of map

        //animateMarker(centerMarker, mapCenterLatLng, false);
//        Toast.makeText(getActivity(), "The camera has stopped moving.",
//                Toast.LENGTH_SHORT).show();

        //String address = getCompleteAddressString(mapCenterLatLng.longitude, mapCenterLatLng.longitude);//Error
//        if (etDest != null) {
//            String pickedPlace = "";
//            List list = getCountryCityNames(mapCenterLatLng.longitude, mapCenterLatLng.longitude);
//            pickedPlace = list.get(0).toString() + " - " + list.get(1).toString() + " - " + list.get(2).toString();
//            etDest.setText(pickedPlace);
//        }


    }

    @Override
    public void onCameraMove() {


    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            ///tvLocationName.setText("Lat " + mapCenterLatLng.latitude + "  Long :" + mapCenterLatLng.longitude);
//            Toast.makeText(getActivity(), "The user gestured on the map.",
//                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "The user gestured on the map.");
            //here


        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
//            Toast.makeText(getActivity(), "The user tapped something on the map.",
//                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "The user tapped something on the map.");
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
//            Toast.makeText(getActivity(), "The app moved the camera.",
//                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "The app moved the camera.");

        }
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.e("Map", "Map clicked");
        ///marker.remove();
//        double latitude = latLng.latitude;
//        double longitude = latLng.longitude;
//


    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public void onPlaceClick(ArrayList<AutoCompleteAdapter.PlaceAutoComplete> mResultList, int position) {

    }
}
