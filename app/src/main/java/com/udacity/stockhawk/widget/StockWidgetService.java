package com.udacity.stockhawk.widget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.utils.PricePercentFormatter;

/**
 * Created by victoraldir on 12/01/2017.
 */

public class StockWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewFactory();
    }

    public class ListRemoteViewFactory implements RemoteViewsFactory {

        private Cursor data = null;
        PricePercentFormatter pricePercentFormatter;

        @Override
        public void onCreate() {
            pricePercentFormatter = new PricePercentFormatter();
        }

        @Override
        public void onDestroy() {
            if (data != null) {
                data.close();
                data = null;
            }

        }

        @Override
        public void onDataSetChanged() {
            if (data != null) data.close();

            final long identityToken = Binder.clearCallingIdentity();
            data = getContentResolver().query(Contract.Quote.URI,
                    Contract.Quote.QUOTE_COLUMNS.toArray(new String[Contract.Quote.QUOTE_COLUMNS.size()]),
                    null,
                    null,
                    Contract.Quote.COLUMN_SYMBOL);
            Binder.restoreCallingIdentity(identityToken);
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.getCount();
        }

        @SuppressLint("PrivateResource")
        @Override
        public RemoteViews getViewAt(int position) {
            if (position == AdapterView.INVALID_POSITION || data == null
                    || !data.moveToPosition(position)) {
                return null;
            }

            RemoteViews remoteViews = new RemoteViews(getPackageName(),
                    R.layout.list_item_quote);

            String stockSymbol = data.getString(Contract.Quote.POSITION_SYMBOL);
            float stockPrice = data.getFloat(Contract.Quote.POSITION_PRICE);
            Float absoluteChange = data.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
            int backgroundDrawable;

            if (absoluteChange >= 0) {
                backgroundDrawable = R.drawable.percent_change_pill_green;
            } else {
                backgroundDrawable = R.drawable.percent_change_pill_red;
            }

            remoteViews.setTextViewText(R.id.symbol, stockSymbol);
            remoteViews.setTextViewText(R.id.main_text_stock_name, data.getString(Contract.Quote.POSITION_NAME));
            remoteViews.setTextViewText(R.id.price, pricePercentFormatter.getDollarFormat(stockPrice));
            remoteViews.setTextViewText(R.id.change, pricePercentFormatter.getPercentageFormat(absoluteChange));
            remoteViews.setInt(R.id.change, "setBackgroundResource", backgroundDrawable);
            remoteViews.setContentDescription(R.string.widget_cd,"");
            //remoteViews.setInt(R.id.list_item_quote, "setBackgroundResource", R.color.white);

            final Intent fillInIntent = new Intent();
            Uri stockUri = Contract.Quote.makeUriForStock(stockSymbol);
            fillInIntent.setData(stockUri);
            remoteViews.setOnClickFillInIntent(R.id.list_item_quote, fillInIntent);
            return remoteViews;

        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return data.moveToPosition(i) ? data.getLong(Contract.Quote.POSITION_ID) : i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
