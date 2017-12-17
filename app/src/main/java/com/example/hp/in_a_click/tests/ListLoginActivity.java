package com.example.hp.in_a_click.tests;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.hp.in_a_click.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class ListLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {


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
    private boolean mRequestingLocationUpdates;
    private String mLastUpdateTime;

//    public boolean checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//                new AlertDialog.Builder(this)
//                        .setTitle(R.string.title_location_permission)
//                        .setMessage(R.string.text_location_permission)
//                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //Prompt the user once explanation has been shown
//                                //here the onRequestPermissionsResult will be executed
//                                ActivityCompat.requestPermissions(ListLoginActivity.this,
//                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                                        MY_PERMISSIONS_REQUEST_LOCATION);
//                            }
//                        })
//                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//
//                            }
//                        })
//                        .create()
//                        .show();
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//
//            if (checkPlayServices()) {
//                buildGoogleApiClient();
//                createLocationRequest();
//                displayLocation();
//
//
//            }
//
//            return choose_this;
//        }
//    }

    public static boolean isPlayServicesAvailable(Context context) {
        // Google Play Service APKが有効かどうかチェックする
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog((Activity) context, resultCode, 2).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_login);


        init();
        initRefs();
        listAllUsers();
        updateList();

        buildGoogleApiClient();
        //checkLocationPermission();


    }

    private void updateList() {
//        adapter = new FirebaseRecyclerAdapter<User, ListOnlineHolder>(User.class, R.layout.recycler_view_item, ListOnlineHolder.class, counterRef) {
//            @Override
//            protected void populateViewHolder(ListOnlineHolder viewHolder, final User model, int position) {
//
//                viewHolder.tvUserEmail.setText(model.getEmail());
//                viewHolder.setOnItemClickListener(new ListOnlineHolder.ClickListener() {
//                    @Override
//                    public void onItemClick(int position, View v) {
//                        // Toast.makeText(ListLoginActivity.this, "clicked", Toast.LENGTH_SHORT).show();
//                        if (!model.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
//                            if (FirebaseAuth.getInstance().getCurrentUser() != null && location != null) {
//                                Intent intentMap = new Intent(ListLoginActivity.this, MapTrackingActvity.class);
//                                intentMap.putExtra("email", model.getEmail());
//                                intentMap.putExtra("lat", location.getLatitude());
//                                intentMap.putExtra("lon", location.getLongitude());
//                                startActivity(intentMap);
//
//                            }
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onItemLongClick(int position, View v) {
//
//                    }
//                });
//
//
//            }
//        };
//        recyclerViewListOnlineUsers.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

    private void init() {

        //init view
        recyclerViewListOnlineUsers = (RecyclerView) findViewById(R.id.rvListUsers);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewListOnlineUsers.setHasFixedSize(true);
        recyclerViewListOnlineUsers.setLayoutManager(layoutManager);


        //set the supported tooolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("RealTime Database");
        this.setSupportActionBar(toolbar);


    }

    private void initRefs() {


        onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");//create a new one
// counterRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://whatsapp-38bc2.firebaseio.com/").child("lastOnline");
//        counterRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://whatsapp-38bc2.firebaseio.com/");
        counterRef = FirebaseDatabase.getInstance().getReference("lastOnline");
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUserRef = FirebaseDatabase.getInstance().getReference("lastOnline").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        refLocations = FirebaseDatabase.getInstance().getReference("Locations");


    }

    private void listAllUsers() {
        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Boolean.class) && FirebaseAuth.getInstance().getCurrentUser() != null) {//if it is exist
                    currentUserRef.onDisconnect().removeValue();
                    counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(
                            new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(), "Online"));
                    adapter.notifyDataSetChanged();
                    recyclerViewListOnlineUsers.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        counterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    User user = dataSnapshot1.getValue(User.class);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_test, menu);

        //return super.onCreateOptionsMenu(menu);
        return true;


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
                //buildGoogleApiClient();
                //createLocationRequest();
                //displayLocation();


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

    private void displayLocation() {


//        if (/*ContextCompat*/ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Forbidden", Toast.LENGTH_SHORT).show();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            Tracking tracking = new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    Double.toString(location.getLatitude()),
                    Double.toString(location.getLongitude()));

            refLocations.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(tracking);
            Toast.makeText(this, "Location got", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Couldn't get current location", Toast.LENGTH_SHORT).show();
        }

    }

    private void createLocationRequest() {


        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISTANCE);
    }

    protected void stopLocationUpdates() {
        //Log.i(TAG, "stopLocationUpdates");
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

//    private void startLocationUpdates() {
//
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//
//        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
//
//
//    }

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
        //googleApiClient.connect();
        createLocationRequest();


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

    private void startLocationUpdates() {
        // Log.i(TAG, "startLocationUpdates");

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        // 現在位置の取得の前に位置情報の設定が有効になっているか確認する
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // 設定が有効になっているので現在位置を取得する
                        if (ContextCompat.checkSelfPermission(ListLoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, ListLoginActivity.this);
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // 設定が有効になっていないのでダイアログを表示する
                        try {
                            status.startResolutionForResult(ListLoginActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (checkLocationPermission()) {
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//
//                //Request location updates:
//                //locationManager.requestLocationUpdates(provider, 400, 1, this);
//            }
//        }
//
//    }

       @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //setButtonsEnabledState();
                    startLocationUpdates();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        mRequestingLocationUpdates = false;

                    } else {
                        showRationaleDialog();
                    }
                }
                break;
            }
        }
    }

    private void showRationaleDialog() {
        new AlertDialog.Builder(this)
                .setPositiveButton("許可する", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(ListLoginActivity.this,
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
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        isPlayServicesAvailable(this);

        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (googleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.menuJoin:
                //Toast.makeText(this, "Join", Toast.LENGTH_SHORT).show();
                counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        setValue(new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(), "Online"));
                //updateList();
                break;
            case R.id.menuLogOut:
//                currentUserRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(ListLoginActivity.this, "deleted", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(ListLoginActivity.this, "not deleted", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                //FirebaseDatabase.getInstance().getReference("latsOnline").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                FirebaseDatabase.getInstance().getReference("lastOnline").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null);
                //Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;


        }
        return true;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        ///Log.i(TAG, "onConnected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            return;
        }
        if (location == null) {
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            //Toast.makeText(this, location.toString(), Toast.LENGTH_SHORT).show();
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            if (location != null && FirebaseAuth.getInstance().getCurrentUser() != null) {


                Tracking tracking = new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        Double.toString(location.getLatitude()),
                        Double.toString(location.getLongitude()));

                refLocations.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(tracking);
                Toast.makeText(this, "Location got", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Couldn't get current location", Toast.LENGTH_SHORT).show();
        }


        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.i(TAG, "onLocationChanged");
        location = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        //updateUI();
        Toast.makeText(this, getResources().getString(R.string.location_updated_message), Toast.LENGTH_SHORT).show();
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

}
