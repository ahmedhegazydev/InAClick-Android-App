package com.example.hp.in_a_click.frg_worker_proc;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.in_a_click.R;
import com.example.hp.in_a_click.residemenu.MenuActivity;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.special.ResideMenu.ResideMenu;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;

//import com.github.fcannizzaro.materialstepper.AbstractStep;
//import com.github.fcannizzaro.materialstepper.style.BaseStyle;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午1:33
 * Mail: specialcyci@gmail.com
 */
public class Step5AddCat extends Fragment {


    View viewParent;
    NachoTextView nachoTextView = null;
    Spinner spinnerModelNumber = null, spinnerCatName = null;
    ArrayList<String> list = new ArrayList<>();
    Context context = null;
    ArrayList<String> listChips = new ArrayList<>();
    boolean isModelSelected = false;
    Resources resources = null;
    int countSpinnerModelNumber = 0, countSpinnerCatName;

    public Spinner getSpinnerModelNumber() {
        return spinnerModelNumber;
    }

    public Spinner getSpinnerCatName() {
        return spinnerCatName;
    }

    public ArrayList<String> getListChips() {
        listChips.clear();
        //ArrayList<String> chips = new ArrayList<>();
        // Iterate over all of the chips in the NachoTextView
        for (Chip chip : nachoTextView.getAllChips()) {
            // Do something with the text of each chip
            CharSequence text = chip.getText();
            listChips.add((String) text);
            // Do something with the data of each chip (this data will be set if the chip was created by tapping a suggestion)
            // Object data = chip.getData();
        }
        return listChips;
    }

    public void setListChips(ArrayList<String> listChips) {
        this.listChips = listChips;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewParent = inflater.inflate(R.layout.step5_add_cat, container, false);
        initSpinnerModelAndCat();
        initNachoTextView();
        return viewParent;
    }

    private void initSpinnerModelAndCat() {
        context = getContext();
        //---------------------
        spinnerModelNumber = viewParent.findViewById(R.id.spinnerModels);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);

        for (int i = 2008; i <= 2020; i++) {
            list.add(i + "");
        }
        spinnerModelNumber.setAdapter(adapter);
        spinnerModelNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countSpinnerModelNumber++;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //--------------------------
        resources = getResources();
        spinnerCatName = viewParent.findViewById(R.id.spinnerCarCat);
        spinnerCatName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               countSpinnerCatName++;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCatName.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.titles_cars)));


    }

    public int getCountSpinnerCatName() {
        return countSpinnerCatName;
    }

    public int getCountSpinnerModelNumber() {
        return countSpinnerModelNumber;
    }

    private void initNachoTextView() {

        nachoTextView = viewParent.findViewById(R.id.nacho_text_view);


//        if you want just the text from the previous chip (or beginning) up to the cursor to be chipified when the user presses space, call:
        //nachoTextView.addChipTerminator(character, behavior);
        nachoTextView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);


//        if you want all the plain text in the text field to be chipified when the user presses enter, call:
        ///nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);


//        If moveChipToEnd is true, the chip will be moved to the end of the text field when it is tapped.
//        It will then be unchipified and the user will be able to edit it.
//        If moveChipToEnd is false, the chip will be unchipified in place allowing the user to edit it right where it is.
        nachoTextView.enableEditChipOnTouch(true, true);


        nachoTextView.setOnChipClickListener(new NachoTextView.OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip, MotionEvent motionEvent) {
                // Do something
            }
        });


        //If you want to prevent the user from typing certain characters, you can call:
        // These characters, when typed, will be immediately deleted from the text
        //nachoTextView.setIllegalCharacters(illegalCharacter1, illegalCharacter2, ...);


    }


}
