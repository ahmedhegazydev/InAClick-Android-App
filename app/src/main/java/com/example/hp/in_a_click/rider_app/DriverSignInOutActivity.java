package com.example.hp.in_a_click.rider_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.hp.in_a_click.R;
import com.example.hp.in_a_click.model.UserNormal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DriverSignInOutActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_sign_in_out);
        ButterKnife.bind(this);
        init();


    }

//    @OnClick(R.id.btnRegister)
//    public void onClickRegister(View view) {
//        showRegister();
//    }
//
//    @OnClick(R.id.btnLogin)
//    public void onClickLogin(View view) {
//        showLogin();
//    }

    private void init() {

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnSIgnIn = (Button) findViewById(R.id.btnSignIn);


        btnSIgnIn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);


        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/IndieFlower.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        rlMainView = (RelativeLayout) findViewById(R.id.rlMainView);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        refUsers = firebaseDatabase.getReference("Riders");


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
        final View view = layoutInflater.inflate(R.layout.layout_signin, null);


        builder = new AlertDialog.Builder(this)
                .setTitle("Login")
                .setMessage("Login via your email address")
                .setView(view)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        final EditText etEmail = view.findViewById(R.id.etEmail);
                        final EditText etPass = view.findViewById(R.id.etPassword);


                        if (TextUtils.isEmpty(etEmail.getText().toString())) {
                            Snackbar.make(rlMainView, "Enter Email", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(etPass.getText().toString())) {
                            Snackbar.make(rlMainView, "Enter Password", Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        if (etPass.getText().toString().trim().length() < 6) {
                            Snackbar.make(rlMainView, "Password  is too short", Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        waitLogin = new SpotsDialog(DriverSignInOutActivity.this, "Please wait");
                        btnSIgnIn.setEnabled(false);
                        waitLogin.show();


                        firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                if (waitLogin.isShowing()) {
                                    waitLogin.dismiss();
                                }
                                //Snackbar.make(rlMainView, "Logged success", Snackbar.LENGTH_SHORT).show();
                                //btnSIgnIn.setEnabled(true);
                                Intent intent = new Intent(DriverSignInOutActivity.this, DriverMapActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (waitLogin.isShowing()) {
                                    waitLogin.dismiss();
                                }
                                btnSIgnIn.setEnabled(true);
                                Snackbar.make(rlMainView, "Login Failed", Snackbar.LENGTH_SHORT).show();
                            }
                        });


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
        ;
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }


    }

    private void showRegister() {

        LayoutInflater layoutInflater = getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.layout_register, null);


        builder = new AlertDialog.Builder(this)
                .setTitle("Register")
                .setMessage("Please use email to register")
                .setView(view)
                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        final EditText etEmail = view.findViewById(R.id.etEmail);
                        final EditText etPass = view.findViewById(R.id.etPassword);
                        final EditText etName = view.findViewById(R.id.etName);
                        final EditText etPhone = view.findViewById(R.id.etPhone);


                        if (TextUtils.isEmpty(etEmail.getText().toString())) {
                            Snackbar.make(rlMainView, "Enter Email", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(etPass.getText().toString())) {
                            Snackbar.make(rlMainView, "Enter Password", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(etName.getText().toString())) {
                            Snackbar.make(rlMainView, "Enter Name", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(etPhone.getText().toString())) {
                            Snackbar.make(rlMainView, "Enter Phone Number", Snackbar.LENGTH_SHORT).show();
                            return;
                        }


                        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {


                                        UserNormal inAClickUser = new UserNormal(
                                                etEmail.getText().toString(),
                                                etPass.getText().toString(),
                                                etName.getText().toString(),
                                                etPhone.getText().toString()
                                        );

                                        //using email to key, u can't as itis contain @ and .   characters
                                        //use id instead
                                        refUsers.child(firebaseAuth.getCurrentUser().getUid()
                                                //inAClickUser.getUserEmail()
                                        ).setValue(inAClickUser
                                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(rlMainView, "You are registered as a new user successfully :D", Snackbar.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(rlMainView, "Failed in inserting data", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rlMainView, "Failed in authentication", Snackbar.LENGTH_SHORT).show();
                            }
                        });


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
        ;
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }


    }
}
