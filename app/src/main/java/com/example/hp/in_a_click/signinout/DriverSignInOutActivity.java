package com.example.hp.in_a_click.signinout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.in_a_click.R;
import com.example.hp.in_a_click.dialogs.FragmentModalBottomSheet;
import com.example.hp.in_a_click.model.UserDriver;
import com.example.hp.in_a_click.model.UserHomeOwner;
import com.example.hp.in_a_click.model.UserNormal;
import com.example.hp.in_a_click.residemenu.MenuActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.irozon.sneaker.Sneaker;
import com.rey.material.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class DriverSignInOutActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    public static final String WHO = "WHO";
    final static String TAG_HOME_OWNER = "HOME_OWNER";
    final static String TAG_DRIVER = "CAR_DRIVER";
    private static final String TAG = "PhoneAuthActivity";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private static final int RC_SIGN_IN = 7;
    LinearLayout llSelectedRole = null;
    TextView tvRegSelectedRole = null;
    FirebaseDatabase firebaseDatabase = null;
    DatabaseReference refUsers = null;
    FirebaseAuth firebaseAuth = null;
    //    @BindView(R.id.btnSignIn)
//    Button btnSIgnIn;
//    @BindView(R.id.btnRegister)
//    Button btnRegister;
    RelativeLayout rlMainView;
    AlertDialog alertDialog = null;
    AlertDialog.Builder builder = null;
    Button btnRegister, btnSIgnIn;
    SpotsDialog waitLogin = null;
    boolean b = true, b2 = true;
    TextView textView1, textView2;
    //    @OnClick(R.id.btnRegister)
//    public void onClickRegister(View view) {
//        showRegister();
//    }
//
//    @OnClick(R.id.btnLogin)
//    public void onClickLogin(View view) {
//        showLogin();
//    }
    View wall1, wall2;
    boolean anim = true;
    LinearLayout linearLayout = null;
    DatabaseReference refWorkers = null;
    String strDriverOrHome = "";
    TextView connectWithSocial = null;
    Switch aSwitchLoginVia = null;
    Context context = null;
    TextView tvLoginVia = null;
    Animation animScaleUp, animScaleDown;
    TextView tvErrorLogin = null;
    View viewLogin = null, viewRegister;
    TextView tvErrorRegister = null;
    AlertDialog alertDialogRegister, alertDialogLogin = null;
    CheckBox cbUserLogin, cbWorkerLogin, cbUserReg, cbWorkerReg;
    View view;
    LoginButton loginButton = null;
    EditText etEmailReg, etPassReg, etNameReg, etPhoneReg;
    View viewSocial = null;
    boolean mVerificationInProgress = false;//by default
    SpotsDialog waitDialog = null;
    String countryCode = "";
    CountryCodePicker countryCodePicker = null;
    BottomDialog bottomDialogSocialMedia = null;
    String fbEmail = "", fbFirstName, fbLastName, fbGender;
    FirebaseAuth.AuthStateListener authStateListener = null;
    GoogleSignInClient mGoogleSignInClient = null;
    GoogleApiClient googleApiClient = null;
    BottomDialog bottomDialogWorkerOptios = null;
    String googleUserEmail = "", googleUserPhone = "", googleUserName = "";
    View.OnClickListener clickListeneerGoogleSignin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signInWithGooglePlus();
//            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
//            if (acct != null) {
//                String personName = acct.getDisplayName();
//                String personGivenName = acct.getGivenName();
//                String personFamilyName = acct.getFamilyName();
//                String personEmail = acct.getEmail();
//                String personId = acct.getId();
//                Uri personPhoto = acct.getPhotoUrl();
//
//                showRegister();
//                etEmailReg.setText(personEmail);
//                etNameReg.setText(personName);
//
//
//            } else {
//                signInWithGooglePlus();
//            }
        }
    };
    boolean emailExist = false;
    private String strWorkerOrUser = "";
    private CallbackManager callbackManager;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    //    private void setCountryCode() {
//        Spinner spinnerCountries = (Spinner) findViewById(R.id.spinner);
//        // populate country codes
//        final CountryCodesAdapter ccList = new CountryCodesAdapter(this,
//                android.R.layout_select_role.simple_list_item_1,
//                android.R.layout_select_role.simple_spinner_dropdown_item);
//        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
//        Set<String> ccSet = getSupportedRegions(util);
//        for (String cc : ccSet)
//            ccList.add(cc);
//
//        ccList.sort(new Comparator<CountryCodesAdapter.CountryCode>() {
//            public int compare(CountryCodesAdapter.CountryCode lhs, CountryCodesAdapter.CountryCode rhs) {
//                return lhs.regionName.compareTo(rhs.regionName);
//            }
//        });
//        spinnerCountries.setAdapter(ccList);
//        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ccList.setSelected(position);
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        //Your country will get automatically selected in the spinner.
//        // FIXME this doesn't consider creation because of configuration change
//        Phonenumber.PhoneNumber myNum = getMyNumber(this);
//        CountryCodesAdapter.CountryCode cc = new CountryCodesAdapter.CountryCode();
//        if (myNum != null) {
//            cc.regionCode = util.getRegionCodeForNumber(myNum);
//            if (cc.regionCode == null)
//                cc.regionCode = util.getRegionCodeForCountryCode(myNum.getCountryCode());
//            spinnerCountries.setSelection(ccList.getPositionForId(cc));
//            //Toast.makeText(this, String.valueOf(myNum.getNationalNumber()), Toast.LENGTH_SHORT).show();
//        } else {
//            final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//            final String regionCode = tm.getSimCountryIso().toUpperCase(Locale.US);
//            cc.regionCode = regionCode;
//            cc.countryCode = util.getCountryCodeForRegion(regionCode);
//            spinnerCountries.setSelection(ccList.getPositionForId(cc));
//            //Toast.makeText(this, cc.regionCode+"   "+cc.countryCode, Toast.LENGTH_SHORT).show();
//            countryCode = "+" + cc.countryCode;
//        }
//
//
//    }
//     class FragmentModalBottomSheet extends BottomSheetDialogFragment {
//
//        private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                switch (newState) {
//                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        //Log.d("BSB","collapsed") ;
//
//                    }
//                    case BottomSheetBehavior.STATE_SETTLING: {
//                        //Log.d("BSB","settling") ;
//
//                    }
//                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        //Log.d("BSB","expanded") ;
//
//                    }
//                    case BottomSheetBehavior.STATE_HIDDEN: {
//                        //Log.d("BSB" , "hidden") ;
//                        llSelectedRole.setVisibility(View.VISIBLE);
//                        tvRegSelectedRole.setText(strDriverOrHome);
//
//                        dismiss();
//                    }
//                    case BottomSheetBehavior.STATE_DRAGGING: {
//                        //Log.d("BSB","dragging") ;
//
//                    }
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                // Log.d("BSB","sliding " + slideOffset ) ;
//            }
//        };
//
////        public  FragmentModalBottomSheet newInstance(String title) {
////            FragmentModalBottomSheet frag = new FragmentModalBottomSheet();
////            Bundle args = new Bundle();
////            args.putString("title", title);
////            frag.setArguments(args);
////            return frag;
////        }
//
//
//
//        @Override
//        public void setupDialog(Dialog dialog, int style) {
//            super.setupDialog(dialog, style);
//            View contentView = View.inflate(getContext(), R.layout.layout_select_role, null);
//            dialog.setContentView(contentView);
//
//            ///Accessing the select role worker options view
//            //showWorkerOption(contentView);
//
//            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
//            CoordinatorLayout.Behavior behavior = params.getBehavior();
//
//            if (behavior != null && behavior instanceof BottomSheetBehavior) {
//                ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
//            }
//        }
//    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        setContentView(R.layout.activity_driver_sign_in_out);
        ButterKnife.bind(this);
        init();
        initGoogleLoginButton();

        if (checkAccountEmailExistInFirebase("wowrar1234@gmail.com")) {
            Toast.makeText(context, "This email already exists ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "This email  not exist ", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean checkIfEmailExistForSmrUserTest(String insertedEmailWhileReg) {
        Query query = refUsers.orderByChild("userEmail").equalTo(insertedEmailWhileReg);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //here means the value exist
//here means the value not exist
                emailExist = dataSnapshot.exists();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return emailExist;
    }

    private boolean checkAccountEmailExistInFirebase(String email) {

        FirebaseAuth.getInstance().fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                emailExist = !task.getResult().getProviders().isEmpty();
            }
        });
        return emailExist;
    }

    private void initGoogleLoginButton() {


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(DriverSignInOutActivity.this, gso);

//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API)
//                .enableAutoManage(this, this)
//                .build();


        // Set the dimensions of the sign-in button.
        SignInButton signInButton = viewSocial.findViewById(R.id.loginGooglePlus);
        ///signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(clickListeneerGoogleSignin);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        //Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

//                            Log.e("email", user.getEmail());
//                            Log.e("phone", user.getPhoneNumber());
//                            Log.e("Name", user.getDisplayName());
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            //firebaseAuthWithGoogle(task);
        }
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(googleSignInResult);
//
//        }
    }

    private void signInWithGooglePlus() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
            googleUserEmail = account.getEmail();
            googleUserName = account.getDisplayName();
            account.getFamilyName();
            //googleUserName = account.getGivenName();
            showRegister();
            etEmailReg.setText(googleUserEmail);
            etNameReg.setText(googleUserName);
            //etPhoneReg.setText(getPhoneNumber());
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    private void handleSignInResult(GoogleSignInResult googleSignInResult) {

        if (googleSignInResult.isSuccess()) {
            //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
            googleUserEmail = googleSignInResult.getSignInAccount().getEmail();
            googleUserName = googleSignInResult.getSignInAccount().getDisplayName() + " " + googleSignInResult.getSignInAccount().getFamilyName();
            googleSignInResult.getSignInAccount().getGivenName();
            showRegister();
            etEmailReg.setText(googleUserEmail);
            etNameReg.setText(googleUserName);
            //etPhoneReg.setText(getPhoneNumber());

        } else {

        }
    }

    public String getPhoneNumber() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        return mPhoneNumber;
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void firebaseAuthWithGoogle(Task<GoogleSignInAccount> task) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(task.getResult().getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void init() {

        context = this;

        btnRegister = findViewById(R.id.btnRegister);
        btnSIgnIn = findViewById(R.id.btnSignIn);


        btnSIgnIn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);


//        CalligraphyConfig.initDefault(
//                new CalligraphyConfig.Builder()
//                        .setDefaultFontPath("fonts/IndieFlower.ttf")
//                        .setFontAttrId(R.attr.fontPath)
//                        .build()
//        );

        viewSocial = LayoutInflater.from(context).inflate(R.layout.connect_with_social, null);

        rlMainView = findViewById(R.id.rlMainView);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    // NOTE: this Activity should get onpen only when the user is not signed in, otherwise
                    // the user will receive another verification email.
                    sendVerificationEmail();
                } else {
                    // User is signed out

                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
        firebaseDatabase = FirebaseDatabase.getInstance();
        refUsers = firebaseDatabase.getReference("Users");
        refWorkers = firebaseDatabase.getReference("Workers");


        connectWithSocial = findViewById(R.id.connectWithSocial);
        connectWithSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSocialBottomDialog();

            }
        });

        animScaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down);
        animScaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up);


    }

    private void showSocialBottomDialog() {


        // Initialize your instance of callbackManager//
        callbackManager = CallbackManager.Factory.create();
        loginButton = viewSocial.findViewById(R.id.loginFacebook);
        //loginButton.setReadPermissions("email", "public_profile");
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Log.d(TAG, "facebook:onSuccess:" + loginResult);
                Toast.makeText(context, "loginButton onSuccess", Toast.LENGTH_LONG).show();
//                handleFacebookAccessToken(loginResult.getAccessToken());
//                getDataFromFaceBookLogin(loginResult);
            }

            @Override
            public void onCancel() {
                //Log.d(TAG, "facebook:onCancel");
                // ...
                Toast.makeText(context, "loginButton onCancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                //Log.d(TAG, "facebook:onError", error);error
                // ...
                Toast.makeText(context, "loginButton onError", Toast.LENGTH_LONG).show();
            }
        });

        // Register your callback//
        LoginManager.getInstance().registerCallback(callbackManager,

                // If the login attempt is successful, then call onSuccess and pass the LoginResult//
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Print the user’s ID and the Auth Token to Android Studio’s Logcat Monitor//
//                        Log.d(TAG, "User ID: " +
//                                loginResult.getAccessToken().getUserId() + "\n" +
//                                "Auth Token: " + loginResult.getAccessToken().getToken());
                        // Toast.makeText(context, "LoginManager onSuccess", Toast.LENGTH_LONG).show();

                        handleFacebookAccessToken(loginResult.getAccessToken());
                        getDataFromFaceBookLogin(loginResult);

                    }

                    // If the user cancels the login, then call onCancel//
                    @Override
                    public void onCancel() {
                        Toast.makeText(context, "LoginManager onCancel", Toast.LENGTH_LONG).show();
                    }

                    // If an error occurs, then call onError//
                    @Override
                    public void onError(FacebookException exception) {

                    }
                });

//        new BottomDialog.Builder(this)
//                .setCustomView(viewSocial)
//                .setCancelable(false)
//                .setTitle("Connect With Social")
//                .setNegativeText("Exit")
//                .setNegativeTextColorResource(R.color.colorAccent)
//                .setIcon(R.drawable.share)
//                .onNegative(new BottomDialog.ButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull BottomDialog dialog) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();

        bottomDialogSocialMedia = new BottomDialog.Builder(DriverSignInOutActivity.this)
                .setCustomView(viewSocial)
                .setCancelable(true)
                .setTitle("Connect With Social")
                .setNegativeText("Exit")
                .setNegativeTextColorResource(R.color.colorAccent)
                .setIcon(R.drawable.share)
                .onNegative(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build();

        bottomDialogSocialMedia.show();

    }

    private void getDataFromFaceBookLogin(LoginResult loginResult) {

        waitDialog = new SpotsDialog(context, "Please wait...");
        waitDialog.show();
        Log.e("accessToken", loginResult.getAccessToken().toString());

        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.e("ahmed20130074", response.toString());
                Log.e("ahmed20130074", object.toString());
                //Bundle bFacebookData = getFacebookData(object);
                //bottomDialogSocialMedia.dismiss();


                try {
//                    JSONObject jsonObject = new JSONObject(response.toString());
//                    Log.e("ahmed20130074", jsonObject.toString());
//                    JSONObject jsonObject1 = jsonObject.getJSONObject("graphObject");
//                    Log.e("ahmed20130074", jsonObject1.toString());
//                    fbEmail = jsonObject1.getString("email");
//                    fbFirstName = jsonObject1.getString("first_name");
//                    fbLastName = jsonObject1.getString("last_name");
//                    fbGender = jsonObject1.getString("gender");


                    waitDialog.dismiss();
                    //bottomDialogSocialMedia.dismiss();
                    showRegister();
                    if (object.has("email")) {
                        fbEmail = object.getString("email");
                        etEmailReg.setText(fbEmail);
                    }
                    if (object.has("first_name") && object.has("last_name")) {
                        fbFirstName = object.getString("first_name");
                        fbLastName = object.getString("last_name");
                        etNameReg.setText(fbFirstName + " " + fbLastName);
                    } else {
                        if (object.has("first_name")) {
                            fbFirstName = object.getString("first_name");
                            etNameReg.setText(fbFirstName);
                        } else {
                            if (object.has("last_name")) {
                                fbLastName = object.getString("last_name");
                                etNameReg.setText(fbLastName);
                            }
                        }
                    }
                    if (object.has("gender")) {
                        fbGender = object.getString("gender");
                    }


//                    Profile profile = Profile.getCurrentProfile();
//                    String id = profile.getId();
//                    String link = profile.getLinkUri().toString();
                    ///Log.i("Link",link);
//                    if (Profile.getCurrentProfile() != null) {
//                         Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
//                    }

//                    Log.i("Login" + "Email", fbEmail);
//                    Log.i("Login" + "FirstName", fbFirstName);
//                    Log.i("Login" + "LastName", fbLastName);
//                    Log.i("Login" + "Gender", fbGender);
//                    Toast.makeText(context, fbEmail, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(context, fbFirstName, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(context, fbLastName, Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                    showMessage(context, e.getMessage().toString());
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender, location");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (JSONException e) {
            Log.d(TAG, "Error parsing JSON");
        }
        return null;
    }

    @Override
    public void onClick(View v) {

        if (v.equals(btnRegister)) {
            showRegister();
        }
        if (v.equals(btnSIgnIn)) {
            showLogin();
        }


    }

    private void showLogin() {
        LayoutInflater layoutInflater = getLayoutInflater();
        viewLogin = layoutInflater.inflate(R.layout.layout_signin, null);
        tvErrorLogin = viewLogin.findViewById(R.id.tvErrorLogin);

        cbUserLogin = viewLogin.findViewById(R.id.cbUser);
        cbWorkerLogin = viewLogin.findViewById(R.id.cbWorker);
        cbUserLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbWorkerLogin.setChecked(false);
                } else {
                    cbWorkerLogin.setChecked(true);
                }
            }
        });
        cbWorkerLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbUserLogin.setChecked(false);
                } else {
                    cbUserLogin.setChecked(true);
                }
            }
        });


        final View view1 = viewLogin.findViewById(R.id.llLoginWithEmail);
        final View view2 = viewLogin.findViewById(R.id.llLoginWithPhone);


        tvLoginVia = viewLogin.findViewById(R.id.tvLoginVia);
        aSwitchLoginVia = viewLogin.findViewById(R.id.switchLogin);
        aSwitchLoginVia.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if (checked) {
                    tvLoginVia.setText("Login via Phone Number");

                    view2.startAnimation(animScaleDown);
                    view1.setVisibility(View.GONE);

                    view2.startAnimation(animScaleUp);
                    view2.setVisibility(View.VISIBLE);


                } else {
                    tvLoginVia.setText("Login vial Email Address");

                    view2.startAnimation(animScaleDown);
                    view2.setVisibility(View.GONE);

                    view1.startAnimation(animScaleUp);
                    view1.setVisibility(View.VISIBLE);
                }
            }
        });

        builder = new AlertDialog.Builder(this)
                //.setTitle("Login")
                ///.setMessage("Login via your email address")
                .setView(viewLogin)
                .setPositiveButton("Login", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
        ;
        alertDialogLogin = builder.create();
        alertDialogLogin.setCancelable(false);
        alertDialogLogin.setCanceledOnTouchOutside(false);
        alertDialogLogin.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (aSwitchLoginVia.isChecked()) {
                            loginUserWithPhoneNumber();
                        } else {
                            loginUserWithEmail();
                        }

                    }
                });
            }
        });
        if (!alertDialogLogin.isShowing()) {
            alertDialogLogin.show();
        }


    }

    public void loginUserWithEmail() {
        final EditText etEmail = viewLogin.findViewById(R.id.etEmail);
        final EditText etPass = viewLogin.findViewById(R.id.etPassword);


        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            //Snackbar.make(rlMainView, "Enter Email", Snackbar.LENGTH_SHORT).show();
            etEmail.requestFocus();
            tvErrorLogin.setVisibility(View.VISIBLE);
            tvErrorLogin.setText("Enter Email");
            return;
        }
        if (TextUtils.isEmpty(etPass.getText().toString())) {
//            Snackbar.make(rlMainView, "Enter Password", Snackbar.LENGTH_SHORT).show();
            etPass.requestFocus();
            tvErrorLogin.setVisibility(View.VISIBLE);
            tvErrorLogin.setText("Enter Password");
            return;
        }

        if (etPass.getText().toString().trim().length() < 6) {
            //Snackbar.make(rlMainView, "Password  is too short", Snackbar.LENGTH_SHORT).show();
            etPass.requestFocus();
            tvErrorLogin.setVisibility(View.VISIBLE);
            tvErrorLogin.setText("Password  is too short");
            return;
        }

        tvErrorLogin.setVisibility(View.GONE);
        waitLogin = new SpotsDialog(DriverSignInOutActivity.this, "Please wait");
        //btnSIgnIn.setEnabled(false);
        waitLogin.show();


        firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
            }
        })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w("TAG", "signInWithEmail:failed", task.getException());
                            showMessage(context, "Email or Password is invalid\nPlease check them\nThen try again");
                        } else {
                            checkIfEmailVerified();
                        }
                        if (waitLogin.isShowing()) {
                            waitLogin.dismiss();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (waitLogin.isShowing()) {
                            waitLogin.dismiss();
                        }
                        // btnSIgnIn.setEnabled(true);
                        Snackbar.make(rlMainView, "Login Failed", Snackbar.LENGTH_SHORT).show();
                        showMessage(context, "Email or Password is invalid\nPlease check them\nThen try again");
                    }
                });

    }

    public void loginUserWithPhoneNumber() {
        final EditText etPhoneNumber = viewLogin.findViewById(R.id.etPhoneNumber);

        //assign the country code picker to the phone number edittext
        countryCodePicker = viewLogin.findViewById(R.id.countryCodePicker);
        countryCodePicker.registerCarrierNumberEditText(etPhoneNumber);

        if (etPhoneNumber.getText().toString().trim().length() < 6) {
            //Snackbar.make(rlMainView, "Password  is too short", Snackbar.LENGTH_SHORT).show();
            etPhoneNumber.requestFocus();
            tvErrorLogin.setVisibility(View.VISIBLE);
            tvErrorLogin.setText("Enter Phone Number ");
            return;
        }

        tvErrorLogin.setVisibility(View.GONE);
        waitLogin = new SpotsDialog(DriverSignInOutActivity.this, "Please wait");
        //btnSIgnIn.setEnabled(false);
        waitLogin.show();


        //ok the , let's go to signin with phone number
        startPhoneNumberVerification(countryCode + etPhoneNumber.getText().toString());

    }

    public Phonenumber.PhoneNumber getMyNumber(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String regionCode = tm.getSimCountryIso().toUpperCase(Locale.US);
            return PhoneNumberUtil.getInstance().parse(tm.getLine1Number(), regionCode);
        } catch (Exception e) {
            return null;
        }
    }

    private void showRegister() {


        LayoutInflater layoutInflater = getLayoutInflater();
        viewRegister = layoutInflater.inflate(R.layout.layout_register, null);

        etEmailReg = viewRegister.findViewById(R.id.etEmail);
        etPassReg = viewRegister.findViewById(R.id.etPassword);
        etNameReg = viewRegister.findViewById(R.id.etName);
        etPhoneReg = viewRegister.findViewById(R.id.etPhone);


        cbUserReg = viewRegister.findViewById(R.id.cbUser);
        cbWorkerReg = viewRegister.findViewById(R.id.cbWorker);
        cbUserReg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbWorkerReg.setChecked(false);
                    hideRole();
                } else {
                    cbWorkerReg.setChecked(true);
                }
            }
        });
        cbWorkerReg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbUserReg.setChecked(false);
                    showRole(getApplicationContext(), viewRegister);
                    //showWorkerOption(null);


                    //new FragmentModalBottomSheet().show(getSupportFragmentManager(), "BottomSheet Fragment");
//                    FragmentModalBottomSheet fragmentModalBottomSheet = new FragmentModalBottomSheet();
//                    fragmentModalBottomSheet.show(getSupportFragmentManager(), "BottomSheet Fragment");

//                    FragmentManager fm = getSupportFragmentManager();
//                    FragmentModalBottomSheet fragmentModalBottomSheet = FragmentModalBottomSheet.newInstance("Some Title");
//                    fragmentModalBottomSheet.show(fm, "fragment_edit_name");


                } else {
                    cbUserReg.setChecked(true);
                }
            }
        });
        tvErrorRegister = viewRegister.findViewById(R.id.tvErrorRegister);

        llSelectedRole = viewRegister.findViewById(R.id.llSelectedRole);
        tvRegSelectedRole = llSelectedRole.findViewById(R.id.tvRegSelectedRole);


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                //.setTitle("Register")
                //.setMessage("Please use email to register")
                .setView(viewRegister)
                .setPositiveButton("Register", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialogRegister = builder.create();
        alertDialogRegister.setCancelable(false);
        alertDialogRegister.setCanceledOnTouchOutside(false);
        alertDialogRegister.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        registerUser();

                    }
                });
            }
        });
        if (!alertDialogRegister.isShowing()) {
            alertDialogRegister.show();
        }


    }

    public void registerUser() {

        if (TextUtils.isEmpty(etEmailReg.getText().toString())) {
            //Snackbar.make(rlMainView, "Enter Email", Snackbar.LENGTH_SHORT).show();
            //etEmail.setError("Enter Email");
            tvErrorRegister.setVisibility(View.VISIBLE);
            tvErrorRegister.setText("Enter Email");
            etEmailReg.requestFocus();
            return;
        }
        if (!isValidEmail(etEmailReg.getText().toString())) {
//            Snackbar.make(rlMainView, "Enter Valid Email", Snackbar.LENGTH_SHORT).show();
//            etEmail.setError("Enter Valid Email");
            tvErrorRegister.setVisibility(View.VISIBLE);
            tvErrorRegister.setText("Enter Valid Email");
            etEmailReg.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(etPassReg.getText().toString())) {
//            Snackbar.make(rlMainView, "Enter Password", Snackbar.LENGTH_SHORT).show();
//            etPass.setError("Enter Password");
            tvErrorRegister.setVisibility(View.VISIBLE);
            tvErrorRegister.setText("Enter Password");
            etPassReg.requestFocus();
            return;
        }
        if (etPassReg.getText().toString().trim().length() <= 6) {
//            Snackbar.make(rlMainView, "Enter Password", Snackbar.LENGTH_SHORT).show();
//            etPass.setError("Enter Password");
            tvErrorRegister.setVisibility(View.VISIBLE);
            tvErrorRegister.setText("Password is too short");
            etPassReg.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(etNameReg.getText().toString())) {
//            Snackbar.make(rlMainView, "Enter Name", Snackbar.LENGTH_SHORT).show();
//            etName.setError("Enter Name");
            tvErrorRegister.setVisibility(View.VISIBLE);
            tvErrorRegister.setText("Enter Name");
            etNameReg.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(etPhoneReg.getText().toString())) {
//            Snackbar.make(rlMainView, "Enter Phone Number", Snackbar.LENGTH_SHORT).show();
//            etPhone.setError("Enter Phone Number");
            tvErrorRegister.setVisibility(View.VISIBLE);
            tvErrorRegister.setText("Enter Phone Number");
            etPhoneReg.requestFocus();
            return;
        }
        if (!cbUserReg.isChecked() && !cbWorkerReg.isChecked()) {
//            Snackbar.make(rlMainView, "Please Select Who Are You", Snackbar.LENGTH_SHORT).show();
            tvErrorRegister.setVisibility(View.VISIBLE);
            tvErrorRegister.setText("Please Select Who Are You\nRegister As");
            return;
        }

        if (cbUserReg.isChecked()) {
            //do nothing
        } else {
            if (strDriverOrHome == "") {
//            Snackbar.make(rlMainView, "Please Select Your Role", Snackbar.LENGTH_SHORT).show();
                tvErrorRegister.setVisibility(View.VISIBLE);
                tvErrorRegister.setText("Please Select Your Role");
                return;
            }
        }

        //check if email already exists
        if (checkIfEmailExistForSmrUser(etEmailReg.getText().toString())) {
            tvErrorRegister.setVisibility(View.VISIBLE);
            tvErrorRegister.setText("This email already exists \n You can login directly");
            return;
        }


        tvErrorRegister.setVisibility(View.GONE);

        waitLogin = new SpotsDialog(DriverSignInOutActivity.this, "Please wait");
        btnSIgnIn.setEnabled(false);
        waitLogin.show();

        firebaseAuth.createUserWithEmailAndPassword(etEmailReg.getText().toString(), etPassReg.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        waitLogin.dismiss();
                        alertDialogRegister.dismiss();

                        showLogin();
                        showMessage(context, "Please confirm you email");


                        if (cbUserReg.isChecked()) {
                            ///strWorkerOrUser = "Users";


                            UserNormal userNormal = new UserNormal(
                                    etEmailReg.getText().toString(),
                                    etPassReg.getText().toString(),
                                    etNameReg.getText().toString(),
                                    etPhoneReg.getText().toString()

                            );

                            //using email to key, u can't as itis contain @ and .   characters
                            //use id instead
                            refUsers.child(firebaseAuth.getCurrentUser().getUid()
                                    //inAClickUser.getUserEmail()
                            ).setValue(userNormal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (cbUserReg.isChecked())
                                        Snackbar.make(rlMainView, "You are registered as a new USER(Trips)", Snackbar.LENGTH_SHORT).show();
                                    else
                                        Snackbar.make(rlMainView, "You are registered as a new WORKER(Driver)", Snackbar.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(rlMainView, "Network Error, Failed in inserting data", Snackbar.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            if (strDriverOrHome == TAG_DRIVER) {
                                UserDriver userDriver = new UserDriver(
                                        etEmailReg.getText().toString(),
                                        etPassReg.getText().toString(),
                                        etNameReg.getText().toString(),
                                        etPhoneReg.getText().toString()

                                );
                                refWorkers.child(strDriverOrHome).child(firebaseAuth.getCurrentUser().getUid()
                                        //inAClickUser.getUserEmail()
                                ).setValue(userDriver).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(rlMainView, "You are registered as a new " + TAG_DRIVER, Snackbar.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(rlMainView, "Network Error, Failed in  Registration", Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                UserHomeOwner userHomeOwner = new UserHomeOwner(
                                        etEmailReg.getText().toString(),
                                        etPassReg.getText().toString(),
                                        etNameReg.getText().toString(),
                                        etPhoneReg.getText().toString()

                                );
                                refWorkers.child(strDriverOrHome).child(firebaseAuth.getCurrentUser().getUid()
                                        //inAClickUser.getUserEmail()
                                ).setValue(userHomeOwner).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(rlMainView, "You are registered as a new " + TAG_HOME_OWNER, Snackbar.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(rlMainView, "Network Error, Failed in  Registration", Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(rlMainView, "Network Error !!!", Snackbar.LENGTH_SHORT).show();
            }
        });


    }

    public boolean checkIfEmailExistForSmrUser(String insertedEmailWhileReg) {
//        if (firebaseAuth == null)
//            return false;
//
        Query query = null;
        if (cbUserReg.isChecked()) {
            query = refUsers.orderByChild("userEmail").equalTo(insertedEmailWhileReg);
        } else {
            if (strDriverOrHome == TAG_DRIVER) {
                query = refWorkers.child(TAG_DRIVER).orderByChild("userEmail").equalTo(insertedEmailWhileReg);
            } else {
                query = refWorkers.child(TAG_HOME_OWNER).orderByChild("userEmail").equalTo(insertedEmailWhileReg);
            }
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //here means the value exist
//here means the value not exist
                emailExist = dataSnapshot.exists();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return emailExist;
    }

    private void hideRole() {

        if (linearLayout != null) {
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_down);
            linearLayout.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    linearLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }

    private void showRole(final Context context, View view) {

        linearLayout = view.findViewById(R.id.llRole);

        linearLayout.setVisibility(View.VISIBLE);

        final View view1 = linearLayout.findViewById(R.id.llAsDriver);
        final View view2 = linearLayout.findViewById(R.id.llRentAHome);


        final Animation anim1 = AnimationUtils.loadAnimation(context, R.anim.scale_up);
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                view2.setVisibility(View.VISIBLE);
                view2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up));
                anim = false;


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view1.setVisibility(View.VISIBLE);
        view1.startAnimation(anim1);


        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (b) {

                    //view1
                    textView1 = v.findViewById(R.id.tvAsDriver);
                    wall1 = v.findViewById(R.id.wallDriver);

                    wall1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_up));
                    wall1.setVisibility(View.VISIBLE);

                    textView1.setTextColor(Color.GRAY);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                    //view2
                    textView2 = view2.findViewById(R.id.tvHomeAsRent);
                    wall2 = view2.findViewById(R.id.wallHomeRen);

                    wall2.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_down));
                    wall2.setVisibility(View.GONE);

                    textView2.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                    b = false;
                    b2 = true;

                    strDriverOrHome = TAG_DRIVER;


                    Sneaker.with(getParent())
                            .setTitle("Success!!")
                            .setMessage("This is the success message")
                            .sneakSuccess();
                }


            }
        });

        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b2) {
                    //view2
                    textView2 = v.findViewById(R.id.tvHomeAsRent);
                    wall2 = v.findViewById(R.id.wallHomeRen);

                    wall2.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_up));
                    wall2.setVisibility(View.VISIBLE);

                    textView2.setTextColor(Color.GRAY);
                    //textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, textView2.getTextSize());
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


                    //view1
                    textView1 = view1.findViewById(R.id.tvAsDriver);
                    wall1 = view1.findViewById(R.id.wallDriver);

                    wall1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_down));
                    wall1.setVisibility(View.INVISIBLE);

                    textView1.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);


                    b = true;
                    b2 = false;

                    strDriverOrHome = TAG_HOME_OWNER;


                }


            }
        });


    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            //start login view
                            //.....
//                            Sneaker.with(getParent())
//                                    .setTitle("Success!!")
//                                    .setMessage("The email verification sent")
//                                    .sneakSuccess();
                            showMessage(context, "The email verification sent");

                        } else {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
//                            Sneaker.with(getParent())
//                                    .setTitle("Error!!")
//                                    .setMessage("Email Not Sent")
//                                    .sneakError();
                            showMessage(context, "Email Not Sent");

                        }
                    }
                });
    }

    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()) {
            // user is verified, so you can finish this activity or send user to activity which you want.

            //Snackbar.make(rlMainView, "Logged success", Snackbar.LENGTH_SHORT).show();
            //btnSIgnIn.setEnabled(true);
            Intent intent = new Intent(DriverSignInOutActivity.this, MenuActivity.class);
            if (cbUserLogin.isChecked()) {
                intent.putExtra(WHO, "User");
            } else {
                intent.putExtra(WHO, "Worker");
            }
            startActivity(intent);
            finish();
        } else {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();
            //restart this activity
            showSnackbar("Please check email verification\nVerify your email\nThen login");
            tvErrorLogin.setVisibility(View.VISIBLE);
            tvErrorLogin.setText("Please check email verification");

        }
    }

    public void showMessage(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

    }

    public void showSnackbar(String message) {
        Snackbar.make(rlMainView, message, Snackbar.LENGTH_LONG).show();
    }

    private void startPhoneNumberVerification(String phoneNumber) {

//        if (TextUtils.isEmpty(etPhoneNumber.getText().toString())) {
//            //Toast.makeText(this, "Please, Enter the phone number, Then start competition.", Toast.LENGTH_SHORT).show();
//            Snackbar.make(findViewById(android.R.id.content),
//                    "Please, Enter the phone number firstly", Snackbar.LENGTH_SHORT).show();
//            return;
//        }
        waitDialog = new SpotsDialog(this, "Verifying ...");
        waitDialog.show();
        //FirebaseApp.initializeApp(this);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // Toast.makeText(MainActivity.this, "onVerificationCompleted", Toast.LENGTH_SHORT).show();
//               // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                //Log.d(TAG, "onVerificationCompleted:" + credential);

                mVerificationInProgress = false;

                // Update the UI and attempt sign in with the phone credential
//                updateUI(STATE_VERIFY_SUCCESS, credential);
//                signInWithPhoneAuthCredential(credential);


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // Toast.makeText(MainActivity.this, "onVerificationFailed", Toast.LENGTH_SHORT).show();
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                //Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;
//
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                    textInputLayout.setErrorEnabled(true);
//                    textInputLayout.setError("Invalid phone number.");
//                    // [END_EXCLUDE]
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
//                            Snackbar.LENGTH_SHORT).show();
//                }
//
//                // Show a message and update the UI
//                //updateUI(STATE_VERIFY_FAILED);
//                if (progressDialog != null) {
//                    if (progressDialog.isShowing())
//                        progressDialog.dismiss();
//                }
//                if (pdVerification != null) {
//                    if (pdVerification.isShowing())
//                        pdVerification.dismiss();
//                }
//                if (barResendCode != null) {
//                    barResendCode.setVisibility(ProgressBar.GONE);
//                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                // Update UI
                //updateUI(STATE_CODE_SENT);
                //Toast.makeText(HomeActivity.this, "onCodeSent", Toast.LENGTH_SHORT).show();

//                showTheVerificationLayout();
//                if (progressDialog != null) {
//                    if (progressDialog.isShowing())
//                        progressDialog.dismiss();
//                }
//                if (pdVerification != null) {
//                    if (pdVerification.isShowing())
//                        pdVerification.dismiss();
//                }
//                if (barResendCode != null) {
//                    barResendCode.setVisibility(ProgressBar.GONE);
//                }

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);

            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        mVerificationInProgress = true;

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            //Log.d(TAG, "signInWithCredential:success");
//
//                            //adding a unique user id that contains his all own uploaded imaged
//
////                            firebaseDatabase = FirebaseDatabase.getInstance();
////                            databaseReference = firebaseDatabase.getReference().child(etPhoneNumber.getText().toString());
////                            String keyUploadedImages = databaseReference.child("uploadedImages").push().getKey();
////                            String keyFavImages = databaseReference.child("favImages").push().getKey();
//
//                            //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                            sharedPreferences = getSharedPreferences(KEY_PHONE_NUMBER, MODE);
//                            editor = sharedPreferences.edit();
//                            editor.putString(KEY_PHONE_NUMBER, etPhoneNumber.getText().toString());
//                            editor.commit();
////                            editor.putString(KEY_IMAGES_FAV, keyFavImages);
////                            editor.putString(KEY_IMAGES_UPLOADED, keyUploadedImages);
//
//
//                            FirebaseUser user = task.getResult().getUser();
//                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
//                            finish();
//                        } else {
//                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                mVerificationField.setError("Invalid code.");
//                            }
//                        }
//                        textInputLayout.setErrorEnabled(false);
//                        if (progressDialog != null) {
//                            if (progressDialog.isShowing())
//                                progressDialog.dismiss();
//                        }
//                        if (pdVerification != null) {
//                            if (pdVerification.isShowing())
//                                pdVerification.dismiss();
//                        }
//                        if (barResendCode != null) {
//                            barResendCode.setVisibility(ProgressBar.GONE);
//                        }

                    }
                });


    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void updateUI(int uiState) {
        updateUI(uiState, firebaseAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                break;
            case STATE_VERIFY_FAILED:

                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                // Set the verification text based on the credential
//                showTheVerificationLayout();
//                if (cred != null && mVerificationField != null) {
//                    if (cred.getSmsCode() != null) {
//                        mVerificationField.setText(cred.getSmsCode());
//                        //signInWithPhoneAuthCredential(cred);
//                    } else {
//                        mVerificationField.setText(R.string.instant_validation);
//                        mVerificationField.setTextColor(Color.parseColor("#4bacb8"));
//                    }
//                }

                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check
                break;
            case STATE_SIGNIN_SUCCESS:
                // Np-op, handled by sign-in check
                //startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
                break;
        }

        if (user == null) {
            // Signed out
//            mPhoneNumberViews.setVisibility(View.VISIBLE);
//            mSignedInViews.setVisibility(View.GONE);
//
//            mStatusText.setText(R.string.signed_out);;
        } else {
            // Signed in
            //mPhoneNumberViews.setVisibility(View.GONE);
            /*
            mSignedInViews.setVisibility(View.VISIBLE);
            enableViews(mPhoneNumberField, mVerificationField);
            mPhoneNumberField.setText(null);
            mVerificationField.setText(null);
            mStatusText.setText(R.string.signed_in);
            mDetailText.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            */
//            Intent intent = new Intent(this, HomeActivity.class);
//            startActivity(intent);
//            finish();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);

        firebaseAuth.addAuthStateListener(authStateListener);

//        if (mVerificationInProgress && validatePhoneNumber()) {
//            startPhoneNumberVerification(etPhoneNumber.getText().toString());
//        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(rlMainView, "Network Error !!, Google Play Services", Snackbar.LENGTH_SHORT).show();
    }

    public void showWorkerOption(View view) {

//        if (view == null)
//            return;
//        else


        //linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_select_role, null);
        //linearLayout.setVisibility(View.VISIBLE);
        linearLayout = (LinearLayout) view;

        final View view1 = linearLayout.findViewById(R.id.llAsDriver);
        final View view2 = linearLayout.findViewById(R.id.llRentAHome);


        final Animation anim1 = AnimationUtils.loadAnimation(context, R.anim.scale_up);
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                view2.setVisibility(View.VISIBLE);
                view2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up));
                anim = false;


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view1.setVisibility(View.VISIBLE);
        view1.startAnimation(anim1);


        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (b) {

                    //view1
                    textView1 = v.findViewById(R.id.tvAsDriver);
                    wall1 = v.findViewById(R.id.wallDriver);

                    wall1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_up));
                    wall1.setVisibility(View.VISIBLE);

                    textView1.setTextColor(Color.GRAY);
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                    //view2
                    textView2 = view2.findViewById(R.id.tvHomeAsRent);
                    wall2 = view2.findViewById(R.id.wallHomeRen);

                    wall2.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_down));
                    wall2.setVisibility(View.GONE);

                    textView2.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                    b = false;
                    b2 = true;

                    strDriverOrHome = TAG_DRIVER;


                }


            }
        });

        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b2) {
                    //view2
                    textView2 = v.findViewById(R.id.tvHomeAsRent);
                    wall2 = v.findViewById(R.id.wallHomeRen);

                    wall2.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_up));
                    wall2.setVisibility(View.VISIBLE);

                    textView2.setTextColor(Color.GRAY);
                    //textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, textView2.getTextSize());
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


                    //view1
                    textView1 = view1.findViewById(R.id.tvAsDriver);
                    wall1 = view1.findViewById(R.id.wallDriver);

                    wall1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_down));
                    wall1.setVisibility(View.INVISIBLE);

                    textView1.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);


                    b = true;
                    b2 = false;

                    strDriverOrHome = TAG_HOME_OWNER;


                }


            }
        });

        bottomDialogWorkerOptios = new BottomDialog.Builder(DriverSignInOutActivity.this)
                .setCustomView(linearLayout)
                .setCancelable(true)
                .autoDismiss(true)
                .setTitle("Select Your Role")
                //.setNegativeText("Exit")
                //.setNegativeTextColorResource(R.color.colorAccent)
//                .setIcon(R.drawable.share)
//                .onNegative(new BottomDialog.ButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull BottomDialog dialog) {
//                        dialog.dismiss();
//                    }
//                })
                .build();

        bottomDialogWorkerOptios.show();


    }


}
