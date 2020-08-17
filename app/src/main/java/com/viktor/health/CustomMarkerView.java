package com.viktor.health;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class CustomMarkerView extends MarkerView {

    private Button tvContent;
    private Entry entry;
    boolean showing;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // find your layout components
        tvContent = (Button) findViewById(R.id.tvContent);
        showing = true;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        DayEatingData dayEatingData = (DayEatingData)e.getData();

        tvContent.setText("Pain: " +  dayEatingData.painLevel + "\n" +
                        "Calories: " + dayEatingData.calories + "\n" +
                        "Carbs: " + dayEatingData.carbs + "\n" +
                        "Fat: " + dayEatingData.fat + "\n" +
                        "Protein: " + dayEatingData.protein);

        // this will perform necessary layouting
        if(entry == e && showing)
        {
            Toast.makeText(getContext(), "New activity " + showing, Toast.LENGTH_SHORT).show();
        }
        else showing = !showing;
        entry = e;
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}