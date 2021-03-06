package com.example.landy.ggchtxl2.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.example.landy.ggchtxl2.Dao.BmobHandle;
import com.example.landy.ggchtxl2.Dao.DatabaseHelper;
import com.example.landy.ggchtxl2.Model.Data_Verson;
import com.example.landy.ggchtxl2.Model.UserExist;
import com.example.landy.ggchtxl2.R;
import com.example.landy.ggchtxl2.Model.User;
import com.tencent.bugly.crashreport.CrashReport;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.Bmob;

@SuppressWarnings("ALL")
public class Loading extends Activity {
    private static final int GO_MAIN=1000;
    private static final int CHECK_AGREE=1001;
    private static final int CHECK_AUTH=1002;
    private static final int Go_ERROR=1003;
    private static final int Get_Version=1004;
    private static final int CHECK_ERROR=1005;
    private static final int INIT_DATA = 1007;
    User user=null;
    SharedPreferences.Editor editor;
    SharedPreferences count;
    Boolean isFirst;
    EditText username,phonenumber;
    String Firstuser;
    int Data_Version,Net_Version;
    ProgressDialog progressDialog;
    ArrayList<User> AllUser;
    DatabaseHelper databaseHelper;
    Data_Verson data_verson;
    Handler myhandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            editor = count.edit();
            switch (msg.what)
            {
                case Get_Version:
                {
                    Net_Version = msg.arg1;
                    data_verson = (Data_Verson) msg.obj;
                    if (Net_Version==Data_Version)
                    {
                        BmobHandle.getAllUser(myhandle,false);
                    }
                    else
                    {
                        Data_Version=Net_Version;
                        BmobHandle.getAllUser(myhandle,true);
                        editor.putInt("Date_Version",Net_Version);
                        editor.apply();
                    }
                    break;
                }
                case INIT_DATA:
                {
                    if (isFirst)
                    {
                        databaseHelper = new DatabaseHelper(getApplicationContext());
                        AllUser = (ArrayList<User>) msg.obj;
                        Init_Data(AllUser);
                    }

                    else
                    {
                        msg.what=GO_MAIN;
                        myhandle.handleMessage(msg);
                    }
                    break;
                }
                case GO_MAIN:
                {
                    if (progressDialog!=null)
                    {
                        progressDialog.dismiss();
                    }
                    Intent i = new Intent(Loading.this,Main.class);
                    Bundle bundle = new Bundle();
                    AllUser=new ArrayList<>();
                    AllUser.addAll((List<User>)msg.obj);
                    for (User tempuser:AllUser)
                    {
                        if (tempuser.getUsername().equals(Firstuser))
                            user =tempuser;
                    }
                    bundle.putSerializable("AllUser",AllUser);
                    bundle.putSerializable("User",user);
                    bundle.putSerializable("Data_Version",data_verson);
                    Log.e("User",AllUser.size()+"");
                    i.putExtras(bundle);
                    startActivity(i);
                    if (databaseHelper!=null)
                    {
                        databaseHelper.close();
                    }
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
                        editor.apply();
                        showprogress();
                        BmobHandle.GetNet_Version(myhandle);
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
                        editor.apply();
                        showprogress();
                        BmobHandle.GetNet_Version(myhandle);
                    }
                    break;
                }
                case Go_ERROR:
                {
                     ErrorDialog();
                    break;
                }
                case CHECK_ERROR:
                {
                    CheckError();
                    break;
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
        CrashReport.initCrashReport(getApplicationContext(), "900053783", true);
        init();
    }
    private void init() {
        count = getPreferences(MODE_PRIVATE);
        isFirst = count.getBoolean("isfirst", true);
        Data_Version = count.getInt("Date_Version",1);
        if (isFirst) {

            final String phone = getPhoneNumber();
            BmobHandle.checkagreement(phone,myhandle);
        }
        else
        {
            Firstuser = count.getString("username",null);
            BmobHandle.GetNet_Version(myhandle);
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
    private void CheckError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Loading.this);
        builder.setTitle("错误");
        builder.setMessage("超级验证失败，请联系软件管理员进行核实。");
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
                return false;
            }
        });
        AlertDialog dialog =builder.create();
        dialog.show();

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);



    }
    private void showprogress()
    {
        progressDialog = ProgressDialog.show(Loading.this,"提示","数据初始化中！",true,false);
    }
    private void Init_Data(final ArrayList<User> allUser)
    {

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (User user :allUser)
                {
                    UserExist userExist = new UserExist();
                    userExist.setUsername(user.getUsername());
                    if (ifexist(user.getUsername()))
                        userExist.setExist(1);
                    else
                        userExist.setExist(0);
                    long e=databaseHelper.createUserExist(userExist);
                    Log.e("long",e+"Eee");
                }
                Message msg = new Message();
                msg.what=GO_MAIN;
                msg.obj =allUser;
                myhandle.handleMessage(msg);

            }
        }).start();
    }
    private static final String[] PHONES_PROJECT=new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
    public boolean ifexist(String name)
    {
        boolean check = false;
        ContentResolver resolver = Loading.this.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECT,null,null,null);
        if (cursor!=null)
        {
            while (cursor.moveToNext())
            {
                String temp_name = cursor.getString(0);
                if (temp_name.equals(name))
                {
                    check=true;
                    break;
                }

            }
        }
        if (cursor!=null)
            cursor.close();
        return check;
    }
}
