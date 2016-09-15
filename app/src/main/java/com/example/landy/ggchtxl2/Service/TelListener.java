package com.example.landy.ggchtxl2.Service;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.landy.ggchtxl2.Dao.BmobHandle;
import com.example.landy.ggchtxl2.Model.User;

/**
 * Created by Landy on 2016/9/9.
 */
public class TelListener extends PhoneStateListener {
    private static final int CHECK_AGREE=1001;
    private Context context;
    private WindowManager windowManager;
    private TextView tv;
    User item;
    WindowManager.LayoutParams params;
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case CHECK_AGREE:
                {
                    if (msg.obj!=null)
                    {
                        item = (User)msg.obj;
                        tv.setText("姓名："+item.getUsername()+"  "+"学院："+item.getAcademy()+"\n"+"年级："+item.getGrade());
                        tv.setTextSize(16);
                        windowManager.addView(tv,params);
                    }
                }
            }
        }
    };
    public TelListener(Context context) {
        this.context =context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        if (state== TelephonyManager.CALL_STATE_RINGING)
        {
            windowManager = (WindowManager)context.getApplicationContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            params = new WindowManager.LayoutParams();
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = PixelFormat.RGBA_8888;
            tv = new TextView(context);

            BmobHandle.checkagreement(incomingNumber,handler);
        }
        else if (state==TelephonyManager.CALL_STATE_IDLE)
        {
            if (windowManager!=null)
            {
                windowManager.removeView(tv);
            }
        }


    }
}
