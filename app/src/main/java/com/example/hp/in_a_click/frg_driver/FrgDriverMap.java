package com.example.hp.in_a_click.frg_driver;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.media.MediaPlayer;
import android.net.Uri;
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
import com.arsy.maps_library.MapRipple;
import com.bumptech.glide.Glide;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.hp.in_a_click.R;
import com.example.hp.in_a_click.adapters.AutoCompleteAdapter;
import com.example.hp.in_a_click.adapters.CoverFlowAdapter;
import com.example.hp.in_a_click.frg_user.FrgUserMap;
import com.example.hp.in_a_click.frg_worker_proc.Step5AddCat;
import com.example.hp.in_a_click.model.GameEntity;
import com.example.hp.in_a_click.model.UserDriver;
import com.example.hp.in_a_click.model.UserNormal;
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
import com.google.android.gms.maps.CameraUpdate;
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
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
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
import com.hanks.htextview.HTextView;
import com.mapbox.mapboxsdk.Mapbox;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.skyfishjy.library.RippleBackground;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.hp.in_a_click.R.raw.notice;


public class FrgDriverMap extends Fragment implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnMapLoadedCallback,
        GoogleMap.OnMapClickListener,
        AutoCompleteAdapter.PlaceAutoCompleteInterface, View.OnClickListener,
        RoutingListener {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    final static int MY_PERMISSIONS_REQUEST_LOCATION = 1233;
    final static int PLAY_SERVICES_REF_REQUEST = 12345;
    final static int DISTANCE = 1000;
    final static String TAG = "InAClick";
    private static final int REQUEST_CHECK_SETTINGS = 10;
    MapView mapViewDriver;
    //com.mapbox.mapboxsdk.maps.MapView mapViewNavigation = null;
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
    Context context = null;
    private View parentView;
    private ResideMenu resideMenu;
    private GoogleMap googleMap;
    private TextSwitcher textSwitcherStatus;
    private View viewCustomerInfo = null;

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        parentView = inflater.inflate(R.layout.frg_driver_map_home, container, false);
        initSwitchGeoFire();
        initUserMap(savedInstanceState);
        checkLocationPermission();
        getAssignedCustomer();
        initCustomerInfoView();
        initAnimations();
        //initNoticeVars();
        //runNoticeProgress();
        //initMapBoxNavigationSDK(savedInstanceState);
        //testStartNavigation();
        return parentView;
    }

    private void testStartNavigation() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?" +
                        "saddr=" + 29.980390 + "," + 31.181483 +
                        "&daddr=" + 30.004476 + "," + 31.184744));
        intent.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNoticeVars(view);
        initBtnStartNavigation(view);
        initStartTripTextView(view);
        initAlreadyArrivedButton(view);


    }

    Button btnAlreadyArrived = null;

    private void initAlreadyArrivedButton(View parentView) {
        btnAlreadyArrived = parentView.findViewById(R.id.btnArrivedAlready);
        btnAlreadyArrived.setVisibility(View.INVISIBLE);
        btnAlreadyArrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvStartTripNow.setVisibility(View.VISIBLE);
            }
        });

    }

    HTextView tvStartTripNow = null;

    private void initStartTripTextView(View view) {
        tvStartTripNow = view.findViewById(R.id.textStartTripNow);
        tvStartTripNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        tvStartTripNow.setVisibility(View.INVISIBLE);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    Button btnStartNavigation = null;

    private void initBtnStartNavigation(View view) {
        btnStartNavigation = view.findViewById(R.id.btnStartNavigation);
        btnStartNavigation.setVisibility(View.GONE);
        btnStartNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?" +
                                "saddr=" + lastLocation.getLatitude() + "," + lastLocation.getLongitude() +
                                "&daddr=" + latLngUser.latitude + "," + latLngUser.longitude));
                intent.setClassName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    ImageView ivUserPhoto;
    TextView tvUserName, tvUserPhoneNumber;

    private void initCustomerInfoView() {
        viewCustomerInfo = parentView.findViewById(R.id.layoutCustomerInfo);
        tvUserName = viewCustomerInfo.findViewById(R.id.tvUserName);
        tvUserPhoneNumber = viewCustomerInfo.findViewById(R.id.tvUserPhoneNumber);
        viewCustomerInfo.setVisibility(View.GONE);
        tvUserName.setText("");
        tvUserPhoneNumber.setText("");


    }

    Animation animBottomTop, animTopBottom;

    private void initAnimations() {
        animBottomTop = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
        animTopBottom = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_bottom);

    }

    private void initMapBoxNavigationSDK(Bundle savedInstanceState) {

//        Mapbox.getInstance(getActivity(),
//                getResources().getString(R.string.mapbox_access_token));
//
//        mapViewNavigation = parentView.findViewById(R.id.mapViewNavigation);
//        mapViewNavigation.onCreate(savedInstanceState);
//        mapViewDriver.onResume();


    }

    View circularPbarLayout = null;
    RippleBackground rippleBackground = null;

    private void initNoticeVars(View parentView) {
        circularProgressBar = parentView.findViewById(R.id.layoutProgressDriverHaveWork).findViewById(R.id.pbWorkAlert);
        circularPbarLayout = parentView.findViewById(R.id.layoutProgressDriverHaveWork);
//        circularProgressBar.setColor(ContextCompat.getColor(this, R.color.progressBarColor));
//        circularProgressBar.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundProgressBarColor));
//        circularProgressBar.setProgressBarWidth(getResources().getDimension(R.dimen.progressBarWidth));
//        circularProgressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen.backgroundProgressBarWidth));
        ///int animationDuration = 1000 * 15; // 2500ms = 2,5s
        ///circularProgressBar.setProgressWithAnimation(100, animationDuration); // Default duration = 1500ms
        parentView.findViewById(R.id.layoutProgressDriverHaveWork).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                stopHandler = false;
                circularPbarLayout.setVisibility(View.GONE);

                passDriverIdToCustomer();
                drawLineBetweenDriverAndUser();

                checkRequestStatus();
                fire = true;

                //start checking if the driver arrived to user
                boStartTrip = true;


//                btnStartNavigation.setVisibility(View.VISIBLE);
//                btnStartNavigation.startAnimation(animIn);

            }
        });


        mp = MediaPlayer.create(context, R.raw.notice);
        handler = new Handler();

        rippleBackground = circularPbarLayout.findViewById(R.id.rippleBg);


    }

    LatLng latLngDriver = null;

    private void drawLineBetweenDriverAndUser() {
        latLngDriver = new LatLng(this.lastLocation.getLatitude(), this.lastLocation.getLongitude());
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(latLngDriver, latLngUser)
                .build();
        routing.execute();

    }

    private void passDriverIdToCustomer() {
        final DatabaseReference reference = FirebaseDatabase
                .getInstance()
                .getReference("Users").child(customerId)
                //.child("driverId")
                ;
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserNormal userNormal = dataSnapshot.getValue(UserNormal.class);
                        reference.setValue(new UserNormal(userNormal.getUserEmail(),
                                userNormal.getUserPass(), userNormal.getUserName(),
                                userNormal.getUserPhone(), userNormal.getCity(),
                                firebaseUser.getUid(), userNormal.getUserType(),
                                userNormal.getUserPhotoUrl()));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }

    Handler handler = null;
    Runnable runnable = null;
    boolean stopHandler = true;
    int count = 10;
    MediaPlayer mp;
    CircularProgressBar circularProgressBar = null;

    private void runNoticeProgress() {
        circularPbarLayout.setVisibility(View.VISIBLE);
        final int delay = 1500;
        runnable = new Runnable() {
            @Override
            public void run() {
                if (stopHandler) {
                    count += 10;
                    circularProgressBar.setProgressWithAnimation(count, delay);
                    mp.start();
                    if (count == 100) {
                        handler.removeCallbacks(runnable);
                        stopHandler = false;
                        circularPbarLayout.setVisibility(View.GONE);
                        //the driver doesn't accept

                    }
                    handler.postDelayed(runnable, delay);// move this inside the run method
                }
            }
        };
        runnable.run(); // missing

        //----------------------start ripple animation
        rippleBackground.startRippleAnimation();


    }

    String customerId = "";
    LatLng latLngUser = null;
    ValueEventListener listenerGetAssignedCustomer = null;
    DatabaseReference refGetAssignedCustomer = null;

    private void getAssignedCustomer() {
        refGetAssignedCustomer = FirebaseDatabase.getInstance().getReference().child("Workers")
                .child(DriverSignInOutActivity.TAG_DRIVER)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
        /// .child("passengerId")
        ;
        listenerGetAssignedCustomer = refGetAssignedCustomer
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                            HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
//                    assert map != null;
                            if (map.get("passengerId") != null) {
                                customerId = map.get("passengerId").toString();
//                        getAssignedCustomerPickupLocation(customerId);
                                //customerId = dataSnapshot.getValue(String.class);
                                if (customerId != "" && map.get("passengerId") != null) {
                                    getAssignedCustomerPickupLocation(customerId);
                                    getAssignedCustomerInfo();

                                }
                            }
                        } else {
                            customerId = "";
                            erasePloylines();
                            //notice if the user cancel the request
                            //delete the picked up marker
                            if (markerPickedUpLocation != null) {
                                markerPickedUpLocation.remove();
                            }
                            refGetAssignedCustomer.removeEventListener(listenerGetAssignedCustomer);
                            addDriverAvailablitiy();

                            //disappear the view customer info
                            viewCustomerInfo.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void getAssignedCustomerInfo() {
        if (customerId == "") return;
        DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(customerId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    HashMap<String, Object> map = null;
                    map = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (map != null) {
                        /////show the bottom view of the user info
                        viewCustomerInfo.setVisibility(View.VISIBLE);
                        viewCustomerInfo.startAnimation(animBottomTop);
                        ////access the user info
                        if (map.get("userName") != null) {
                            tvUserName.setText(map.get("userName").toString());
                        }
                        if (map.get("userPhone") != null) {
                            tvUserPhoneNumber.setText(map.get("userPhone").toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    Marker markerPickedUpLocation = null;

    private void getAssignedCustomerPickupLocation(final String customerId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("RequestsCars").child(customerId).child("l")
                //.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                //.child("passengerId")
                ;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !customerId.equals("")) {
                    List<Object> objects = (List<Object>) dataSnapshot.getValue();
                    double lat = 0, lon = 0;
                    assert objects != null;
                    if (objects.get(0) != null) {
                        lat = Double.parseDouble(objects.get(0).toString());
                    }
                    if (objects.get(1) != null) {
                        lon = Double.parseDouble(objects.get(1).toString());
                    }
                    latLngUser = new LatLng(lat, lon);
                    markerPickedUpLocation = googleMap.addMarker(new MarkerOptions().position(latLngUser).title("PickedUp Location"));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            latLngUser, 15.0f));
                    runNoticeProgress();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setStatusDriverOnMap() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Workers");
        final DatabaseReference refDriversGeoFireDatabase = reference.child(DriverSignInOutActivity.TAG_DRIVER)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        refDriversGeoFireDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    UserDriver userDriver = dataSnapshot.getValue(UserDriver.class);

                    //set current status that has been before at last time
                    assert userDriver != null;
                    if (userDriver.isDriverStatus()) {
                        textSwitcherStatus.setText("Online");
                        switchOnlineOffline.setChecked(true);
                        //showDialogMessage(context, "You are online now, change the status if u want");
                    } else {
                        textSwitcherStatus.setText("Offline");
                        switchOnlineOffline.setChecked(false);
                        //showDialogMessage(context, "You are offline now, change the status if u want");
//                        if (markerCurrentLocation != null)
//                            markerCurrentLocation.remove();
//                        if (circleCurrentLocation != null)
//                            circleCurrentLocation.remove();
//                        stopLocationUpdates();
                    }

                    //refDriversGeoFireDatabase.removeEventListener(this);

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void showDialogMessage(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    boolean connected = false;

    private void initSwitchGeoFire() {
        context = getContext();
        ////////////////////////////////
        refDriversGeoFire = FirebaseDatabase.getInstance().getReference("DriversAvailable");
        geoFire = new GeoFire(refDriversGeoFire);
        //-----------------------------------------------------------
        switchOnlineOffline = parentView.findViewById(R.id.switchDriverStatus);
        setStatusDriverOnMap();
        switchOnlineOffline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (connected) {
                    if (isChecked) {
                        pbChangeDriverStatus.setVisibility(View.VISIBLE);
                        startLocationUpdates();
                        displayLocation();
                        changStatusDriverOnMap(true);

                    } else {
                        pbChangeDriverStatus.setVisibility(View.VISIBLE);
//                    if (markerCurrentLocation != null)
//                        markerCurrentLocation.remove();
//                    if (circleCurrentLocation != null)
//                        circleCurrentLocation.remove();
                        //stopLocationUpdates();
                        changStatusDriverOnMap(false);
                        removeGeoFire();
                        //startLocationUpdates();
                        //displayLocation();
                    }
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
        final DatabaseReference refDriversGeoFireDatabase =
                reference.child(DriverSignInOutActivity.TAG_DRIVER)
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
                                    userDriver.getCarModelNumber(),
                                    userDriver.getCarCatName(),
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


                    refDriversGeoFireDatabase.removeEventListener(this);

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initUserMap(Bundle savedInstanceState) {

        mapViewDriver = parentView.findViewById(R.id.mapViewDriver);
        mapViewDriver.onCreate(savedInstanceState);
        mapViewDriver.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapViewDriver.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                handleMap(googleMap);
            }
        });

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
        googleMap.setMyLocationEnabled(true);
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

    MapRipple mapRippleCurrent = null;
    int withStrokewidth = 5, withRippleDuration = 10000;
    int withNumberOfRipples = 6;
    int withDistance = 15;

    private void addRadarOnMapCurrent(Context context, LatLng latLng) {
        mapRippleCurrent = new MapRipple(googleMap, latLng, context);
        mapRippleCurrent.withNumberOfRipples(withNumberOfRipples);
//            mapRippleCurrent.withFillColor(Color.parseColor("#FFA3D2E4"));
        mapRippleCurrent.withStrokeColor(Color.BLACK);
        mapRippleCurrent.withStrokewidth(withStrokewidth);      // 10dp
        mapRippleCurrent.withDistance(withDistance);      // 2000 metres radius
        mapRippleCurrent.withRippleDuration(withRippleDuration);    //12000ms
//            mapRippleCurrent.withTransparency(0.5f);
        mapRippleCurrent.startRippleMapAnimation();
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
    public void onStart() {
        super.onStart();
//        mapViewNavigation.onStart();
        mapViewDriver.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }

//        if (switchOnlineOffline != null) {
//            if (switchOnlineOffline.isChecked())
//                addDriverAvailablitiy();
//        }
    }

    private void addDriverAvailablitiy() {
        GeoFire geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference("DriversAvailable"));
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null || lastLocation == null) return;
        geoFire.setLocation(firebaseUser.getUid(), new GeoLocation(lastLocation.getLatitude(),
                lastLocation.getLongitude()));

    }

    @Override
    public void onResume() {
        super.onResume();
        mapViewDriver.onResume();
//       mapViewNavigation.onResume();
//        if (!switchOnlineOffline.isChecked()){
//            removeGeoFire();
//        }else{
//
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapViewDriver.onPause();
//       mapViewNavigation.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                stopLocationUpdates();
            }
        }
        if (listenerGetAssignedCustomer != null)
            refGetAssignedCustomer.removeEventListener(listenerGetAssignedCustomer);

        stopRippleRadar();


    }

    private void stopRippleRadar() {
        if (mapRippleCurrent != null) {
            if (mapRippleCurrent.isAnimationRunning()) {
                mapRippleCurrent.stopRippleMapAnimation();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapViewDriver.onDestroy();
//       mapViewNavigation.onDestroy();

        if (listenerGetAssignedCustomer != null)
            refGetAssignedCustomer.removeEventListener(listenerGetAssignedCustomer);

        stopRippleRadar();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapViewDriver.onLowMemory();
//       mapViewNavigation.onLowMemory();

    }

    @Override
    public void onStop() {
        super.onStop();
//        mapViewNavigation.onStop();
        mapViewDriver.onStop();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                stopLocationUpdates();
            }
        }
        ///removeGeoFire();
        if (listenerGetAssignedCustomer != null)
            refGetAssignedCustomer.removeEventListener(listenerGetAssignedCustomer);
        stopRippleRadar();


    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        mapViewDriver.onSaveInstanceState(bundle);
//        mapViewNavigation.onSaveInstanceState(bundle);
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

    private synchronized void buildGoogleApiClient() {

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
        if (ActivityCompat/*ContextCompat*/.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat/*ContextCompat*//*getActivity()*/
                .checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
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

        getDriverInfo();


    }

    ValueEventListener listenereDriverStatus = null;
    DatabaseReference refDriversGeoFireDatabase = null;

    private void getDriverInfo() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Workers");
        refDriversGeoFireDatabase = reference.child(DriverSignInOutActivity.TAG_DRIVER)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        if (listenereDriverStatus != null)
//            refDriversGeoFireDatabase.removeEventListener(listenerGetAssignedCustomer);
        listenereDriverStatus = refDriversGeoFireDatabase
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            UserDriver userDriver = dataSnapshot.getValue(UserDriver.class);

                            if (lastLocation != null) {
                                lat = lastLocation.getLatitude();
                                lon = lastLocation.getLongitude();
                                if (userDriver.isDriverStatus()) {
                                    //check if it is in driversworking ref, if yes - > don't add driversAvailable
                                    //if not add its availability
                                    FirebaseDatabase.getInstance().getReference("DriversWorking")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                                                        //don't add it as available
                                                    } else {
                                                        geoFire.setLocation(FirebaseAuth.getInstance().
                                                                getCurrentUser().getUid(), new GeoLocation(lat, lon), new GeoFire.CompletionListener() {
                                                            @Override
                                                            public void onComplete(String key, DatabaseError error) {

                                                                //changStatusDriverOnMap(true);
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                }


                                if (markerCurrentLocation != null) {
                                    markerCurrentLocation.remove();
                                }

                                if (/*switchOnlineOffline.isChecked()*/userDriver.isDriverStatus()) {
                                    markerCurrentLocation = googleMap.addMarker(new MarkerOptions()
                                                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                                                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))//by default
                                                    .icon(createAppropIconForThisDriver(userDriver.getCarCatName()))
                                                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.user))
                                                    .title(userDriver.getUserName())
                                                    .snippet(setCarCatName(userDriver.getCarCatName()))
                                                    //.snippet(userDriver.getCarModelNumber() + "  " + userDriver.getCarCatName())
                                                    .position(new LatLng(lat, lon))
                                                    .flat(false)
                                            //.draggable(false)


                                    );
                                    //Log.e("DrivUserName", userDriver.getUserEmail() + userDriver.getUserName());

                                } else {
                                    markerCurrentLocation = googleMap.addMarker(new MarkerOptions()
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.invisible))
                                                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))//by default
                                                    //.icon(createAppropIconForThisDriver(userDriver.getCarCatName())
                                                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.user))
                                                    .title(userDriver.getUserName())
                                                    .snippet(setCarCatName(userDriver.getCarCatName()))
                                                    .position(new LatLng(lat, lon))
                                                    .flat(false)
                                            //.draggable(false)


                                    );

                                }
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lat, lon), 15.0f));
                                //rotateMarker(currentMarker, -360, mMap);
                                //Log.e("getCarCatName", userDriver.getCarCatName());
                                if (mapRippleCurrent != null) {
                                    mapRippleCurrent.stopRippleMapAnimation();//stop last
                                }
                                //then create a new one
                                addRadarOnMapCurrent(getActivity(),
                                        new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
                            }

                        }
                        //refDriversGeoFireDatabase.removeEventListener(this);


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    private String setCarCatName(String carCatName) {
        String catName = "";
        switch (carCatName) {
            case FrgDriverVehicles.CAT_CHEAP:
                catName = "إقتصاديه";
                break;
            case FrgDriverVehicles.CAT_NEEDS:
                catName = "زوى الاحتياجات الخاصه";
                break;
            case FrgDriverVehicles.CAT_FAMILY:
                catName = "عائليه";
                break;
            case FrgDriverVehicles.CAT_TRANSPORT:
                catName = "سياره نقل";
                break;
            case FrgDriverVehicles.CAT_WENSH:
                catName = "ونش";
                break;
            case FrgDriverVehicles.CAT_HIGH:
                catName = "فاخره";
                break;
        }

        return catName;
    }

    private BitmapDescriptor createAppropIconForThisDriver(String carCatName) {
        float imgSize = 70;
        if (carCatName == FrgDriverVehicles.CAT_WENSH/*.toString().trim()*/) {
            Log.e("getCarCatName", "CAT_WENSH");
            //return BitmapDescriptorFactory.fromBitmap(scaleDown(BitmapFactory.decodeResource(getResources(), R.drawable.h6), imgSize, true));
            return BitmapDescriptorFactory.fromResource(R.drawable.h66);
        }
        if (carCatName == FrgDriverVehicles.CAT_FAMILY) {
            Log.e("getCarCatName", "CAT_FAMILY");
            return BitmapDescriptorFactory.fromBitmap(scaleDown(BitmapFactory.decodeResource(getResources(), R.drawable.h2), imgSize, true));
        }

        if (carCatName == FrgDriverVehicles.CAT_HIGH) {
            Log.e("getCarCatName", "CAT_HIGH");
            return BitmapDescriptorFactory.fromBitmap(scaleDown(BitmapFactory.decodeResource(getResources(), R.drawable.h1), imgSize, true));
        }

        if (carCatName == FrgDriverVehicles.CAT_CHEAP) {
            Log.e("getCarCatName", "CAT_CHEAP");
            return BitmapDescriptorFactory.fromBitmap(scaleDown(BitmapFactory.decodeResource(getResources(), R.drawable.h3), imgSize, true));
        }

        if (carCatName == FrgDriverVehicles.CAT_NEEDS) {
            Log.e("getCarCatName", "CAT_NEEDS");
            return BitmapDescriptorFactory.fromBitmap(scaleDown(BitmapFactory.decodeResource(getResources(), R.drawable.h4), imgSize, true));
        }

        if (carCatName == FrgDriverVehicles.CAT_TRANSPORT) {
            Log.e("getCarCatName", "CAT_TRANSPORT");
            return BitmapDescriptorFactory.fromBitmap(scaleDown(BitmapFactory.decodeResource(getResources(), R.drawable.h5), imgSize, true));
        }


        return null;
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
        connected = true;

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
        if (googleApiClient.isConnected())
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

    private void removeGeoFire() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference refDriversGeoFire = FirebaseDatabase.getInstance().getReference("DriversAvailable");
        GeoFire geoFire = new GeoFire(refDriversGeoFire);
        geoFire.removeLocation(userId);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //removeGeoFire();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    boolean fire = false;

    @Override
    public void onLocationChanged(Location location) {
        //Log.i(TAG, "onLocationChanged");
        this.lastLocation = location;
        //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        //updateUI();
        //Toast.makeText(this, getResources().getString(R.string.location_updated_message), Toast.LENGTH_SHORT).show();
        checkIfDriverArrivedToUser();
        displayLocation();
        if (fire) checkRequestStatus();
    }

    boolean boStartTrip = false;

    private void checkIfDriverArrivedToUser() {
        if (boStartTrip) {
            if (lastLocation != null && latLngUser != null) {
                //calculate the distance between the user and driver
                Location locationDriver = new Location(""),
                        locationUser = new Location("");
                locationUser.setLatitude(latLngUser.latitude);
                locationUser.setLongitude(latLngUser.longitude);

                locationDriver.setLatitude(lastLocation.getLatitude());
                locationDriver.setLongitude(lastLocation.getLongitude());

                float distance = locationDriver.distanceTo(locationUser);

                if (distance < 100) {
                    btnAlreadyArrived.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    private void checkRequestStatus() {
        DatabaseReference refDriversAvailable = FirebaseDatabase.getInstance().getReference("DriversAvailable"),
                //.getReference("Workers").child(DriverSignInOutActivity.TAG_DRIVER),//available drivers
                refDriversWorking = FirebaseDatabase.getInstance().getReference("DriversWorking");
        String driverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        GeoFire geoFireAvailable = new GeoFire(refDriversAvailable), geoFireWorking = new GeoFire(refDriversWorking);

        switch (customerId) {
            case "":
                geoFireWorking.removeLocation(driverId);
                geoFireAvailable.setLocation(driverId, new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()));
                break;
            default:
                geoFireAvailable.removeLocation(driverId);
                geoFireWorking.setLocation(driverId, new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()));
                break;
        }

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

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Polyline> polylines;

    @Override
    public void onRoutingStart() {
    }

    private static final int[] COLORS = new int[]
            {R.color.primary_dark,
                    R.color.primary,
                    R.color.primary_light, R.color.accent, R.color.primary_dark_material_light};

    @Override
    public void onRoutingSuccess(ArrayList<Route> routes, int pos) {
//        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
//
//        googleMap.moveCamera(center);


        if (polylines != null) {
            if (polylines.size() > 0) {
                for (Polyline poly : polylines) {
                    poly.remove();
                }
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < routes.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(routes.get(i).getPoints());
            Polyline polyline = googleMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getActivity(),
                    "Route " + (i + 1) + ": distance - " +
                            routes.get(i).getDistanceValue() + ": duration - " +
                            routes.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }

        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(latLngDriver);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
        googleMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(latLngUser);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green));
        googleMap.addMarker(options);
    }

    @Override
    public void onRoutingCancelled() {

    }

    private void erasePloylines() {
        if (polylines != null) {
            if (!polylines.isEmpty()) {
                for (Polyline polyline : polylines
                        ) {
                    polyline.remove();
                }
                polylines.clear();
            }
        }
    }

}
