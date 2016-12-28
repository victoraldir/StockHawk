package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.stockhawk.R;

import static com.udacity.stockhawk.ui.StockDetailFragment.FRAGMENT_DETAIL_TAG;
import static com.udacity.stockhawk.ui.StockDetailFragment.SYMBOL_PARAM;

public class StockDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putParcelable(SYMBOL_PARAM,
                    getIntent().getData());

            StockDetailFragment fragment = new StockDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, fragment, FRAGMENT_DETAIL_TAG)
                    .commit();
        }
    }

}
