package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.chart.DayAxisValueFormatter;
import com.udacity.stockhawk.chart.MoneyAxisValueFormatter;
import com.udacity.stockhawk.chart.XYMarkerView;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.sync.QuoteSyncJob;
import com.udacity.stockhawk.utils.HistoryFormatter;
import com.udacity.stockhawk.utils.PricePercentFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnChartValueSelectedListener {

    public static final String SYMBOL_PARAM = "symbom_param";
    public static final String FRAGMENT_DETAIL_TAG = "fragment_detail";
    private Uri mStockURI;
    private final int STOCK_DETAIL_LOADER = 0;
    private PricePercentFormatter pricePercentFormatter;

    @BindView(R.id.detail_bar_chart) LineChart mChart;
//    @BindView(R.id.timezone) TextClock timeZone;
//    @BindView(R.id.price) TextView stockPrice;
//    @BindView(R.id.change) TextView stockChange;

    private static final String[] DETAIL_COLUMNS = {
            Contract.Quote.COLUMN_HISTORY,
            Contract.Quote.COLUMN_NAME,
            Contract.Quote.COLUMN_SYMBOL,
            Contract.Quote.COLUMN_PRICE,
            Contract.Quote.COLUMN_ABSOLUTE_CHANGE,
            Contract.Quote.COLUMN_PERCENTAGE_CHANGE,
            Contract.Quote.COLUMN_TIME_ZONE,
            Contract.Quote.COLUMN_LAST_CHECK,
    };

    public static final int COL_HISTORY = 0;
    public static final int COL_NAME = 1;
    public static final int COL_SYMBOL = 2;
    public static final int COL_PRICE = 3;
    public static final int COL_ABSOLUTE_CHANGE = 4;
    public static final int COL_PERCENTAGE_CHANGE = 5;
    public static final int COL_TIME_ZONE = 6;
    public static final int COL_LAST_CHECK = 7;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this, rootView);

        Bundle arguments = getArguments();

        mStockURI = arguments.getParcelable(SYMBOL_PARAM);

        pricePercentFormatter = new PricePercentFormatter();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().getSupportLoaderManager().initLoader(STOCK_DETAIL_LOADER, null, this);
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),mStockURI,DETAIL_COLUMNS,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            setUpLineChart(data.getString(COL_HISTORY));
            float absoluteChange = data.getFloat(COL_ABSOLUTE_CHANGE);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.setTitle(data.getString(COL_SYMBOL));
            actionBar.setSubtitle(data.getString(COL_NAME));

            DetailActivity detailActivity = (DetailActivity) getActivity();

            detailActivity.timeZone.setTimeZone(data.getString(COL_TIME_ZONE));
            detailActivity.timeZone.setFormat12Hour("K:mm a, zzzz");
            detailActivity.timeZone.setContentDescription(getString(R.string.cd_timezone,data.getString(COL_TIME_ZONE)));

            detailActivity.stockPrice.setText(pricePercentFormatter.getDollarFormat(data.getFloat(COL_PRICE)));
            detailActivity.stockPrice.setContentDescription(getString(R.string.cd_stock_price,pricePercentFormatter.getDollarFormat(data.getFloat(COL_PRICE))));
            detailActivity.stockChange.setText(pricePercentFormatter.getPercentageFormat(absoluteChange));
            detailActivity.stockChange.setContentDescription(getString(R.string.cd_stock_change,pricePercentFormatter.getDollarFormat(absoluteChange)));

            mChart.setContentDescription(getString(R.string.cd_graph, data.getString(COL_NAME)));

            if (absoluteChange >= 0) {
                detailActivity.stockChange.setBackgroundResource(R.drawable.percent_change_pill_green);
            } else {
                detailActivity.stockChange.setBackgroundResource(R.drawable.percent_change_pill_red);
            }

        }
    }

    private void setUpLineChart(String historyData) {
        Pair<Float, List<Entry>> result = HistoryFormatter.getFormattedStockHistory(historyData);
        List<Entry> dataPairs = result.second;
        Float referenceTime = result.first;
        LineDataSet dataSet = new LineDataSet(dataPairs, "");
        dataSet.setLineWidth(2f);
        dataSet.setDrawHighlightIndicators(false);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);

        Description description = new Description();
        description.setText(getString(R.string.graph_description, QuoteSyncJob.YEARS_OF_HISTORY));
        description.setTextColor(getResources().getColor(R.color.white));

        mChart.setDescription(description);


        DayAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(referenceTime);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineWidth(1.5f);
        xAxis.setTextSize(12f);
        xAxis.setLabelCount(4);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(getResources().getColor(R.color.white));

        YAxis yAxisRight = mChart.getAxisRight();
        yAxisRight.setEnabled(false);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setValueFormatter(new MoneyAxisValueFormatter());
        yAxis.setDrawGridLines(false);
        yAxis.setAxisLineWidth(1.5f);
        yAxis.setTextSize(12f);
        yAxis.setTextColor(getResources().getColor(R.color.white));

        XYMarkerView mv = new XYMarkerView(getActivity(), xAxisFormatter);


        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        mChart.setMarker(mv);
        mChart.invalidate();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
