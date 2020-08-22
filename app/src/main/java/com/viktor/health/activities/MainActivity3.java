package com.viktor.health.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viktor.health.R;
import com.viktor.health.data.DayData;
import com.viktor.health.utils.Utils;

//TODO: add animations, add dayEatingData to views

public class MainActivity3 extends AppCompatActivity {

    public static DayData dayData;

    public static RelativeLayout container;
    public static ImageView mealsViewImage; //TODO: should be clickable and display DayEatingData
    public static AnimationDrawable anim;

    public static TextView caloriesTextView;
    public static TextView fatTextView;
    public static TextView proteinTextView;
    public static TextView carbsTextView;
    public static TextView sleepTextView;
    public static TextView bowelMovementsTextView;
    public static TextView exerciseTextView;
    public static TextView mentalTextView;

    public static TextView bowelMovementsTextViewDescription;
    public static TextView exerciseTextViewDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDayInformation();
        animationOnResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        animationOnPause();
    }

    private void init()
    {
        initViews();
        initAnimation();
    }

    private void initViews()
    {
        //Containers
        container = (RelativeLayout) findViewById(R.id.containerActivity3);
        mealsViewImage = (ImageView) findViewById(R.id.mealsImageView);

        //TextViews
        caloriesTextView = (TextView) findViewById(R.id.caloriesTextView);
        fatTextView = (TextView) findViewById(R.id.fatTextView);
        proteinTextView = (TextView) findViewById(R.id.proteinTextView);
        carbsTextView = (TextView) findViewById(R.id.carbsTextView);
        sleepTextView = (TextView) findViewById(R.id.sleepTextView);
        bowelMovementsTextView = (TextView) findViewById(R.id.bowelMovementsTextView);
        exerciseTextView = (TextView) findViewById(R.id.exerciseTextView);
        mentalTextView = (TextView) findViewById(R.id.mentalTextView);
        //Descriptions
        bowelMovementsTextViewDescription = (TextView) findViewById(R.id.bowelMovementsTextViewDescription);
        exerciseTextViewDescription = (TextView) findViewById(R.id.exerciseTextViewDescription);
    }

    private void initAnimation()
    {
        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(6000);
        anim.setExitFadeDuration(2000);
    }

    private void animationOnResume()
    {
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    private void animationOnPause(){
        if (anim != null && anim.isRunning())
            anim.stop();
    }

    private void loadDayInformation()
    {
        dayData = MainActivity.dayData;
        caloriesTextView.setText("CALORIES: "+dayData.dayEatingData.calories);
        fatTextView.setText("FAT: "+dayData.dayEatingData.fat);
        proteinTextView.setText("PROTEIN: "+dayData.dayEatingData.protein);
        carbsTextView.setText("CARBS: "+dayData.dayEatingData.carbs);
        sleepTextView.setText("SLEEP: "+ "8 hrs"); //TODO: fix later to TimePicker
        bowelMovementsTextView.setText("BOWEL MOVEMENTS: "+dayData.lifeData.bowelMovements);
        bowelMovementsTextViewDescription.setText(dayData.lifeData.bowelMovementDescription);
        exerciseTextView.setText("EXERCISE: "+dayData.lifeData.exercise);
        exerciseTextViewDescription.setText(dayData.lifeData.exerciseDescription);
        mentalTextView.setText("MENTAL: "+dayData.lifeData.mental);
    }

    private void formatText() {
        //tvContent.setText(String.format("%.1f", dayEatingData.painLevel));
        //From green to yellow
        if (dayData.dayEatingData.painLevel < 5) {
            int startColor = ContextCompat.getColor(this, R.color.startColor);
            int endColor = ContextCompat.getColor(this, R.color.centerColor);
            float ratio = Utils.map(dayData.dayEatingData.painLevel, 0, 5, 0, 10)/10;
            //tvContent.getBackground().setColorFilter(ColorUtils.blendARGB(startColor, endColor, ratio), PorterDuff.Mode.MULTIPLY);
        }
        //From yellow to red
        else {
            int startColor = ContextCompat.getColor(this, R.color.centerColor);
            int endColor = ContextCompat.getColor(this, R.color.endColor);
            float ratio = Utils.map(dayData.dayEatingData.painLevel, 5, 10, 0, 10)/10;
            //tvContent.getBackground().setColorFilter(ColorUtils.blendARGB(startColor, endColor, ratio), PorterDuff.Mode.MULTIPLY);
        }
    }
}