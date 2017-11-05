package com.example.hp.in_a_click.tests;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hp.in_a_click.R;
import com.firebase.ui.auth.AuthUI;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    private final static int LOGIN_PERMISSION = 1000;
    Button btnLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    private void init() {

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_PERMISSION) {
            startNewActvity(resultCode, data);
        }

    }

    private void startNewActvity(int resultCode, Intent data) {

        if (resultCode == RESULT_OK){
            Intent intent = new Intent(getApplicationContext(), ListLoginActivity.class);
            startActivity(intent);
            finish();

        }else{
            Toast.makeText(this, "Login failed !!!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnLogin)) {
            login();
        }
    }

    private void login() {

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAllowNewEmailAccounts(true).build(), LOGIN_PERMISSION);

    }
}
