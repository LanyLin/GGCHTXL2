package com.example.landy.ggchtxl2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

public class Loading extends Activity {
    private static final int GO_MAIN=1000;
    private static final int CHECK_AGREE=1001;
    private static final int CHECK_AUTH=1002;
    private static final int Go_ERROR=1003;
    User user=null;
    SharedPreferences.Editor editor;
    SharedPreferences count;
    Boolean isFirst;
    EditText username,phonenumber;
    String Firstuser;
    Handler myhandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case GO_MAIN:
                {
                    Intent i = new Intent(Loading.this,Main.class);
                    Bundle bundle = new Bundle();
                    ArrayList<User> AllUser=new ArrayList<>();
                    AllUser.addAll((List<User>)msg.obj);
                    for (User tempuser:AllUser)
                    {
                        if (tempuser.getUsername().equals(Firstuser))
                            user =tempuser;
                    }
                    bundle.putSerializable("AllUser",AllUser);
                    bundle.putSerializable("User",user);
                    Log.e("User",AllUser.size()+"");
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                    break;
                }
                case CHECK_AGREE:
                {
                    user =(User) msg.obj;
                    if (user==null)
                    {
                        Error();
                    }
                    else
                    {
                        editor = count.edit();
                        editor.putBoolean("isfirst", false);
                        editor.putString("username",user.getUsername());
                        editor.commit();
                        BmobHandle.getAllUser(myhandle);
                    }
                    break;
                }
                case CHECK_AUTH:
                {
                    user =(User) msg.obj;
                    if (user==null)
                    {
                        finish();
                    }
                    else
                    {
                        editor = count.edit();
                        editor.putBoolean("isfirst", false);
                        editor.putString("username",user.getUsername());
                        editor.commit();
                        BmobHandle.getAllUser(myhandle);
                    }
                    break;
                }
                case Go_ERROR:
                {
                     ErrorDialog();
                }
            }


        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);
        Bmob.initialize(getApplicationContext(),"b4828d71f046c99dff76061a01045ee0");
        init();
    }
    private void init() {
        count = getPreferences(MODE_PRIVATE);
        isFirst = count.getBoolean("isfirst", true);
        editor = count.edit();
        if (isFirst) {
            final String phone = getPhoneNumber();
            BmobHandle.checkagreement(phone,myhandle);


        }
        else
        {
            Firstuser = count.getString("username",null);
            BmobHandle.getAllUser(myhandle);
        }
    }
    private String getPhoneNumber(){
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager)  getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getLine1Number();
    }
    private void ErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Loading.this);
        builder.setTitle("错误");
        builder.setMessage("获取数据失败，请检查网络连接是否正常");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }
    private void Error()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Loading.this);
        LinearLayout l = (LinearLayout)getLayoutInflater().inflate(R.layout.error_dialog,null);
        builder.setView(l);
        builder.setTitle("验证失败");
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode)
                {
                    case KeyEvent.KEYCODE_BACK:
                        finish();
                        break;
                }
                return true;

            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                check_authority();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
    private void check_authority() {
        // TODO 自动生成的方法存根
        editor = count.edit();
        AlertDialog.Builder builder =new AlertDialog.Builder(Loading.this);
        final LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.check_agreement, null);
        builder.setTitle("检测权限");
        builder.setCancelable(false);
        builder.setView(ll);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO 自动生成的方法存根
                username = (EditText) ll.findViewById(R.id.username);
                phonenumber = (EditText)ll.findViewById(R.id.phone);
                String tempusername = username.getText().toString();
                String tempphonenum = phonenumber.getText().toString();
                BmobHandle.checkagreement(tempusername,tempphonenum,myhandle);

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO 自动生成的方法存根
                finish();
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO 自动生成的方法存根
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        finish();
                        break;

                }
                return true;
            }
        });
        builder.create().show();


    }
}
