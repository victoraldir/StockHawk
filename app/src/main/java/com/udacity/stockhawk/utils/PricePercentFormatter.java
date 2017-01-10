package com.udacity.stockhawk.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by victoraldir on 09/01/2017.
 */

public class PricePercentFormatter {

    private final DecimalFormat dollarFormatWithPlus;
    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;

    public PricePercentFormatter(){
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");

    }

    public String getDollarFormat(float positionPrice){
        return dollarFormat.format(positionPrice);
    }


    public String getDollarFormatWithPlus(float rawAbsoluteChange){

        return dollarFormatWithPlus.format(rawAbsoluteChange);
    }

    public String getPercentageFormat(float percentageChange){
        return percentageFormat.format(percentageChange / 100);
    }
}
