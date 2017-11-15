package com.example.hp.in_a_click.residemenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.in_a_click.R;
import com.example.hp.in_a_click.WorkerMapActvity;
import com.igalata.bubblepicker.rendering.BubblePicker;
import com.special.ResideMenu.ResideMenu;

import java.util.ArrayList;

//import android.graphics.Color;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午1:33
 * Mail: specialcyci@gmail.com
 */
public class OrderFragment extends Fragment implements View.OnClickListener {

    int imagesIds[] = {R.drawable.cheapest, R.drawable.fast, R.drawable.biggest, R.drawable.high_quality, R.drawable.closer};
    int colors[] = {
            Color.parseColor("#ffecd2"),
            Color.parseColor("#9fc7ff"),
            Color.parseColor("#ffecd2"),
            Color.parseColor("#ff9fc7"),
            Color.parseColor("#f7ff9f")
    };
    LinearLayout llMainView = null;
    String[] names = null;
    Button btnNext, btnDontAsk;
    ArrayList<String> listSelectedOptions = new ArrayList<String>();
    private BubblePicker picker = null;
    private View parentView;
    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.home, container, false);

        init();
        //setUpViews();
//        setUpViews2();
        //setUpPicker1();
        //setUpPicker2();
        setUpOptions();


        return parentView;
    }

    private void setUpOptions() {
        names = getResources().getStringArray(R.array.countries);


        resideMenu.addIgnoredView(parentView);

        new Item(getActivity(), llMainView);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnNext)) {
            //Toast.makeText(getContext(), "Next", Toast.LENGTH_SHORT).show();

            showMap();

        }
        if (v.equals(btnDontAsk)) {
//            Toast.makeText(getContext(), "Do't Ask", Toast.LENGTH_SHORT).show();
            showMap();

        }
    }

    public void showMap() {

        getActivity().finish();
        startActivity(new Intent(getActivity(), WorkerMapActvity.class));


    }


    private void init() {
        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        llMainView = parentView.findViewById(R.id.llMain);
        ////////////Button next
        btnNext = parentView.findViewById(R.id.btnNext);
        btnDontAsk = parentView.findViewById(R.id.btnDontAsk);
        btnDontAsk.setOnClickListener(this);
        btnNext.setOnClickListener(this);

    }

    private void setUpViews2() {

        resideMenu.addIgnoredView(parentView);


    }

    private void setUpPicker2() {
        final String[] titles = getResources().getStringArray(R.array.countries);
//        final String[] colors = getResources().getStringArray(R.array.colors);
//        final String[] images = getResources().getStringArray(R.array.images);

//        picker = parentView.findViewById(R.id.picker);
//        ArrayList<PickerItem> pickerItems = new ArrayList<PickerItem>();
//        for (int i = 0; i < titles.length; i++) {
//
//            PickerItem pickerItem = new PickerItem(
//                    titles[i],
//                    getActivity().getResources().getDrawable(imagesIds[i]),
//                    false,
//                    colors[i]
//            );
//            pickerItems.add(pickerItem);
//
//        }
//        picker.setItems(pickerItems);
//        picker.setListener(new BubblePickerListener() {
//            @Override
//            public void onBubbleSelected(@NotNull PickerItem item) {
//
//            }
//
//            @Override
//            public void onBubbleDeselected(@NotNull PickerItem item) {
//
//            }
//        });

    }

    private void setUpPicker1() {
//        final String[] titles = getResources().getStringArray(R.array.countries);
//        final TypedArray colors = getResources().obtainTypedArray(R.array.colors);
//        final TypedArray images = getResources().obtainTypedArray(R.array.images);
//
//        picker = parentView.findViewById(R.id.picker);
//
//
//        picker.setAdapter(new BubblePickerAdapter() {
//            @Override
//            public int getTotalCount() {
//                return titles.length;
//            }
//
//            @NotNull
//            @Override
//            public PickerItem getItem(int position) {
//                PickerItem item = new PickerItem();
//                item.setTitle(titles[position]);
//                item.setGradient(new BubbleGradient(colors.getColor((position * 2) % 8, 0),
//                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
//                item.setTypeface(Typeface.DEFAULT_BOLD);
//                item.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
//                item.setBackgroundImage(ContextCompat.getDrawable(getActivity(), images.getResourceId(position, 0)));
//                return item;
//            }
//        });
//
//
//        picker.setListener(new BubblePickerListener() {
//            @Override
//            public void onBubbleSelected(@NotNull PickerItem item) {
//
//            }
//
//            @Override
//            public void onBubbleDeselected(@NotNull PickerItem item) {
//
//            }
//        });

    }

    private void setUpViews() {

//        parentView.findViewById(R.id.btn_open_menu).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
//            }
//        });
//
//        // add gesture operation's ignored views
//        LinearLayout ignored_view = (LinearLayout) parentView.findViewById(R.id.ignored_view);
//        resideMenu.addIgnoredView(ignored_view);
//

    }

    class Item {
        View parentView = null;
        LinearLayout linearLayout = null;
        Context context = null;
        ImageView imageView = null;
        TextView textView = null;

        public Item(Context context, View parentView) {
            this.parentView = parentView;
            this.context = context;
            setUp();
        }

        private void setUp() {


//            LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//            parentView.setLayoutParams(p2);


            for (int i = 0; i < names.length; i++) {
                //LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.option_item, null);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                p.setMargins(10, 10, 10, 10);
                linearLayout.setLayoutParams(p);
                linearLayout.setTag(names[i]);


                imageView = linearLayout.findViewById(R.id.ivTrueIcon);


                textView = linearLayout.findViewById(R.id.tvOption);
                textView.setText(names[i]);

                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout linearLayout = (LinearLayout) v;

                        //add the seelected category and delete it if exist
                        if (!listSelectedOptions.contains(linearLayout.getTag().toString())) {
                            listSelectedOptions.add(linearLayout.getTag().toString());
                        } else {
                            for (int i = 0; i < listSelectedOptions.size(); i++) {
                                if (listSelectedOptions.get(i).equals(linearLayout.getTag().toString())) {
                                    listSelectedOptions.remove(i);
                                    break;
                                }
                            }
                        }
                        ImageView iv = linearLayout.findViewById(R.id.ivTrueIcon);
                        if (iv.getVisibility() == ImageView.GONE) {
                            iv.setVisibility(View.VISIBLE);
                            iv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_up));
                            iv.setBackgroundResource(R.drawable.choose_this);

                        } else {
                            iv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_down));
                            iv.setVisibility(View.GONE);

                        }

                        Log.e("listCat201", listSelectedOptions.toString());

                    }
                });
                ///linearLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_up));
                ((LinearLayout) parentView).addView(linearLayout);


            }
        }


    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        picker.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        picker.onPause();
//    }


}
