package com.viktor.health;

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

//TODO: add animations

public class MainActivity extends AppCompatActivity {
    //Views
    public static DayEatingData dayEatingData;
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
        initYaxis();
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
                            MainActivity.dayEatingData = marker.dayEatingData;
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
            meals.add(new Meal("3 st Ägg", null, e.getY(), 215, 0, 14, 19));
            e.setData(new DayEatingData(meals.toArray(new Meal[0])));
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

    private void initYaxis() {
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
