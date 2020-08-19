package com.viktor.health;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static DayEatingData dayEatingData;
    public static ConstraintLayout layout;
    public static Button button;
    public static LineChart chart;
    public static TextView sticky;
    public static CustomMarkerView marker;

    public static final float THRESHOLD_DISTANCE = 70.f;
    public static final float HEIGHT = 100.f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.chart_layout);
        button = findViewById(R.id.button1);
        chart = findViewById(R.id.test_chart);
        sticky = findViewById(R.id.sticky_label);
        marker = new CustomMarkerView(this, R.layout.custom_marker_view_layout);

        final float threshHoldDistance = 70.f;
        final float height = 100.f;

        //Make a reference to the old listener
        final View.OnTouchListener customListener = chart.getOnTouchListener();

        //Add event listener to main button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });

        //Make the new one
        chart.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                customListener.onTouch(v, event);
                float x = event.getX();
                float y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        if(getDistance(x, y, marker.getDrawingPoint().x, marker.getDrawingPoint().y - HEIGHT) < THRESHOLD_DISTANCE) {
                            MainActivity.dayEatingData = marker.dayEatingData;
                            openActivity3();
                            marker.setDrawingPoint(-1.f, -1.f);
                        }
                        break;
                }
                return true;
            }
        });
        //Init colors from XML-file
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        final int primaryColorDark = typedValue.data;

        typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        final int primaryColor = typedValue.data;

        typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        final int colorAccent = typedValue.data;

        //Add data to list
        List<Entry> data = new ArrayList<>();
        int amount = 100;
        for (int i = 0; i < amount; ++i) {
            Entry e = new Entry(i, (float) Math.random() * 10);

            //Simple testing
            List<Meal> meals = new ArrayList<>();
            meals.add(new Meal("3 st Ägg", null, e.getY(), 215, 0, 14, 19));
            e.setData(new DayEatingData(meals.toArray(new Meal[0])));
            data.add(e);
        }

        //Make dataset
        LineDataSet lds = new LineDataSet(data, "data");

        //Smooth curves
        lds.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        //Colors and drawing
        lds.setDrawCircles(true);
        lds.setDrawCircleHole(false);

        lds.setCircleColor(primaryColor);
        lds.setCircleRadius(3.f);
        lds.setLineWidth(0f);
        LineData ld = new LineData(lds);
        ld.setValueTextSize(15.f);

        lds.setValueTextColor(Color.BLACK);
        ld.setValueTypeface(Typeface.DEFAULT_BOLD);
        ld.setDrawValues(false);

        //Gradient
        lds.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.gradient);
        lds.setFillDrawable(drawable);

        //Set data to chart
        chart.setData(ld);

        //X-axis
        final float textSize = 20f;
        final XAxis xa = chart.getXAxis();
        xa.setGranularity(1f);
        xa.setGranularityEnabled(true);
        xa.setValueFormatter(new StickyDateAxisValueFormatter(chart, sticky));

        xa.setPosition(XAxis.XAxisPosition.BOTTOM);
        xa.setTextSize(textSize);
        xa.setDrawGridLines(true);

        //Y-axis
        final YAxis ya = chart.getAxisLeft();
        ya.setAxisMaxValue(10.f);

        //Init chart
        chart.setTouchEnabled(true);
        chart.getAxisLeft().setTextColor(colorAccent);
        chart.getXAxis().setTextColor(colorAccent);
        chart.setPinchZoom(true);
        chart.setDoubleTapToZoomEnabled(false);
        chart.zoom(amount / 6, 0.5f, 0f, 0f);
        sticky.setTextSize(textSize);
        chart.setMarker(marker);

        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // use removeGlobalOnLayoutListener prior to API 16
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                float xo = xa.getXOffset();
                float yo = xa.getYOffset();
                final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                float rho = displayMetrics.density;

                // WARNING: The 2.2 here was calibrated to the screen I was using. Make sure it works
                // on all resolutions and densities. There may be a pixel/dp inconsistency in here somewhere.
                // The 10f is from the extra bottom offset (set below).
                float ty = chart.getY() + chart.getMeasuredHeight() - rho * yo - 2.2f * textSize - 10f;
                float tx = chart.getX() + rho * xo;

                sticky.setTranslationY(ty);
                sticky.setTranslationX(tx);
            }
        });

        sticky.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

        chart.getAxisLeft().setTextSize(textSize);
        chart.setExtraBottomOffset(10f);

        chart.getAxisRight().setEnabled(false);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.getLegend().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(true);
    }

    public void openActivity2(){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    public void openActivity3(){
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }

    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float)Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }
}





















































/*import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private static ArrayList<Entry> yValues;
    private static LineChart mChart;
    private Button dataButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataButton = (Button) findViewById(R.id.dataButton);
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        mChart  = (LineChart) findViewById(R.id.lineChart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setDoubleTapToZoomEnabled(true);
        mChart.getDescription().setEnabled(false);

        yValues = new ArrayList<>();
        addDataToSet(7.5f);
        initDataSet();
    }

    public void openActivity2(){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void initDataSet(){
        LineDataSet set1 = new LineDataSet(yValues, "Health Data");

        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(15f);
        set1.setCircleRadius(7.f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        data.setValueFormatter(new DateValueFormatter());
        mChart.setData(data);
        mChart.setVisibleXRangeMaximum(10);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addDataToSet(float y){
        int currentDay = getDayOfYear();
        int savedDay = (Integer) PrefUtils.getFromPrefs(this, PrefKeys.USER_DATE_INT, -1);

        //Save data of none has been saved today
        if(currentDay != savedDay)
        {
            int xValue = 0;
            yValues.add(new Entry(currentDay, y));
            PrefUtils.saveToPrefs(this, PrefKeys.USER_DATE_INT, currentDay);
            initDataSet();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static int getDayOfYear(){
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        return dayOfYear;
    }*/
