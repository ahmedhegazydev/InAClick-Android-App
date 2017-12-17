//package com.example.hp.in_a_click.main;
//
//import android.content.res.AssetManager;
//import android.graphics.Typeface;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.example.hp.in_a_click.R;
////import com.mingle.entity.MenuEntity;
////import com.mingle.sweetpick.BlurEffect;
////import com.mingle.sweetpick.CustomDelegate;
////import com.mingle.sweetpick.DimEffect;
////import com.mingle.sweetpick.RecyclerViewDelegate;
////import com.mingle.sweetpick.SweetSheet;
////import com.mingle.sweetpick.ViewPagerDelegate;
//
//
//import java.util.ArrayList;
//
//public class Main3Activity extends AppCompatActivity {
//
//
//    LinearLayout llBottomContactUs = null;
//    boolean visibile = true;
//
////    private void setupCustomView() {
////
////
////        mSweetSheet3 = new SweetSheet(rl);
////        CustomDelegate customDelegate = new CustomDelegate(choose_this,
////                CustomDelegate.AnimationType.DuangLayoutAnimation);
////        View view = LayoutInflater.from(this).inflate(R.layout_select_role.sweet_layout_custom_view, null, false);
////        customDelegate.setCustomView(view);
////        mSweetSheet3.setDelegate(customDelegate);
////        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                mSweetSheet3.dismiss();
////            }
////        });
////    }
////    private SweetSheet sweetSheetCars, sweetSheetHomes = null;
//    private RelativeLayout rlCars = null, rlHomes, rlMain = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        init();
//        addFontsToTvs();
//        initImagViewsImages();
//
//
//    }
//
//    private void initImagViewsImages() {
//
//
//        Glide.with(this)
//                .load(R.drawable.logo_finish)
//                ///.placeholder(R.drawable.placeholder)
//                //.error(R.drawable.imagenotfound)
//                .into(((ImageView) findViewById(R.id.ivLogo)));
//
//        Glide.with(this)
//                .load(R.drawable.order_home3)
//                //.placeholder(R.drawable.placeholder)
//                //.error(R.drawable.imagenotfound)
//                .into(((ImageView) findViewById(R.id.ivOrderHome)));
//
//        Glide.with(this)
//                .load(R.drawable.order_car3)
//                //.placeholder(R.drawable.placeholder)
//                //.error(R.drawable.imagenotfound)
//                .into(((ImageView) findViewById(R.id.ivOrderCar)));
//
//        Glide.with(this)
//                .load(R.drawable.map)
//                //.placeholder(R.drawable.placeholder)
//                //.error(R.drawable.imagenotfound)
//                .into(((ImageView) findViewById(R.id.ivMap))
//                );
//
//
//    }
//
//    private void addFontsToTvs() {
//
//        AssetManager assetManager = getAssets();
//        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font1.OTF");
//
//        ((TextView) findViewById(R.id.tvAboutUs)).setTypeface(custom_font);
//        ((TextView) findViewById(R.id.tvContactUs)).setTypeface(custom_font);
//        ((TextView) findViewById(R.id.tvOruSer)).setTypeface(custom_font);
//    }
//
////    private ArrayList<MenuEntity> createListCars(int[] icons, String[] titles, int[] titleColors) {
////
////        ArrayList<MenuEntity> list = new ArrayList<>();
////
////
////        for (int i = 0; i < icons.length; i++) {
////            MenuEntity menuEntity = new MenuEntity();
////            menuEntity.iconId = icons[i];
////            menuEntity.titleColor = 0xffb3b3b3;
////            //menuEntity.titleColor = titleColors[i];
////            //menuEntity.title = titles[i];
////            menuEntity.title = "Car " + i;
////
////
////            list.add(menuEntity);
////        }
////
////
////        return list;
////    }
////
////    private ArrayList<MenuEntity> createListHomes(int[] icons, String[] titles, int[] titleColors) {
////
////        ArrayList<MenuEntity> list = new ArrayList<>();
////
////
////        for (int i = 0; i < icons.length; i++) {
////            MenuEntity menuEntity = new MenuEntity();
////            menuEntity.iconId = icons[i];
////            menuEntity.titleColor = 0xffb3b3b3;
////            //menuEntity.titleColor = titleColors[i];
////            //menuEntity.title = titles[i];
////            menuEntity.title = "Home " + i;
////
////
////            list.add(menuEntity);
////        }
////
////
////        return list;
////    }
//
//    private void init() {
//        setContentView(R.layout.sweet_activity_main);
//
//        rlCars = findViewById(R.id.rlCars);
//        rlMain = findViewById(R.id.rlMain);
//        rlHomes = findViewById(R.id.rlHomes);
//        llBottomContactUs = findViewById(R.id.llContactUsBootom);
//
//
//        sweetSheetCars = createSweetSheet(rlMain,
//                createListCars(
//                        new int[]{
//                                R.drawable.car1,
//                                R.drawable.car2,
//                                R.drawable.car3,
//                                R.drawable.car4,
//                                R.drawable.car5,
//                                R.drawable.car6,
//                                R.drawable.car7,
//                                R.drawable.car8,
//                                R.drawable.car9, R.drawable.car7,
//                                R.drawable.car8,
//                                R.drawable.car9
//                        },
//                        null,
//                        null
//                ));
//
//
//        sweetSheetHomes = createSweetSheet(rlMain, createListHomes(
//                new int[]{
//                        R.drawable.home1,
//                        R.drawable.home2,
//                        R.drawable.home3,
//                        R.drawable.home4,
//                        R.drawable.home5,
//                        R.drawable.home6,
//                        R.drawable.home7,
//                        R.drawable.home8,
//                        R.drawable.home9, R.drawable.home7,
//                        R.drawable.home8,
//                        R.drawable.home9
//                },
//                null,
//                null
//        ));
//
//
//    }
//
//    //    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.menu_main2, menu);
////        return choose_this;
////    }
//
//    private SweetSheet createSweetSheet(View v, ArrayList<MenuEntity> list) {
//
//
//        SweetSheet sweetSheet = new SweetSheet((RelativeLayout) v);
//        sweetSheet.setMenuList(list);
//        sweetSheet.setDelegate(new ViewPagerDelegate());
//        sweetSheet.setBackgroundEffect(new DimEffect(0.5f));
//        sweetSheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener() {
//            @Override
//            public boolean onItemClick(int position, MenuEntity menuEntity1) {
//                Toast.makeText(getApplicationContext(), menuEntity1.title + "  " + position, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//
//        return sweetSheet;
//    }
//
//    @Override
//    public void onBackPressed() {
//
//        if (sweetSheetCars.isShow() || sweetSheetHomes.isShow()) {
//            if (sweetSheetCars.isShow()) {
//                sweetSheetCars.dismiss();
//            }
//            if (sweetSheetHomes.isShow()) {
//                sweetSheetHomes.dismiss();
//            }
//        } else {
//            super.onBackPressed();
//        }
//
//
//    }
//
//    //    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////
////        int id = item.getItemId();
////        if (id == R.id.action_recyclerView) {
////            if (mSweetSheet2.isShow()) {
////                mSweetSheet2.dismiss();
////            }
////            if (mSweetSheet3.isShow()) {
////                mSweetSheet3.dismiss();
////            }
////            mSweetSheet.toggle();
////
////            return choose_this;
////        }
////        if (id == R.id.action_viewpager) {
////            if (mSweetSheet.isShow()) {
////                mSweetSheet.dismiss();
////            }
////            if (mSweetSheet3.isShow()) {
////                mSweetSheet3.dismiss();
////            }
////            mSweetSheet2.toggle();
////            return choose_this;
////        }
////        if (id == R.id.action_custom) {
////            if (mSweetSheet.isShow()) {
////                mSweetSheet.dismiss();
////            }
////            if (mSweetSheet2.isShow()) {
////                mSweetSheet2.dismiss();
////            }
////            mSweetSheet3.toggle();
////            return choose_this;
////        }
////        return super.onOptionsItemSelected(item);
////    }
////
//    public void onClickOrderCar(View view) {
//        if (sweetSheetHomes.isShow()) {
//            sweetSheetHomes.dismiss();
//        }
//
//
//        sweetSheetCars.toggle();
//    }
//
//    public void onClickOrderHome(View view) {
//        if (sweetSheetCars.isShow()) {
//            sweetSheetCars.dismiss();
//
//        }
////        if (visibile){
////            llBottomContactUs.setVisibility(View.INVISIBLE);
////            visibile = false;
////        }else{
////            llBottomContactUs.setVisibility(View.VISIBLE);
////            visibile = choose_this;
////        }
//
//
//        sweetSheetHomes.toggle();
//    }
//
//    public void onClickAboutUs(View view) {
//        Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
//    }
//
//    public void onClickContactUs(View view) {
//        Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
//    }
//
//    public void onClickOurServices(View view) {
//        Toast.makeText(this, "Our Services", Toast.LENGTH_SHORT).show();
//    }
//}