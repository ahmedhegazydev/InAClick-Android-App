package com.example.hp.in_a_click.residemenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.hp.in_a_click.R;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class MenuActivity extends FragmentActivity implements View.OnClickListener {

    private ResideMenu resideMenu;
    private MenuActivity mContext;
    private ResideMenuItem itemUser;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemWorker;
    private ResideMenuItem itemSettings, itemSignout, itemAboutUs, itemContactUs;
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mContext = this;

        setUpMenu();
        if (savedInstanceState == null)
            changeFragment(new WorkerFragment());
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setUse3D(true);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemProfile = new ResideMenuItem(this, R.drawable.icon_profile, "Profile");
        itemWorker = new ResideMenuItem(this, R.drawable.icon_home, "Worker");
        itemUser = new ResideMenuItem(this, R.drawable.icon_calendar, "User");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "Settings");
        itemAboutUs = new ResideMenuItem(this, R.drawable.icon_settings, "About");
        itemContactUs = new ResideMenuItem(this, R.drawable.icon_settings, "Contact");
        itemSignout = new ResideMenuItem(this, R.drawable.icon_settings, "SignOut");


        itemProfile.setOnClickListener(this);
        itemWorker.setOnClickListener(this);
        itemUser.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        itemAboutUs.setOnClickListener(this);
        itemContactUs.setOnClickListener(this);
        itemSignout.setOnClickListener(this);


        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemWorker, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemUser, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAboutUs, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemContactUs, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSignout, ResideMenu.DIRECTION_LEFT);


        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemWorker) {
            changeFragment(new WorkerFragment());
        }
        if (view == itemUser) {
            changeFragment(new UserFragment());
        }

        if (view == itemAboutUs) {
            changeFragment(new AboutUsFragment());
        }

        if (view == itemContactUs) {
            changeFragment(new ContactUsFragment());
        }

        if (view == itemSettings) {
            changeFragment(new SettingsFragment());
        }
        if (view == itemSignout) {
            finish();
        }

        if (view == itemProfile) {
            changeFragment(new ProfileFragment());
        }


        resideMenu.closeMenu();
    }

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }
}
