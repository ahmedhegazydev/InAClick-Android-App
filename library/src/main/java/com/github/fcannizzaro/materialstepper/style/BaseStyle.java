package com.github.fcannizzaro.materialstepper.style;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.TypedValue;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.R;
import com.github.fcannizzaro.materialstepper.interfaces.Stepable;
import com.github.fcannizzaro.materialstepper.util.StepUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Francesco Cannizzaro on 24/12/2015.
 */
public class BaseStyle extends AppCompatActivity implements Stepable {

    protected StepUtils mSteps = new StepUtils();
    Bundle mExtras = new Bundle();
    HashMap<Integer, Bundle> mStepData = new HashMap<>();

    // attributes
    String mTitle;
    String mErrorString;

    // properties
    int tintColor, primaryColor, primaryColorDark;
    boolean startPreviousButton = false;
    private int mErrorTimeout = 1500;
    private boolean useStateAdapter = false;
    private Toolbar toolbar;

    // getters
    protected int getColor() {
        return primaryColor;
    }

    public Bundle getStepData() {
        return getStepDataFor(mSteps.current());
    }

    public Bundle getStepDataFor(int step) {
        if (mStepData.get(step) == null)
            mStepData.put(step, new Bundle());
        return mStepData.get(step);
    }

    public int steps() {
        return mSteps.total();
    }

    int getErrorTimeout() {
        return mErrorTimeout;
    }

    protected void setErrorTimeout(int mErrorTimeout) {
        this.mErrorTimeout = mErrorTimeout;
    }

    @Deprecated
    protected void useStateAdapter() {
        this.useStateAdapter = true;
    }

    protected void setStateAdapter() {
        this.useStateAdapter = true;
    }

    protected void setStartPreviousButton() {
        this.startPreviousButton = true;
    }

    boolean getStateAdapter() {
        return useStateAdapter;
    }

    public Bundle getExtras() {
        return mExtras;
    }

    // setters

    protected void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    protected void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    protected void setDarkPrimaryColor(int primaryColorDark) {
        this.primaryColorDark = primaryColorDark;
    }

    // steps utils

    protected void addStep(AbstractStep step) {
        mSteps.add(wrap(step));
    }

    protected void addSteps(List<AbstractStep> steps) {
        mSteps.addAll(wrap(steps));
    }

    protected void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(primaryColor);
        toolbar.setTitle(Html.fromHtml(mTitle));
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    private void findColors() {

        if (primaryColor == 0) {
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            primaryColor = typedValue.data;
        }

        if (primaryColor == 0)
            primaryColor = ContextCompat.getColor(this, R.color.material_stepper_global);

        if (primaryColorDark == 0)
            primaryColorDark = ContextCompat.getColor(this, R.color.material_stepper_global_dark);

        if (primaryColor == 0) {
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
            primaryColorDark = typedValue.data;
        }

    }

    void applyTheme() {

        findColors();

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(Html.fromHtml(mTitle));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(primaryColor));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().setStatusBarColor(primaryColorDark);

        }

        tintColor = ContextCompat.getColor(this, R.color.material_stepper_bottom_bar_text);

    }

    @Override
    public void onPrevious() {
        if (mSteps.current() <= 0)
            return;

        mSteps.current(mSteps.current() - 1);
        onUpdate();
    }

    @Override
    public void onNext() {

        AbstractStep step = mSteps.getCurrent();

        if (!step.isOptional() && !step.nextIf()) {
            mErrorString = step.error();
            onError();
            return;
        }

        if (mSteps.current() == mSteps.total() - 1) {
            Intent intent = new Intent();
            intent.putExtras(mExtras);
            setResult(1, intent);
            onComplete();
            onComplete(getExtras());
            return;
        }

        if (mSteps.current() > mSteps.total() - 1)
            return;

        mSteps.current(mSteps.current() + 1);
        onUpdate();
    }

    public void onComplete() {
        // to be redefined
    }

    public void onComplete(Bundle data) {
        // to be redefined
    }

    @Override
    public void onError() {
        // to be redefined
    }

    @Override
    public void onUpdate() {
        // to be redefined
    }

    // wrap stepper in every step

    private AbstractStep wrap(AbstractStep step) {
        return step.stepper(this);
    }

    private List<AbstractStep> wrap(List<AbstractStep> steps) {
        for (AbstractStep step : steps)
            step.stepper(this);

        return steps;
    }


    @Deprecated
    protected void setColorPrimary(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    @Deprecated
    protected void setColorPrimaryDark(int primaryColorDark) {
        this.primaryColorDark = primaryColorDark;
    }

}
