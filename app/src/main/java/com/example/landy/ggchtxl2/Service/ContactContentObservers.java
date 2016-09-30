package com.example.landy.ggchtxl2.Service;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;


public class ContactContentObservers extends ContentObserver {
    private static String TAG="ContentObserver";
    private  int CONTACT_CHANGE = 3;
    private Context mContext;
    private Handler mHandler;
    public ContactContentObservers(Context context,Handler handler){
        super(handler);
        mContext=context;
        mHandler =handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.e(TAG,"通讯录已经修改");
        mHandler.obtainMessage(CONTACT_CHANGE,"change").sendToTarget();;
    }
}
