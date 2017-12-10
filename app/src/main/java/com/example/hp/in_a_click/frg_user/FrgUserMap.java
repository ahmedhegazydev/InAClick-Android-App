package com.example.hp.in_a_click.frg_user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.hp.in_a_click.frg_driver.FrgDriverVehicles;
import com.example.hp.in_a_click.model.GameEntity;
import com.example.hp.in_a_click.model.UserNormal;
import com.example.hp.in_a_click.residemenu.MenuActivity;
import com.example.hp.in_a_click.signinout.DriverSignInOutActivity;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


public class FrgUserMap extends Fragment implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnMapLoadedCallback,
        GoogleMap.OnMapClickListener,
        AutoCompleteAdapter.PlaceAutoCompleteInterface, View.OnClickListener {

    /////////////////////
    public final static String KIND_CAR_OR_HOME = "CAR_OR_HOME";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    final static int MY_PERMISSIONS_REQUEST_LOCATION = 1233;
    final static int PLAY_SERVICES_REF_REQUEST = 12345;
    final static int DISTANCE = 1000;
    final static String TAG = "InAClick";
    private static final int REQUEST_CHECK_SETTINGS = 10;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    MapView mMapView;
    ImageView ivOrderCar, ivOrderHome;
    LinearLayout llOrderUser;
    Animation animScaleDown, animScaleUp;
    Button btnOpenMenu;
    BottomSheetBehavior<View> bottomSheetBehavior;
    Context context = null;
    String carOrHoemConst = "";
    ImageView ivCloseDialog;
    Button btnHideDialog = null;
    View viewBottomSheet;
    CoverFlowAdapter mAdapter;
    Animation in, out;
    FeatureCoverFlow mCoverFlow;
    View viewWhereTo;
    Animation animDownTop;
    BottomSheetBehavior<View> bsbWhereTo;
    View viewBottomSheetWhereTo;
    int h, w;
    CardView cardViewCurAndDest;
    int heightOfWhereTo = 0;
    LocationRequest locationRequest = null;
    GoogleApiClient googleApiClient;
    Location location = null;
    TextView tvCurrentLoc;
    Marker centerMarker;
    EditText etDest;
    Marker marker;
    ImageView ivClearCrossIcon;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() != 0) {
                //adjustingTheEdittextTextSize(s.toString());
            }

            if (s.toString().trim().length() == 0) {
                ivClearCrossIcon.setVisibility(View.INVISIBLE);
            } else {
                ivClearCrossIcon.setVisibility(View.VISIBLE);
            }
        }
    };
    boolean clickedFrom = false, clickedTo = true;
    BottomSheetBehavior<View> bsbAddHome;
    View viewAddHomeBottomSheet, viewAddHomeBoss, addHomeAbove;
    AutoCompleteAdapter autoCompleteAdapter = null;
    Handler handler = new Handler();
    MaterialProgressBar pbAddHome = null;
    HandlerThread mHandlerThread;
    Handler mThreadHandler = new Handler();
    PlacesAutoCompleteAdapter placesAutoCompleteAdapter = null;
    ArrayList<LocationItem> listLocations = new ArrayList<LocationItem>();
    RelativeLayout rlRootMainView;
    Marker markerCurrentLocation = null;
    Circle circleCurrentLocation = null;
    private View parentView;
    private ResideMenu resideMenu;
    private GoogleMap googleMap;
    ////////////////////////////////CoverFlow/**/
    private TextSwitcher mTitle;
    private ArrayList<GameEntity> listCarsItems = new ArrayList<>(0),
            listHomesItems = new ArrayList<>();
    private FusedLocationProviderClient mFusedLocationClient;

    public static int getViewHeight(View view) {
        WindowManager wm =
                (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int deviceWidth;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            deviceWidth = size.x;
        } else {
            deviceWidth = display.getWidth();
        }

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        return view.getMeasuredHeight(); //        view.getMeasuredWidth();
    }

    private void adjustingTheEdittextTextSize(String s) {

        //getting the string length
        int len = s.length();
        //getting the EditText view w
        final ViewTreeObserver viewTreeObserver = etDest.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    etDest.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    etDest.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = etDest.getMeasuredWidth();
                int heightOfWhereTo = etDest.getMeasuredHeight();
                //Log.e("WidthAndHeight", width + "  ->   " + heightOfWhereTo);


            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.frg_user_map, container, false);
        //setUpViews();
        init();
        //initCoverFlow();
        initDialog();
        initUserMap(savedInstanceState);
        initLayoutWhereTo();
        initWhereToBSB();
        //getScreenSize();
        checkLocationPermission();
        initAddHomeWorkBottomSheet();
        checkIfDriverAcceptMe();
        initCancelReqLayout();
        return parentView;
    }

    View layoutCancelReq = null;

    private void initCancelReqLayout() {
        layoutCancelReq = parentView.findViewById(R.id.userCancelRequest);
        layoutCancelReq.setVisibility(View.GONE);
        layoutCancelReq.findViewById(R.id.btnCancelRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequest();
            }
        });
        layoutCancelReq.findViewById(R.id.ivClose)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layoutCancelReq.setVisibility(View.GONE);
                        layoutCancelReq.startAnimation(animScaleDown);
                    }
                });

    }

    boolean boRequest = true;

    private void cancelRequest() {
        final SpotsDialog spotsDialog = new SpotsDialog(getActivity(), "Withdrawing request ... ");
        spotsDialog.show();

        //remove listeners
        boRequest = false;
        //geoQueryClosestDriver.removeAllListeners();
        referDriversWorking.removeEventListener(listenerDriverAvailable);

        //set this driver available after working
        GeoFire geoFireDriverAvailable = new GeoFire(FirebaseDatabase.getInstance().getReference("DriversAvailable")),
                geoFireDriverWorking = new GeoFire(FirebaseDatabase.getInstance().getReference("DriversWorking").child(driverKey));
        geoFireDriverAvailable.removeLocation(driverKey);
        geoFireDriverWorking.setLocation(driverKey, new GeoLocation(latLngDriver.latitude, latLngDriver.longitude));
        ////////////////////
        //then delete the customer request // it will working also
//        FirebaseDatabase.getInstance().getReference("RequestsCars")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(context, "Withdrawn successful", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        GeoFire geoFireCarRequest = new GeoFire(FirebaseDatabase.getInstance().getReference("RequestsCars"));
        geoFireCarRequest.removeLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (spotsDialog.isShowing()) spotsDialog.dismiss();
            }
        });

        //////////////////////
        //then delete passenger id from this driver
        // as he will be has no user
        if (driverKey != "") {
            FirebaseDatabase.getInstance().getReference("Workers").child(DriverSignInOutActivity.TAG_DRIVER)
                    .child(driverKey).child("passengerId").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Withdrawn successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            driverKey = "";
        }
        r = .5;
        driverFound = false;


    }

    String driverId = "";

    private void checkIfDriverAcceptMe() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("driverId")) {
                    reference.child("driverId").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Log.e("DriverAccep", "DriverAccep");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showDriverAcceptanceAlert() {
        Toast.makeText(getActivity(), "Driver Accepted", Toast.LENGTH_SHORT).show();

    }

    int distance = 1, LIMIT = 10;

    private void loadAllAvailableDrivers() {


        //load all available drivers in distance 3 km
//        DatabaseReference refAllDrivers = FirebaseDatabase.getInstance().getReference("Locations");
        GeoFire geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference("DriversAvailable"));
        GeoQuery geoQueryAllDriversWithDist = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), distance);
        geoQueryAllDriversWithDist.removeAllListeners();
        geoQueryAllDriversWithDist.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, final GeoLocation location) {

                //add marker to all riders or drivers
                googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(location.latitude, location.longitude))
                                .title("Driver")
                                //.snippet(inAClickUser.getUserName())
                                .flat(false)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                );


            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (distance <= LIMIT) {
                    distance++;
                    loadAllAvailableDrivers();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }

    private void initAddHomeWorkBottomSheet() {


        //init view
        addHomeAbove = parentView.findViewById(R.id.llAddHomeWork);
        //viewAddHomeBoss = LayoutInflater.from(getActivity()).inflate(R.layout.add_home, null);
        viewAddHomeBottomSheet = parentView.findViewById(R.id.bottomSheetAddWork);
        bsbAddHome = BottomSheetBehavior.from(viewAddHomeBottomSheet);
        bsbAddHome.setHideable(true);
        bsbAddHome.setState(BottomSheetBehavior.STATE_HIDDEN);
        bsbAddHome.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {

                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        Log.d("BSB", "collapsed");
                    }
                    case BottomSheetBehavior.STATE_SETTLING: {

                        Log.d("BSB", "settling");
                    }
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        Log.d("BSB", "expanded");

                    }
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        Log.d("BSB", "hidden");
                    }
                    case BottomSheetBehavior.STATE_DRAGGING: {

                        Log.d("BSB", "dragging");
                    }

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d("BSB", "sliding " + slideOffset);


            }

        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        ListView listViewLocations = viewAddHomeBottomSheet.findViewById(R.id.listViewLocations);
//        ListViewAdapter listViewAdapter = new ListViewAdapter(getActivity(), listLocations);
//        listViewLocations.setAdapter(listViewAdapter);
        RecyclerView listViewLocations = viewAddHomeBottomSheet.findViewById(R.id.rvLocations);
        listViewLocations.setLayoutManager(layoutManager);
        ///placesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getActivity(), R.layout.layout_list_item);
        autoCompleteAdapter = new AutoCompleteAdapter(getActivity(), R.layout.layout_list_item, googleApiClient, BOUNDS_GREATER_SYDNEY,
                null, this);
        listViewLocations.setAdapter(autoCompleteAdapter);


//        listViewLocations.setAdapter(placesAutoCompleteAdapter);
//        String TAG = FrgUserMap.class.getSimpleName();
//        if (mThreadHandler == null) {
//
//            // Initialize and start the HandlerThread
//            // which is basically a Thread with a Looper
//            // attached (hence a MessageQueue)
//            mHandlerThread = new HandlerThread(TAG, android.os.Process.THREAD_PRIORITY_BACKGROUND);
//            mHandlerThread.start();
//
//            // Initialize the Handler
//            mThreadHandler = new Handler(mHandlerThread.getLooper()) {
//                @Override
//                public void handleMessage(Message msg) {
//                    if (msg.what == 1) {
//                        ArrayList<LocationItem> results = placesAutoCompleteAdapter.locationItems;
//                        if (results != null && results.size() > 0) {
//                            placesAutoCompleteAdapter.notifyDataSetChanged();
//                        } else {
//                            placesAutoCompleteAdapter.notifyDataSetInvalidated();
//                        }
//                    }
//                }
//            };
//        }

        //add view to root
        //rlRootMainView.addView(viewAddHomeBoss);
        //Access view
        //get the edittext from view
        EditText editText = addHomeAbove.findViewById(R.id.etEnterHome);
//        AutoCompleteTextView autoCompleteTextView = addHomeAbove.findViewById(R.id.autoCompTextView);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
//                    doOnTextChanged(s.toString());
//                    doOnTextChanged2(s.toString());
                }
                if (!s.equals("")) {
                    if (googleApiClient.isConnected()) {
                        autoCompleteAdapter.getFilter().filter(s.toString());
                    } else {
                        Log.e(TAG, "API  NOT CONNECTED");
                    }

                } else {
                    autoCompleteAdapter.mResultList.clear();//make mResultList as public
                    autoCompleteAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() != 0) {
                    //doAfterTextChanged(s.toString());

                }
            }
        });
        pbAddHome = addHomeAbove.findViewById(R.id.pbAddHome);


        //set as not visibile firstly
        //viewAddHomeBoss.setVisibility(View.INVISIBLE);


    }

    private void doOnTextChanged2(String s) {
        Runnable run = new Runnable() {


            @Override
            public void run() {

            }

        };

        // only canceling the network calls will not help, you need to remove all callbacks as well
        // otherwise the pending callbacks and messages will again invoke the handler and will send the request
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        } else {
            handler = new Handler();
        }
        handler.postDelayed(run, 1000);


    }

    private void doOnTextChanged(final String value) {

        mThreadHandler.removeCallbacksAndMessages(null);
        // Now add a new one
        mThreadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Background thread
                placesAutoCompleteAdapter.locationItems = placesAutoCompleteAdapter.mPlaceAPI.autocomplete(value);
                // Footer
//                if (placesAutoCompleteAdapter.locationItems.size() > 0)
//                    placesAutoCompleteAdapter.locationItems.add("footer");
                // Post to Main Thread
                mThreadHandler.sendEmptyMessage(1);
            }
        }, 500);

    }

    private void doAfterTextChanged(String textSearchFor) {


    }

    /*
     * Create a get url to fetch results from google place autocomplete api.
     * Append the input received from autocomplete edittext
     * Append your current location
     * Append radius you want to search results within
     * Choose a language you want to fetch data in
     * Append your google API Browser key
  */
    public String getPlaceAutoCompleteUrl(String input) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/place/autocomplete/json");
        urlString.append("?input=");
        try {
            urlString.append(URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        urlString.append("&amp;location=");
        urlString.append(location.getLatitude() + "," + location.getLongitude()); // append lat long of current location to show nearby results.
        urlString.append("&amp;radius=500&amp;language=en");
        urlString.append("&amp;key=" + "YOUR_GOOGLE_PROJECT_BROWSER_KEY_HERE");
        return urlString.toString();
    }

    @Override
    public void onPlaceClick(ArrayList<AutoCompleteAdapter.PlaceAutoComplete> mResultList, int position) {
        if (mResultList != null) {
            try {
//                String placeId = String.valueOf(mResultList.get(position).getPlaceId());
//
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
//                mSearchText.setCursorVisible(false);
//                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(googleApiClient, placeId);
//                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
//                    @Override
//                    public void onResult(@NonNull PlaceBuffer places) {
//                        if (!places.getStatus().isSuccess()) {
//                            // Request did not complete successfully
//                            Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
//                            places.release();
//                            return;
//                        }
//                        Place place = places.get(0);
//                        mLatitude = place.getLatLng().latitude;
//                        mLongitude = place.getLatLng().longitude;
//                        mPrimaryAddress.setText(place.getName());
//                        mSecondaryAddress.setText(place.getAddress());
//                        mAddressLayout.setVisibility(View.VISIBLE);
//                        sCameraMoved = false;
//                        loadMap();
//                        places.release();
//                    }
//                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
        }
    }

    private void fetchLocations(String textFromSearchView, String urlWebServices) {


        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlWebServices,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void performSearch() {

        Toast.makeText(context, "search", Toast.LENGTH_SHORT).show();


    }

    private void initWhereToBSB() {

        //expanding the add work/home bottom sheet dialog
        viewBottomSheetWhereTo = parentView.findViewById(R.id.bottomSheetWhereTo);
        viewBottomSheetWhereTo.setVisibility(View.VISIBLE);
        bsbWhereTo = BottomSheetBehavior.from(viewBottomSheetWhereTo);
        bsbWhereTo.setHideable(true);
        bsbWhereTo.setState(BottomSheetBehavior.STATE_HIDDEN);
        bsbWhereTo.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {

                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        Log.d("BSB", "collapsed");
                    }
                    break;
                    case BottomSheetBehavior.STATE_SETTLING: {

                        Log.d("BSB", "settling");
                    }
                    break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        Log.d("BSB", "expanded");

                    }
                    break;
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        Log.d("BSB", "hidden");
//                        cardViewCurAndDest.setVisibility(View.GONE);
//                        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
//                            viewWhereTo.setVisibility(View.VISIBLE);
                    }
                    case BottomSheetBehavior.STATE_DRAGGING: {

                        Log.d("BSB", "dragging");
                    }
                    break;

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {


            }


        });


        View viewAddHome = viewBottomSheetWhereTo.findViewById(R.id.viewAddHome);
        View viewAddWork = viewBottomSheetWhereTo.findViewById(R.id.viewAddWork);
        View viewSavedPlaces = viewBottomSheetWhereTo.findViewById(R.id.viewSavedPlaces);
        View viewEnterDestLater = viewBottomSheetWhereTo.findViewById(R.id.viewEnterDestLater);
        View viewSetLocOnMap = viewBottomSheetWhereTo.findViewById(R.id.viewSetLocationMap);
        viewAddHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("AddHome", "add home");
                //viewAddHomeBottomSheet.setVisibility(View.VISIBLE);//ok
                viewWhereTo.setVisibility(View.GONE);
                cardViewCurAndDest.setVisibility(View.GONE);//ok
                addHomeAbove.setVisibility(View.VISIBLE);
                bsbWhereTo.setState(BottomSheetBehavior.STATE_HIDDEN);//ok
                bsbAddHome.setState(BottomSheetBehavior.STATE_EXPANDED);
                bsbAddHome.setPeekHeight(200);

            }
        });
        viewAddWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewSetLocOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        viewEnterDestLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        viewSavedPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }

    private void initLayoutWhereTo() {

        //viewWhereTo = LayoutInflater.from(getActivity()).inflate(R.layout.layout_where_to, null);
        //View view = viewWhereTo.findViewById(R.id.llWhereTo);
        viewWhereTo = parentView.findViewById(R.id.llCurAndDes).findViewById(R.id.layoutWhereTo);
        //Log.e("ViewWhereTo", viewWhereTo.getTag().toString());
        viewWhereTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "onClick");
//                showBottomSheetWhereTo();
//                String currentLoc = getCurrentLocation();
//                assert currentLoc != null;
//                List<String> elephantList = Arrays.asList(currentLoc.split(","));
//                if (!elephantList.isEmpty()) {
//                    tvCurrentLoc.setText(elephantList.get(0));
//                }
            }
        });
        //viewWhereTo.setOnClickListener(new ViewWhereTo());
        viewWhereTo.setOnClickListener(this);

    }

    private String getCurrentLocation() {

        String _Location = "";
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    _Location = listAddresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return _Location;

    }

    private void showBottomSheetWhereTo() {

        //showing the above layout / add bottom shadow on below bottom sheet
        cardViewCurAndDest = parentView.findViewById(R.id.llCurAndDest);
        if (cardViewCurAndDest.getVisibility() != View.VISIBLE) {
            cardViewCurAndDest.setVisibility(View.VISIBLE);
        }
        cardViewCurAndDest.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsbWhereTo.setState(BottomSheetBehavior.STATE_HIDDEN);
                viewWhereTo.setVisibility(View.VISIBLE);
                cardViewCurAndDest.setVisibility(View.GONE);
            }
        });
        etDest = cardViewCurAndDest.findViewById(R.id.etDest);
        etDest.addTextChangedListener(textWatcher);
        etDest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    clickedFrom = false;
//                    clickedTo = true;
//                    tvCurrentLoc.setFocusable(false);
//                } else {
//                    tv.setFocusable(false);
//                    clickedTo = false;
//                    clickedFrom = true;
//
//                }
                if (hasFocus) {
                    etDest.callOnClick();
                }
            }
        });
        etDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedFrom = false;
                clickedTo = true;
                etDest.setFocusable(true);
                //etDest.requestFocus();

//                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
//                    bottomSheetBehavior.setPeekHeight(150);
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    bottomSheetBehavior.setPeekHeight(150);
//                }
//                if (bsbWhereTo.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
//                    bsbWhereTo.setPeekHeight(100);
//                    bsbWhereTo.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                } else {
                bsbWhereTo.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    bsbWhereTo.setPeekHeight(100);
//                }

            }
        });


        tvCurrentLoc = cardViewCurAndDest.findViewById(R.id.tvCurrentLocation);
        tvCurrentLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedTo = false;
                clickedFrom = true;

                //                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
//                    bottomSheetBehavior.setPeekHeight(150);
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    bottomSheetBehavior.setPeekHeight(150);
//                }
//                if (bsbWhereTo.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
//                    bsbWhereTo.setPeekHeight(100);
//                    bsbWhereTo.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                } else {
                bsbWhereTo.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    bsbWhereTo.setPeekHeight(100);
//                }
                ///etDest.setFocusable(false);

            }
        });
        tvCurrentLoc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    clickedFrom = true;
//                    clickedTo = false;
                } else {
                    etDest.setFocusable(true);
                    etDest.requestFocus();
                }
            }
        });


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            cardViewCurAndDest.setElevation(20);
//        }
//        cardViewCurAndDest.setElevation(20);

        if (bsbWhereTo.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            bsbWhereTo.setState(BottomSheetBehavior.STATE_EXPANDED);
            //bsbWhereTo.setPeekHeight(250);
        }
//        bsbWhereTo.setState(BottomSheetBehavior.STATE_EXPANDED);
//        bsbWhereTo.setPeekHeight(200);
        //viewBottomSheetWhereTo.startAnimation(animDownTop);

        //adjusting the BSB for addd work/home to be below of WhereTo(Dest and Curr)
        w = getScreenSize().get(0);
        h = getScreenSize().get(1);

        //get the w and h of top view whereTo layout

//        final ViewTreeObserver viewTreeObserver = cardViewCurAndDest.getViewTreeObserver();
//        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//                    cardViewCurAndDest.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                } else {
//                    cardViewCurAndDest.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                }
//                int width = cardViewCurAndDest.getMeasuredWidth();
//                heightOfWhereTo = cardViewCurAndDest.getMeasuredHeight();
//                Log.e("WidthAndHeight", width + "  ->   " + heightOfWhereTo);
//
//                //subtract the h of all screen from h of where to layout
//                int diff = h - heightOfWhereTo;
//                //set the new layout params for bottom sheet add work/home
//                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) viewBottomSheetWhereTo.getLayoutParams();
//                params.setMargins(0, heightOfWhereTo, 0, 0);
//                viewBottomSheetWhereTo.setLayoutParams(params);
//
//            }
//        });


        //set the new layout params for bottom sheet add work/home
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) viewBottomSheetWhereTo.getLayoutParams();
        params.setMargins(0, getViewHeight(cardViewCurAndDest) - 5, 0, 0);
        viewBottomSheetWhereTo.setLayoutParams(params);

        ivClearCrossIcon = cardViewCurAndDest.findViewById(R.id.ivClear);
        ivClearCrossIcon.setVisibility(View.INVISIBLE);  //set the imageview cross icon clear as first
        ivClearCrossIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDest != null) {
                    etDest.setText("");
                    etDest.getText().clear();
                }
            }
        });

    }

    public List<Integer> getScreenSize() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Log.e("widthAndHeight", width + " --   " + height);

        List<Integer> integers = new ArrayList<>();
        integers.add(width);
        integers.add(height);
        return integers;
    }

    private void initUserMap(Bundle savedInstanceState) {

        mMapView = parentView.findViewById(R.id.mapViewUser);
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
//    private void setUpViews() {
//        MenuActivity parentActivity = (MenuActivity) getActivity();
//        resideMenu = parentActivity.getResideMenu();
//
////        parentView.findViewById(R.id.btn_open_menu).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
////            }
////        });
////
////        // add gesture operation's ignored views
////        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);
////        resideMenu.addIgnoredView(ignored_view);
//    }

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

    private void initDialog() {
        viewBottomSheet = parentView.findViewById(R.id.bottom_sheet);
        viewBottomSheet.setVisibility(View.VISIBLE);
        bottomSheetBehavior = BottomSheetBehavior.from(viewBottomSheet);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {

                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        Log.d("BSB", "collapsed");
                    }
                    break;
                    case BottomSheetBehavior.STATE_SETTLING: {

                        Log.d("BSB", "settling");
                    }
                    break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                        Log.d("BSB", "expanded");
                    }
                    break;
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        Log.d("BSB", "hidden");
                        viewWhereTo.setVisibility(View.GONE);
                        viewWhereTo.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_down));


                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING: {

                        Log.d("BSB", "dragging");
                    }
                    break;

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                Log.d("BSB", "sliding " + slideOffset);
            }

        });

        mTitle = viewBottomSheet.findViewById(R.id.title);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(context);
                return inflater.inflate(R.layout.item_title, null);
            }
        });
        in = AnimationUtils.loadAnimation(context, R.anim.slide_in_top);
        out = AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom);

        mAdapter = new CoverFlowAdapter(getActivity());
        mCoverFlow = viewBottomSheet.findViewById(R.id.coverflow);
//        mAdapter.setData(new ArrayList<GameEntity>());
//        mCoverFlow.setAdapter(mAdapter);
        initCoverFlowCars();//for avoiding errors

    }

    private void initCoverFlow() {

        List<String> titlesCars = Arrays.asList(getResources().getStringArray(R.array.titles_cars));
        TypedArray iconsCars = getResources().obtainTypedArray(R.array.arr_cars_icons);

        for (int i = 0; i < titlesCars.size(); i++) {
            listCarsItems.add(new GameEntity(iconsCars.getResourceId(i, -1), titlesCars.get(i)));
        }


        mTitle = parentView.findViewById(R.id.title);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(context);
                TextView textView = (TextView) inflater.inflate(R.layout.item_title, null);
                return textView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(context, R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);

        mAdapter = new CoverFlowAdapter(context);
        mAdapter.setData(listCarsItems);
        mCoverFlow = parentView.findViewById(R.id.coverflow);
        mCoverFlow.setAdapter(mAdapter);

        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,
                        getResources().getString(listCarsItems.get(position).titleResId),
                        Toast.LENGTH_SHORT).show();
            }
        });

        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                //mTitle.setText(getResources().getString(listCarsItems.get(position).titleResId));
                mTitle.setText(listCarsItems.get(position).title);
            }

            @Override
            public void onScrolling() {
                mTitle.setText("");
            }
        });


        mTitle = viewBottomSheet.findViewById(R.id.title);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(context);
                return inflater.inflate(R.layout.item_title, null);
            }
        });
        in = AnimationUtils.loadAnimation(context, R.anim.slide_in_top);
        out = AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom);
        mAdapter = new CoverFlowAdapter(context);
        FeatureCoverFlow mCoverFlow = viewBottomSheet.findViewById(R.id.coverflow);

    }

    private void init() {
        ////////////////
        context = getContext();

        ////////////////////////
        // mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);


        rlRootMainView = parentView.findViewById(R.id.rlRootMainView);

        //////////////////////////////
        llOrderUser = parentView.findViewById(R.id.llOrderUser);
        ivOrderCar = parentView.findViewById(R.id.ivOrderCar);
        ivOrderHome = parentView.findViewById(R.id.ivOrderHome);
        addBgStartImageView(context, ivOrderCar, R.drawable.order_car_final);
        addBgStartImageView(context, ivOrderHome, R.drawable.order_home_final);
        ivOrderHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showCategories();
//                FragmentModalBottomSheet fragmentModalBottomSheet = new FragmentModalBottomSheet();
//                fragmentModalBottomSheet.show(getActivity().getSupportFragmentManager(), "Homes");
//                Bundle args = new Bundle();
//                args.putString(KIND_CAR_OR_HOME, "homes");
//                fragmentModalBottomSheet.setArguments(args);
//                fragmentModalBottomSheet.setCancelable(false);
                ///--------------------------------------------
                initCoverFlowHomes();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetBehavior.setPeekHeight(300);
            }
        });
        ivOrderCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentModalBottomSheet fragmentModalBottomSheet = new FragmentModalBottomSheet();
//                fragmentModalBottomSheet.show(getActivity().getSupportFragmentManager(), "Cars");
//                Bundle args = new Bundle();
//                args.putString(KIND_CAR_OR_HOME, "cars");
//                fragmentModalBottomSheet.setArguments(args);
//                fragmentModalBottomSheet.setCancelable(false);
                //--------------------------------------------
                initCoverFlowCars();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetBehavior.setPeekHeight(300);
                ///////////////////
                viewWhereTo.startAnimation(animScaleUp);
                viewWhereTo.setVisibility(View.VISIBLE);

            }
        });
        //////////////////////////////
        animScaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down);
        animScaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up);
        animDownTop = AnimationUtils.loadAnimation(context, R.anim.slide_out_top);

        /////////////////////
        addBgStartImageView(context, (ImageView) parentView.findViewById(R.id.ivLogo), R.drawable.logo1);
        /////////////////
        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        ////////////////////
        btnOpenMenu = parentView.findViewById(R.id.btnOpenMenu);
        btnOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!resideMenu.isOpened()) {
                    resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                }
            }
        });
        //////////////////
        addBgStartImageView(context, (ImageView) parentView.findViewById(R.id.ivMap), R.drawable.map_with_icons);
        ////////////////////
        //init the get current location cutom button fab
        parentView.findViewById(R.id.btnGetMyLocationNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkLocationPermission();
                createLocationRequest();
                displayLocation();
            }
        });

    }

    private void addBgStartImageView(Context context, ImageView imageView, int res) {
        Glide.with(context)
                .load(res)
                //.centerCrop()
                .into(imageView);


    }

    private void initCoverFlowCars() {

        List<String> titlesCars = Arrays.asList(getResources().getStringArray(R.array.titles_cars));
        @SuppressLint("Recycle") TypedArray iconsCars = getResources().obtainTypedArray(R.array.arr_cars_icons);
        listCarsItems.clear();
        for (int i = 0; i < titlesCars.size(); i++) {
            listCarsItems.add(new GameEntity(iconsCars.getResourceId(i, -1), titlesCars.get(i)));
        }
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);
        mAdapter.setData(listCarsItems);
        mCoverFlow.setAdapter(mAdapter);
        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showTheCarOnMap(position);
            }
        });
        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                //mTitle.setText(getResources().getString(listCarsItems.get(position).titleResId));
                mTitle.setText(listCarsItems.get(position).title);
            }

            @Override
            public void onScrolling() {
                mTitle.setText("");
            }
        });

        TextView tvTypeHomeOrCar = parentView.findViewById(R.id.tvType);
        tvTypeHomeOrCar.setText("فئة السيارات");
        ivCloseDialog = parentView.findViewById(R.id.ivCloseDialog);
        ivCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheetBehavior.setPeekHeight(100);
                //bottomSheetBehavior.setHideable(false);
//                ivCloseDialog.setVisibility(View.GONE);
//                btnHideDialog.setVisibility(View.VISIBLE);

            }
        });
        btnHideDialog = viewBottomSheet.findViewById(R.id.btnHideDialog);
        btnHideDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss();
            }
        });


    }

    private void showTheCarOnMap(int position) {
//        if (!googleApiClient.isConnected() || !googleApiClient.isConnecting())
//            return;
        //otherwise
        if (listCarsItems.isEmpty())
            return;
        String carType = listCarsItems.get(position).title;
        switch (carType) {
            case "عائليه":
                sendRequestCar(FrgDriverVehicles.CAT_FAMILY);
                break;
            case "فاخره":
                sendRequestCar(FrgDriverVehicles.CAT_HIGH);
                break;
            case "إقتصاديه":
                sendRequestCar(FrgDriverVehicles.CAT_CHEAP);
                break;
            case "ذوي الاحتياجات الخاصه":
                sendRequestCar(FrgDriverVehicles.CAT_NEEDS);
                Log.e("carType", carType);
                break;
            case "ونش":
                sendRequestCar(FrgDriverVehicles.CAT_WENSH);
                break;
            case "سيارة نقل":
                sendRequestCar(FrgDriverVehicles.CAT_TRANSPORT);
                break;
            default:
                break;
        }
        getClosestDriver();

    }

    boolean driverFound = false;
    //    int r = 1;//1 km
    double r = 1;
    String driverKey = "";

    private void getClosestDriver() {
        DatabaseReference refDriversGeoFire = FirebaseDatabase.getInstance().getReference("DriversAvailable");
        GeoFire geoFireClosestDriver = new GeoFire(refDriversGeoFire);
        if (location == null) return;
        GeoQuery geoQueryClosestDriver = null;
        geoQueryClosestDriver = geoFireClosestDriver
                .queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), r);
        geoQueryClosestDriver.removeAllListeners();
        geoQueryClosestDriver.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!driverFound && boRequest) {
                    driverFound = true;
                    driverKey = key;
                    DatabaseReference refDrivers = FirebaseDatabase.getInstance().getReference("Workers")
                            .child(DriverSignInOutActivity.TAG_DRIVER).child(driverKey);
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser != null) {
                        HashMap hmUserContactInfo = new HashMap<>();
                        hmUserContactInfo.put("passengerId", firebaseUser.getUid());
                        refDrivers.updateChildren(hmUserContactInfo).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Log.e("connect", "user connect driver success");
                            }
                        });
                        getDriverLocation();
                        //setTimerForThisDriver();///otherwise it will be rejected then

                    }
                }
            }

            @Override
            public void onKeyExited(String key) {
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!driverFound) {
                    r++;//increment the r to be 2 km,  3km, 4km, ....
                    getClosestDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }

    Marker markerFoundDriver = null;
    LatLng latLngDriver = null;
    DatabaseReference referDriversWorking = null;
    ValueEventListener listenerDriverAvailable = null;

    private void getDriverLocation() {
        ///it will create a new one if not exist
        //it will creat object exactly as smae as GeoFire object
        referDriversWorking = FirebaseDatabase.getInstance().getReference("DriversAvailable")
                .child(driverKey).child("l");
        listenerDriverAvailable = referDriversWorking.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && boRequest) {
                    List<Object> objects = (List<Object>) dataSnapshot.getValue();
                    double lat = 0, lon = 0;
                    assert objects != null;
                    if (objects.get(0) != null) {
                        lat = Double.parseDouble(objects.get(0).toString());
                    }
                    if (objects.get(1) != null) {
                        lon = Double.parseDouble(objects.get(1).toString());
                    }
                    latLngDriver = new LatLng(lat, lon);
                    if (markerFoundDriver != null) {
                        markerFoundDriver.remove();
                    } else {
                        markerFoundDriver = googleMap.addMarker(new MarkerOptions().position(latLngDriver)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                                .title("Your Driver"));
                    }

                    checkIfDriverArrives(latLngDriver);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void checkIfDriverArrives(LatLng driverLocation) {
        Location locationDriver_ = new Location("");
        locationDriver_.setLatitude(driverLocation.latitude);
        locationDriver_.setLongitude(driverLocation.longitude);

        Location locationCurrentUser = new Location("");
        locationCurrentUser.setLatitude(location.getLatitude());
        locationCurrentUser.setLongitude(location.getLongitude());

        float dist = locationCurrentUser.distanceTo(locationDriver_);
        if (dist < 100) {
            showAlert("Driver arrived, Hi how are you");
        }

    }

    private void showAlert(String message) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

    }

    private void sendRequestCar(String carType) {
        DatabaseReference refCustomerRequests = FirebaseDatabase.getInstance().getReference("RequestsCars")
                //.child(carType);
                ;
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        GeoFire geoFire = new GeoFire(refCustomerRequests);
        assert firebaseUser != null;
        geoFire.setLocation(firebaseUser.getUid(), new GeoLocation(location.getLatitude(), location.getLongitude()));
        //then add marker here for user
        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //googleMap.addMarker(new MarkerOptions().position(latLng).title("Come To Me").snippet("Want a trip"));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        layoutCancelReq.setVisibility(View.VISIBLE);
    }

    private void initCoverFlowHomes() {
        List<String> titlesHomes = Arrays.asList(getResources().getStringArray(R.array.titles_homes));
        @SuppressLint("Recycle") TypedArray iconsHomes = getResources().obtainTypedArray(R.array.arr_homes_icons);
        listHomesItems.clear();
        for (int i = 0; i < titlesHomes.size(); i++) {
            listHomesItems.add(new GameEntity(iconsHomes.getResourceId(i, -1), titlesHomes.get(i)));
        }
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);
        mAdapter.setData(listHomesItems);
        mCoverFlow.setAdapter(mAdapter);
        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(context,
//                        getResources().getString(listCarsItems.get(position).titleResId),
//                        Toast.LENGTH_SHORT).show();
                Toast.makeText(context, listHomesItems.get(position).title, Toast.LENGTH_SHORT).show();


            }
        });

        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                //mTitle.setText(getResources().getString(listCarsItems.get(position).titleResId));
                mTitle.setText(listHomesItems.get(position).title);
            }

            @Override
            public void onScrolling() {
                mTitle.setText("");
            }
        });


        TextView tvTypeHomeOrCar = viewBottomSheet.findViewById(R.id.tvType);
        tvTypeHomeOrCar.setText("فئة البيوت");
        ImageView ivCloseDialog = viewBottomSheet.findViewById(R.id.ivCloseDialog);
        ivCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss();


            }
        });


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
                .addConnectionCallbacks(FrgUserMap.this)
                .addOnConnectionFailedListener(FrgUserMap.this)
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
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            setCurrentLocMarkerOnMap(location);
            //addLocationGeofire(location);
            loadAllAvailableDrivers();
        }

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


    }

    private void addLocationGeofire(Location location) {
        GeoFire geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference("UserDriverLocations"));
        if (location != null) {
            double lat, lon;
            lat = location.getLatitude();
            lon = location.getLongitude();
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(lat, lon), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {

                    }
                });
            } else {

            }

        }


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
                        .title("My Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.user))
                );
            } else {

            }
        } else {
            markerCurrentLocation = googleMap.addMarker(new MarkerOptions().position(
                    new LatLng(location.getLatitude(), location.getLongitude()))
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.user))
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

    public String getCompleteAddressString(double lat, double lng) {
        String add = "";

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
//            add = add + "\n" + obj.getCountryName();
//            add = add + "\n" + obj.getCountryCode();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getPostalCode();
//            add = add + "\n" + obj.getSubAdminArea();
//            add = add + "\n" + obj.getLocality();
//            add = add + "\n" + obj.getSubThoroughfare();

            ///Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return add;
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
        // deleGeoFireLocation();

        super.onStop();
    }

    private void deleGeoFireLocation() {
        GeoFire geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference("RequestsCars")
                //.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
        );
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) return;
        geoFire.removeLocation(firebaseUser.getUid());

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
        this.location = location;
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

        if (clickedTo) {
            if (etDest != null) {
                String str = getCompleteAddressString(mapCenterLatLng.latitude, mapCenterLatLng.longitude);
                List<String> elephantList = Arrays.asList(str.split(","));
                etDest.setText(elephantList.get(0));
            }
        }
        if (clickedFrom) {
            if (tvCurrentLoc != null) {
                //etDest.setFocusable(true);
                String str = getCompleteAddressString(mapCenterLatLng.latitude, mapCenterLatLng.longitude);
                List<String> elephantList = Arrays.asList(str.split(","));
                tvCurrentLoc.setText(elephantList.get(0));
            }
        }


    }

    private List<String> getCountryCityNames(double MyLat, double MyLong) {

        List<String> names = new ArrayList<>();
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getAddressLine(0);
        String stateName = addresses.get(0).getAddressLine(1);
        String countryName = addresses.get(0).getAddressLine(2);

        names.add(cityName);
        names.add(stateName);
        names.add(countryName);

        return names;
    }

    @Override
    public void onCameraMove() {

        if (clickedFrom && tvCurrentLoc != null) {
            tvCurrentLoc.setText("Loading ...");
        }
        if (clickedTo && etDest != null) {
            etDest.setText("Loading ...");
        }


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
//        if (etDest != null) {
//            String address = getCompleteAddressString2(latitude, longitude);
//            etDest.setText(address);
//            Log.e("ClickedLoc", "Adress = "+address);
//        }


        List<Address> addresses = new ArrayList<>();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        android.location.Address address = addresses.get(0);

        if (address != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i) + "\n");
            }
            Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG).show();
        }

        //remove previously placed Marker
        if (marker != null) {
            marker.remove();
        }

        //place marker where user just clicked
        marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))

        );


    }

    @SuppressLint("LongLogTag")
    private String getCompleteAddressString2(double LATITUDE, double LONGITUDE) {

        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            ",");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address",
                        "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    @Override
    public void onClick(View v) {
        if (/*v.equals(viewWhereTo)*/v.getId() == R.id.layoutWhereTo) {
            showBottomSheetWhereTo();
            String currentLoc = getCurrentLocation();
            List<String> elephantList = Arrays.asList(currentLoc.split(","));
            if (!elephantList.isEmpty()) {
                tvCurrentLoc.setText(elephantList.get(0));
            }
            viewWhereTo.setVisibility(View.GONE);
            //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        }
    }

    public static class PlaceAPI {


        private static final String TAG = PlaceAPI.class.getSimpleName();

        private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
        private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
        private static final String OUT_JSON = "/json";

        private static String API_KEY = "";

        Context context = null;

        public PlaceAPI(Context context) {
            this.context = context;
            API_KEY = context.getResources().getString(R.string.google_maps_key);
        }


        public ArrayList<LocationItem> autocomplete(String input) {
            ArrayList<LocationItem> locationItems = null;

            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();

            try {
                StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                sb.append("?key=" + API_KEY);
                sb.append("&types=(cities)");//types=geocode
                sb.append("&input=" + URLEncoder.encode(input, "utf8"));//The text string on which to search. The Place Autocomplete service will
                // return candidate matches based on this string and order results based on their perceived relevance.

                URL url = new URL(sb.toString());
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
            } catch (MalformedURLException e) {
                Log.e(TAG, "Error processing Places API URL", e);
                return locationItems;
            } catch (IOException e) {
                Log.e(TAG, "Error connecting to Places API", e);
                return locationItems;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            try {
                // Log.d(TAG, jsonResults.toString());

                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                // Extract the Place descriptions from the results
                locationItems = new ArrayList<>(predsJsonArray.length());
                for (int i = 0; i < predsJsonArray.length(); i++) {
                    String desc = predsJsonArray.getJSONObject(i).getString("description");

                    LocationItem locationItem = new LocationItem();
                    locationItem.setChildName("");
                    locationItem.setParentName(desc);

                    locationItems.add(locationItem);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Cannot process JSON results", e);
            }

            return locationItems;
        }
    }

    static class LocationItem {

        String parentName;
        String childName;

        public String getParentName() {
            return parentName;
        }

        public void setParentName(String parentName) {
            this.parentName = parentName;
        }

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }
    }

    class PlacesAutoCompleteAdapter extends ArrayAdapter<LocationItem> implements Filterable {

        ArrayList<LocationItem> locationItems = new ArrayList<LocationItem>();
        Context mContext;
        int mResource;
        PlaceAPI mPlaceAPI = null;

        public PlacesAutoCompleteAdapter(Context context, int resource) {
            super(context, resource);
            mContext = context;
            mResource = resource;
            mPlaceAPI = new PlaceAPI(mContext);
        }

        @Override
        public int getCount() {
            // Last item will be the footer
            return locationItems.size();
        }

        @Override
        public LocationItem getItem(int position) {
            return locationItems.get(position);
        }

        @NonNull
        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null && constraint != "") {
                        locationItems = mPlaceAPI.autocomplete(constraint.toString());

                        filterResults.values = locationItems;
                        filterResults.count = locationItems.size();
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
        }

        @NonNull
        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            View view;

            view = LayoutInflater.from(context).inflate(R.layout.layout_list_item, null);

            TextView tvParentName, tvChildName;
            tvParentName = view.findViewById(R.id.tvParentName);
            tvChildName = view.findViewById(R.id.tvChildName);


            LocationItem locationItem = getItem(position);

            assert locationItem != null;
            tvParentName.setText(locationItem.getParentName());
            tvChildName.setText(locationItem.getChildName());


            return view;

        }

    }

    public class ListViewAdapter extends BaseAdapter {

        Context context = null;
        ArrayList<LocationItem> listLcations;

        public ListViewAdapter(Context context, ArrayList<LocationItem> listLcations) {
            this.context = context;
            this.listLcations = listLcations;
        }

        @Override
        public int getCount() {
            return this.listLcations.size();
        }

        @Override
        public LocationItem getItem(int position) {
            return this.listLcations.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;

            view = LayoutInflater.from(context).inflate(R.layout.layout_list_item, null);

            TextView tvParentName, tvChildName;
            tvParentName = view.findViewById(R.id.tvParentName);
            tvChildName = view.findViewById(R.id.tvChildName);


            LocationItem locationItem = getItem(position);

            tvParentName.setText(locationItem.getParentName());
            tvChildName.setText(locationItem.getChildName());


            return view;

        }
    }
}
