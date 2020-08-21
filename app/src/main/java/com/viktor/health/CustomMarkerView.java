package com.viktor.health;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.Image;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;
    private MPPointF drawingPoint;
    public DayEatingData dayEatingData;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // find your layout components
        tvContent = (TextView) findViewById(R.id.markerBackground);
        tvContent.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        drawingPoint = new MPPointF(-1, -1);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        drawingPoint.x = highlight.getDrawX();
        drawingPoint.y = highlight.getDrawY();
        dayEatingData = (DayEatingData) e.getData();
        formatText();
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }

    public final MPPointF getDrawingPoint() {
        return drawingPoint;
    }

    public void setDrawingPoint(float x, float y) {
        drawingPoint.x = x;
        drawingPoint.y = y;
    }

    public void formatText() {
        //tvContent.setText(String.format("%.1f", dayEatingData.painLevel));
        //From green to yellow
        if (dayEatingData.painLevel < 5) {
            int startColor = ContextCompat.getColor(getContext(), R.color.startColor);
            int endColor = ContextCompat.getColor(getContext(), R.color.centerColor);
            float ratio = map(dayEatingData.painLevel, 0, 5, 0, 10)/10;
            //tvContent.getBackground().setColorFilter(ColorUtils.blendARGB(startColor, endColor, ratio), PorterDuff.Mode.MULTIPLY);
        }
        //From yellow to red
        else {
            int startColor = ContextCompat.getColor(getContext(), R.color.centerColor);
            int endColor = ContextCompat.getColor(getContext(), R.color.endColor);
            float ratio = map(dayEatingData.painLevel, 5, 10, 0, 10)/10;
            //tvContent.getBackground().setColorFilter(ColorUtils.blendARGB(startColor, endColor, ratio), PorterDuff.Mode.MULTIPLY);
        }
    }
    static float map(float value,
                     float start1, float stop1,
                     float start2, float stop2) {
        float outgoing =
                start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
        return outgoing;
    }
}