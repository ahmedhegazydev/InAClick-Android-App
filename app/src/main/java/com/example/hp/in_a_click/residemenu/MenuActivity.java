package com.example.hp.in_a_click.residemenu;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.hp.in_a_click.R;
import com.example.hp.in_a_click.UserMapActvity;
import com.example.hp.in_a_click.WorkerMapActvity;
import com.example.hp.in_a_click.frg_driver.FrgDriverMap;
import com.example.hp.in_a_click.frg_driver.FrgDriverProfile;
import com.example.hp.in_a_click.frg_driver.FrgDriverSettings;
import com.example.hp.in_a_click.frg_driver.FrgDriverTrips;
import com.example.hp.in_a_click.frg_driver.FrgDriverVehicles;
import com.example.hp.in_a_click.frg_user.FrgUserHomes;
import com.example.hp.in_a_click.frg_user.FrgUserMap;
import com.example.hp.in_a_click.frg_user.FrgUserProfile;
import com.example.hp.in_a_click.frg_user.FrgUserSettings;
import com.example.hp.in_a_click.frg_user.FrgUserTrips;
import com.example.hp.in_a_click.signinout.DriverSignInOutActivity;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.util.Arrays;
import java.util.List;

public class MenuActivity extends FragmentActivity implements View.OnClickListener {

    public final static String WHO = "WHO";
    private ResideMenu resideMenu;
    private MenuActivity mContext;
    View.OnClickListener[] listenersUserMenu = new View.OnClickListener[]{
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragment(new FrgUserProfile());
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragment(new FrgUserMap());
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragment(new FrgUserTrips());
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragment(new FrgUserHomes());
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragment(new FrgUserSettings());
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(mContext, DriverSignInOutActivity.class));
                }
            }
    };
    View.OnClickListener[] listenersDriverMenu = new View.OnClickListener[]{
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragment(new FrgDriverProfile());
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragment(new FrgDriverMap());
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragment(new FrgDriverTrips());
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragment(new FrgDriverVehicles());
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragment(new FrgDriverSettings());
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(mContext, DriverSignInOutActivity.class));
                }
            }
    };
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
        init();
        setUpMenu();
        if (savedInstanceState == null) {
            if (getIntent().getStringExtra(DriverSignInOutActivity.WHO) == DriverSignInOutActivity.TAG_HOME_OWNER) {

            }
            if (getIntent().getStringExtra(DriverSignInOutActivity.WHO) == DriverSignInOutActivity.TAG_DRIVER) {
                changeFragment(new FrgDriverMap());
            }
            if (getIntent().getStringExtra(DriverSignInOutActivity.WHO) == DriverSignInOutActivity.TAG_NORMAL_USER) {
                changeFragment(new FrgUserMap());
            }
        }
    }

    private void init() {
        mContext = this;

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
        resideMenu.setShadowVisible(false);


//        addDriverMenus(
//                mContext,
//                Arrays.asList(getResources().getStringArray(R.array.arr_driver_titles)),
//                getResources().obtainTypedArray(R.array.arr_driver_icons),
//                listenersDriverMenu
//        );
//        changeFragment(new FrgDriverMap());
////
//        addUserMenus(
//                mContext,
//                Arrays.asList(getResources().getStringArray(R.array.arr_user_titles)),
//                getResources().obtainTypedArray(R.array.arr_user_icons),
//                listenersUserMenu
//
//        );
//        changeFragment(new FrgUserMap());

        if (getIntent().hasExtra(DriverSignInOutActivity.WHO)) {
            // Toast.makeText(mContext, "exist", Toast.LENGTH_SHORT).show();//done
            // Toast.makeText(mContext, "Menu   " + getIntent().getStringExtra(DriverSignInOutActivity.WHO), Toast.LENGTH_SHORT).show();
            if (getIntent().getStringExtra(DriverSignInOutActivity.WHO).equals(DriverSignInOutActivity.TAG_NORMAL_USER)) {
                Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                addUserMenus(
                        mContext,
                        Arrays.asList(getResources().getStringArray(R.array.arr_user_titles)),
                        getResources().obtainTypedArray(R.array.arr_user_icons),
                        listenersUserMenu

                );
                changeFragment(new FrgUserMap());
            }

            if (getIntent().getStringExtra(DriverSignInOutActivity.WHO).equals(DriverSignInOutActivity.TAG_DRIVER)) {
                addDriverMenus(
                        mContext,
                        Arrays.asList(getResources().getStringArray(R.array.arr_driver_titles)),
                        getResources().obtainTypedArray(R.array.arr_driver_icons),
                        listenersDriverMenu
                );
                changeFragment(new FrgDriverMap());
            }

            if (getIntent().getStringExtra(DriverSignInOutActivity.WHO).equals(DriverSignInOutActivity.TAG_HOME_OWNER)) {
                addHomeOwnerMenus();
            }
        } else {
            //Toast.makeText(mContext, "No extra called "+ DriverSignInOutActivity.WHO, Toast.LENGTH_SHORT).show();//done
        }


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

    private void addDriverMenus(MenuActivity mContext, List<String> titles, TypedArray icons, View.OnClickListener[] listeners) {
        for (int i = 0; i < titles.size(); i++) {
            ResideMenuItem resideMenuItem = new ResideMenuItem(mContext, icons.getResourceId(i, -1), titles.get(i));
            resideMenuItem.setOnClickListener(listeners[i]);
            resideMenu.addMenuItem(resideMenuItem, ResideMenu.DIRECTION_LEFT);

        }
    }


    private void addHomeOwnerMenus() {


    }

    private void addUserMenus(Context context, List<String> titles, TypedArray icons, View.OnClickListener[] onClickListeners) {
        for (int i = 0; i < titles.size(); i++) {
            ResideMenuItem resideMenuItem = new ResideMenuItem(context, icons.getResourceId(i, -1), titles.get(i));
            resideMenuItem.setOnClickListener(onClickListeners[i]);
            resideMenu.addMenuItem(resideMenuItem, ResideMenu.DIRECTION_LEFT);

        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        //resideMenu.closeMenu();
    }

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

        if (resideMenu.isOpened()) {
            resideMenu.closeMenu();
        }

    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }
}
