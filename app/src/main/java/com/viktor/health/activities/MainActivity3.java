package com.viktor.health.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.viktor.health.R;
import com.viktor.health.data.DayData;

//TODO: add animations, add dayEatingData to views

public class MainActivity3 extends AppCompatActivity {

    public static DayData dayData;

    public static ImageView mealsViewImage; //TODO: should be clickable and display DayEatingData

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
        loadDayInformation();
        super.onResume();
    }

    private void init()
    {
        initViews();
    }

    private void initViews()
    {
        //ImageView
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

    public static void loadDayInformation()
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
}