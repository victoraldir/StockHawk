package com.udacity.stockhawk.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by victoraldir on 22/12/2016.
 */

public class MessageHandler {

    private static MessageHandler instance;
    private final Handler mHandler;

    private MessageHandler(){
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static MessageHandler getInstance(){
        if(instance == null)
            instance = new MessageHandler();

        return instance;
    }

    public void showToast(final Context appCtx, final String msg){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(appCtx, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

}
