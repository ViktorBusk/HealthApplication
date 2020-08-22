package com.viktor.health.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import com.viktor.health.R;
import com.viktor.health.data.DayData;
import com.viktor.health.data.DayLifeData;
import com.viktor.health.utils.Utils;
import com.viktor.health.chart.CustomMarkerView;
import com.viktor.health.chart.StickyDateAxisValueFormatter;
import com.viktor.health.data.DayEatingData;
import com.viktor.health.data.Meal;

//TODO: add animations, load data from FireBase

public class MainActivity extends AppCompatActivity {
    //Views
    public static DayData dayData;
    public static ConstraintLayout layout;
    public static Button button;
    public static LineChart chart;
    public static TextView sticky;
    public static CustomMarkerView marker;
    //Colors
    public static int primaryColorDark;
    public static int primaryColor;
    public static int colorAccent;
    //Touch
    private static final float THRESHOLD_DISTANCE = 64.f;
    private static final float HEIGHT = 96.f;
    private static final float CHART_DRAW_HEIGHT = 172.07f;
    //Chart
    private List<Entry> data;
    private final int amount = 100;
    private LineDataSet lds;
    private LineData ld;
    private XAxis xa;
    private YAxis ya;
    private static final float TEXT_SIZE = 20f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO: load all the data from firebase
        initActivity();
    }

    private void initActivity(){
        //Views & Colors
        initViews();
        initColors();
        //Event listeners
        initButtonOnClickListener();
        initChartTouchListener();
        //Data
        addDataToList();
        initLineData();
        setDataToChart();
        //Axes
        initXAxis();
        initYAxis();
        //Chart
        initChart();
    }

    private void initViews(){
        layout = findViewById(R.id.chart_layout);
        button = findViewById(R.id.button1);
        chart = findViewById(R.id.test_chart);
        sticky = findViewById(R.id.sticky_label);
        marker = new CustomMarkerView(this, R.layout.custom_marker_view_layout);
    }

    private void initColors() {
        //Init colors from XML-file
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        primaryColorDark = typedValue.data;

        typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        primaryColor = typedValue.data;

        typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        colorAccent = typedValue.data;
    }

    private void initButtonOnClickListener(){
        //Add event listener to main button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });
    }

    private void initChartTouchListener() {
        //Make a reference to the old listener
        final View.OnTouchListener customListener = chart.getOnTouchListener();
        //Make the new one
        chart.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                customListener.onTouch(v, event);

                float x = event.getX();
                float y = event.getY();
                float paddingTop = 0f;

                if(marker.getDrawingPoint().y <= CHART_DRAW_HEIGHT)
                    paddingTop += (CHART_DRAW_HEIGHT - marker.getDrawingPoint().y);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(Utils.getDistance(x, y, marker.getDrawingPoint().x, marker.getDrawingPoint().y - HEIGHT + paddingTop) < THRESHOLD_DISTANCE) {
                            MainActivity.dayData = marker.dayData;
                            openActivity3();
                            marker.setDrawingPoint(-1.f, -1.f);
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void addDataToList() {
        data = new ArrayList<>();
        for (int i = 0; i < amount; ++i) {
            Entry e = new Entry(i, (float)(Math.random() * 10));
            //Simple testing
            List<Meal> meals = new ArrayList<>();
            meals.add(new Meal("This is the description!", null, e.getY(), (int)(Math.random()*3000), (int)(Math.random()*100), (int)(Math.random()*200), (int)(Math.random()*200)));

            //DayData
            e.setData(new DayData(new DayEatingData(meals.toArray(new Meal[0])), new DayLifeData( (int)(Math.random()*2),
                    "Running 4km", (int)(Math.round(Math.random()*10)),
                    (int)(Math.random()*3), "", null)));
            data.add(e);
        }
    }

    private void initLineData() {
        lds = new LineDataSet(data, "data");

        //Smooth curves
        lds.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        //Colors and drawing
        lds.setDrawCircles(true);
        lds.setDrawCircleHole(false);

        lds.setCircleColor(primaryColor);
        lds.setCircleRadius(3.f);
        lds.setLineWidth(0f);
        ld = new LineData(lds);
        ld.setValueTextSize(15.f);

        lds.setValueTextColor(Color.BLACK);
        ld.setValueTypeface(Typeface.DEFAULT_BOLD);
        ld.setDrawValues(false);

        //Gradient
        lds.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.gradient);
        lds.setFillDrawable(drawable);
    }

    private void setDataToChart(){
        //Set data to chart
        chart.setData(ld);
    }

    private void initXAxis(){
        //X-axis
        xa = chart.getXAxis();
        xa.setGranularity(1f);
        xa.setGranularityEnabled(true);
        xa.setValueFormatter(new StickyDateAxisValueFormatter(chart, sticky));

        xa.setPosition(XAxis.XAxisPosition.BOTTOM);
        xa.setTextSize(TEXT_SIZE);
        xa.setDrawGridLines(true);
    }

    private void initYAxis() {
        //Y-axis
        ya = chart.getAxisLeft();
        ya.setAxisMaxValue(10.f);
    }

    private void initChart() {
        //Init chart
        chart.setTouchEnabled(true);
        chart.getAxisLeft().setTextColor(colorAccent);
        chart.getXAxis().setTextColor(colorAccent);
        chart.setPinchZoom(true);
        chart.setDoubleTapToZoomEnabled(false);
        chart.zoom(amount / 6, 0.5f, 0f, 0f);
        sticky.setTextSize(TEXT_SIZE);
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
                float ty = chart.getY() + chart.getMeasuredHeight() - rho * yo - 2.2f * TEXT_SIZE - 10f;
                float tx = chart.getX() + rho * xo;

                sticky.setTranslationY(ty);
                sticky.setTranslationX(tx);
            }
        });

        sticky.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

        chart.getAxisLeft().setTextSize(TEXT_SIZE);
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
}