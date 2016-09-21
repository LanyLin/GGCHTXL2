package com.example.landy.ggchtxl2.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.example.landy.ggchtxl2.Adapter.AutoTextAdapter;
import com.example.landy.ggchtxl2.Dao.BmobHandle;
import com.example.landy.ggchtxl2.Model.Data_Verson;
import com.example.landy.ggchtxl2.Dao.Handle;
import com.example.landy.ggchtxl2.R;
import com.example.landy.ggchtxl2.Model.User;
import com.squareup.picasso.Picasso;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONObject;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;



public class Main extends Activity {
    private final String path="/data/data/com.example.landy.ggchtxl2/";
    private final String IMAGE_TYPE = "image/*";
    private final String URL = "http://120.24.212.93/createwebsite/admin.php/member/android_remark";
    private final int IMAGE_CODE=1;
    private final int IMAGE_CUT =2;
    private final int UPUser=1006;
    private static final String DATA_VERSIONID ="W2XpGGGP";
    AutoCompleteTextView autoCompleteTextView;
    String[] allName;
    Handle H = new Handle();
    FrameLayout frameLayoutleft;
    private DrawerLayout drawerLayout;
    ImageView icon;
    Button SearchByGrade,SearchByAcademy;
    ArrayList<User> AllUsers;
    ImageView tower;
    User user;
    int Data_Version;
    TextView Academy_Text,Grade_Text;
    SharedPreferences.Editor editor;
    SharedPreferences count;


    Handler myhandle = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case UPUser:{
                    AllUsers = (ArrayList<User>) msg.obj;
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
        setContentView(R.layout.activity_main);
        //判断是否是同乡
        //init();
        count = getPreferences(MODE_PRIVATE);
        final Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        AllUsers = (ArrayList<User>) bundle.getSerializable("AllUser");
        user = (User) bundle.getSerializable("User");
        Data_Version = (int)bundle.getInt("Data_Version");
        allName = H.getAllName(AllUsers);
        Log.e("Data_Version",Data_Version+"");
        //CrashReport.testJavaCrash();
        Toast.makeText(getApplicationContext(),"欢迎你"+user.getUsername(),Toast.LENGTH_LONG).show();
        final AutoTextAdapter autoTextAdapter = new AutoTextAdapter(allName,this);
        autoCompleteTextView = (AutoCompleteTextView)this.findViewById(R.id.Searchbar);
        autoCompleteTextView.setAdapter(autoTextAdapter);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                autoCompleteTextView.setAdapter(autoTextAdapter);
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = (String) parent.getItemAtPosition(position);
                User user = Handle.FindUser(AllUsers,username);
                Intent i = new Intent(Main.this,Massage.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("User",user);
                i.putExtras(bundle1);
                startActivity(i);
            }
        });
        drawerLayout = (DrawerLayout)this.findViewById(R.id.drawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        SearchByGrade = (Button)drawerLayout.findViewById(R.id.FindGrade);
        SearchByAcademy = (Button)drawerLayout.findViewById(R.id.FindAcademy);
        frameLayoutleft = (FrameLayout)this.findViewById(R.id.drawer_left);
        Button Fin = (Button)frameLayoutleft.findViewById(R.id.Finish);
        Fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Finish();
            }
        });
        Button Feeback = (Button) frameLayoutleft.findViewById(R.id.feeback);
        Feeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(Main.this,SendSuggest.class);
                startActivity(i);
            }
        });
        Button About =(Button) frameLayoutleft.findViewById(R.id.about);
        Button Import = (Button) frameLayoutleft.findViewById(R.id.Import);
        Import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> GradeList = BmobHandle.getKeyValue("Academy",AllUsers);
                Intent intent1 = new Intent(Main.this,ImportAll.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("list",GradeList);
                bundle1.putSerializable("AllUser",AllUsers);
                intent1.putExtras(bundle1);
                startActivity(intent1);
            }
        });
        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_dialog();
            }
        });
        icon = (ImageView)frameLayoutleft.findViewById(R.id.icon);
        if (user.getPic()!=null)
        {
            Picasso.with(getApplicationContext()).load(user.getPic().getFileUrl()).into(icon);
        }
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ChangeMassage();
                Intent i =  new Intent(Main.this,ChangeMessage.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("user",user);
                bundle1.putInt("Data_Version",Data_Version);
                bundle1.putSerializable("AllUser",AllUsers);
                i.putExtras(bundle1);
                startActivity(i);
            }
        });
        SearchByGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> Gradelist = BmobHandle.getKeyValue("Grade",AllUsers);
                Intent intent = new Intent(Main.this,SearchByType.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list",Gradelist);
                bundle.putSerializable("AllUser",AllUsers);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        SearchByAcademy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> Academylist = BmobHandle.getKeyValue("Academy",AllUsers);
                Intent intent = new Intent(Main.this,SearchByType.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list",Academylist);
                bundle.putSerializable("AllUser",AllUsers);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.alpha);
        animation.setStartOffset(2000);
        SearchByAcademy.startAnimation(animation);
        SearchByGrade.startAnimation(animation);
        tower = (ImageView) findViewById(R.id.tower);
        tower.setAnimation(AnimationUtils.loadAnimation(this,R.anim.translate));
        Academy_Text = (TextView)findViewById(R.id.Academy_Text);
        Grade_Text = (TextView)findViewById(R.id.Grade_Text);
        Academy_Text.startAnimation(animation);
        Grade_Text.startAnimation(animation);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode==RESULT_OK)
        {
            Bitmap iconbm;
            final Uri uri;
            if (requestCode ==IMAGE_CODE)
            {
                try{
                    uri =data.getData();
                    Log.e("uri",uri.toString());
                    Intent intent = new Intent();
                    intent.setAction("com.android.camera.action.CROP");
                    intent.setDataAndType(uri,IMAGE_TYPE);
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 1);// 裁剪框比例
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 150);// 输出图片大小
                    intent.putExtra("outputY", 150);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent,IMAGE_CUT);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (requestCode ==IMAGE_CUT)
            {
                iconbm = data.getParcelableExtra("data");
                icon.setImageBitmap(iconbm);
                FileOutputStream fileOutputStream = null;
                try {
                    Log.e("path",path+user.getUsername()+".png");
                    fileOutputStream = new FileOutputStream(path+user.getUsername()+".png");
                    iconbm.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
                }catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }finally {
                    if (null!=fileOutputStream)
                        try{
                            fileOutputStream.close();
                        }catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                }
                final BmobFile bmobFile = new BmobFile(new File(path+user.getUsername()+".png"));
                if (user.getPic()!=null)
                {
                    BmobFile delete = new BmobFile();
                    delete.setUrl(user.getPic().getUrl());
                    delete.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null)
                            {
                                Log.e("message","删除成功");
                            }
                        }
                    });
                }
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null)
                        {
                            bmobFile.getFileUrl();
                            user.setPic(bmobFile);
                            ++Data_Version;
                            Data_Verson dataVerson = new Data_Verson();
                            dataVerson.setValue("Version",Data_Version);
                            dataVerson.update(DATA_VERSIONID, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null)
                                    {
                                        Log.e("Date_Version",Data_Version+"");
                                    }
                                }
                            });

                            User temp = new User();
                            temp.setPic(bmobFile);
                            temp.update(user.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null)
                                    {
                                        Toast.makeText(getApplicationContext(),"上传成功",Toast.LENGTH_LONG).show();
                                        BmobHandle.UpdataUser(myhandle);
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"上传失败",Toast.LENGTH_LONG).show();
                                        Log.e("error",e.toString());
                                    }
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"上传失败",Toast.LENGTH_LONG).show();
                            Log.e("error",e.toString());
                        }

                    }
                });




            }
        }


    }

    /**
     *软件信息窗口
     */
    public void message_dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
        builder.setTitle("关于本软件");

        LinearLayout l = (LinearLayout)getLayoutInflater().inflate(R.layout.message_dialog,null);

        builder.setView(l);
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }


    /**
     * 修改个人信息
     */
    /*public void ChangeMassage()
    {
        final AlertDialog.Builder builder= new AlertDialog.Builder(Main.this);
        final LinearLayout l = (LinearLayout)getLayoutInflater().inflate(R.layout.changemassage,null);
        final AlertDialog show = builder.create();
        show.setView(l);
        show.setTitle("修改个人信息");
        final EditText longnumChange = (EditText)l.findViewById(R.id.longnumChange);
        final EditText shoutnumChange = (EditText)l.findViewById(R.id.ShoutnumChange);
        final EditText DorimitoryChagne = (EditText)l.findViewById(R.id.DorimitoryChange);
        final ImageView head = (ImageView) l.findViewById(R.id.head);
        if (user.getPic()!=null)
        {
            Picasso.with(getApplicationContext()).load(user.getPic().getFileUrl()).into(head);
        }
        longnumChange.setHint(user.getMobilePhoneNumber());
        shoutnumChange.setHint(user.getShoutnum());
        DorimitoryChagne.setHint(user.getDormitory());
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIcon();
            }
        });
        show.setButton(AlertDialog.BUTTON_NEGATIVE,"取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setnotClose(show,true);
                show.dismiss();

            }
        });
        show.setButton(DialogInterface.BUTTON_POSITIVE,"发送",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (longnumChange.getText().toString().equals("")&&shoutnumChange.getText().toString().equals("")&&DorimitoryChagne.getText().toString().equals(""))
                {
                    Toast.makeText(getApplication(),"数据不能为空",Toast.LENGTH_LONG).show();
                    setnotClose(show,false);
                }
                else
                {
                    User tempUser = new User();
                    if (!longnumChange.getText().toString().equals(""))
                    {
                        tempUser.setMobilePhoneNumber(longnumChange.getText().toString());
                        user.setMobilePhoneNumber(longnumChange.getText().toString());
                    }
                    if (!shoutnumChange.getText().toString().equals(""))
                    {
                        tempUser.setShoutnum(shoutnumChange.getText().toString());
                        user.setShoutnum(shoutnumChange.getText().toString());
                    }
                    if (!DorimitoryChagne.getText().toString().equals(""))
                    {
                        tempUser.setDormitory(DorimitoryChagne.getText().toString());
                        user.setDormitory(DorimitoryChagne.getText().toString());
                    }
                    //BmobHandle.UpdateUser(getApplicationContext(),tempUser,user.getObjectId());
                    tempUser.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null)
                            {
                                Log.e("Date_Version",Data_Version+"");
                                Data_Version++;
                                Data_Verson dataVerson = new Data_Verson();
                                dataVerson.setValue("Version",Data_Version);
                                dataVerson.update(DATA_VERSIONID, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e==null)
                                        {
                                            Log.e("Date_Version",Data_Version+"");
                                        }
                                        else
                                            Log.e("Date_Version",e.toString());
                                    }
                                });
                                BmobHandle.UpdataUser(myhandle);
                                Toast.makeText(getApplicationContext(),"数据更新成功,下次启动生效",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"数据更新失败，请注意格式",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    show.dismiss();
                }
            }
        });
        show.show();
    }*/
    private void setnotClose(AlertDialog dialog,boolean IsOrNot)
    {
        try{
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog,IsOrNot);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void Finish() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
        builder.setTitle("退出");
        builder.setMessage("是否要退出？");

        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    public void  Show(View v)
    {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void Search(View v)
    {
        String temp = autoCompleteTextView.getText().toString();
        ArrayList<User> itemlist;
        itemlist = Handle.SearchDate(AllUsers,temp);
        if (itemlist.size()==0)
        {
            Toast.makeText(getApplicationContext(),"未存在该用户",Toast.LENGTH_LONG).show();
        }
        else if (itemlist.size()==1)
        {
            Bundle bundle = new Bundle();
            for (User user:itemlist)
            {
               bundle.putSerializable("User",user);
            }
            Intent intent = new Intent(Main.this,Massage.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(Main.this,DoubleMessage.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data",itemlist);
            intent.putExtras(bundle);
            startActivity(intent);
        }


    }
}
