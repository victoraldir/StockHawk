package com.udacity.stockhawk.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by victoraldir on 29/12/2016.
 */

public class MoneyAxisValueFormatter implements IAxisValueFormatter {

    private DecimalFormat mFormat;

    public MoneyAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value);
    }
}
