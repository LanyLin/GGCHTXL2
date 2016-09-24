package com.example.landy.ggchtxl2.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Main extends Activity {
    private final String path="/data/data/com.example.landy.ggchtxl2/";
    private final String IMAGE_TYPE = "image/*";
    private final String URL = "http://120.24.212.93/createwebsite/admin.php/member/android_remark";
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
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
    SharedPreferences count;
    ArrayList<String> AcademyList;
    ArrayList<String> Gradelist;
    String tempsuggest;
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            try {

                OkHttpClient client = new OkHttpClient();
                String strutf= URLEncoder.encode(tempsuggest,"utf-8");
                Log.e("text",strutf);
                RequestBody requestBody=RequestBody.create(MEDIA_TYPE_MARKDOWN,"remark="+strutf+"&");
                Request request = new Request.Builder().url(URL).post(requestBody)
                        .addHeader("cache-control","no-cache")
                        .addHeader("content-type","application/x-www-form-urlencoded; charset=utf-8")
                        .build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful())
                {
                    String result = response.body().string();
                    Log.e("text",result);
                    if (result!=null)
                    {
                        result = result.substring(1);
                    }
                    JSONObject obj = new JSONObject(result);
                    String resultcode = obj.getString("ResultCode");
                    Looper.prepare();
                    if (resultcode.equals("0"))
                    {
                        Toast.makeText(getApplicationContext(),"反馈成功",Toast.LENGTH_LONG).show();

                    }else
                    {
                        Toast.makeText(getApplicationContext(),"反馈失败,请检查网络是否畅通",Toast.LENGTH_LONG).show();
                    }
                    Looper.loop();
                }
                else {
                    throw  new IOException(response+"eeee");
                }




            }catch (Exception e)
            {
                Log.e("error",e.toString());
            }
        }
    };
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
        count = getPreferences(MODE_PRIVATE);
        final Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        AllUsers = (ArrayList<User>) bundle.getSerializable("AllUser");
        AcademyList = BmobHandle.getKeyValue("Academy",AllUsers);
        Gradelist = BmobHandle.getKeyValue("Grade",AllUsers);
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
        Button Update = (Button) frameLayoutleft.findViewById(R.id.CheckUpdate);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 检测更新代码
                 */
            }
        });
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
                startActivityForResult(i,4);
            }
        });
        Button About =(Button) frameLayoutleft.findViewById(R.id.about);
        Button Import = (Button) frameLayoutleft.findViewById(R.id.Import);
        Import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long starttiam =System.currentTimeMillis();
                Intent intent1 = new Intent(Main.this,ImportAll.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("list",AcademyList);
                bundle1.putSerializable("AllUser",AllUsers);
                intent1.putExtras(bundle1);
                long endtime =System.currentTimeMillis();
                Log.e("time",endtime-starttiam+"eeee");
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
                long starttiam =System.currentTimeMillis();
                Intent i =  new Intent(Main.this,ChangeMessage.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("user",user);
                i.putExtras(bundle1);
                long endtime =System.currentTimeMillis();
                Log.e("time",endtime-starttiam+"eeee");
                startActivityForResult(i,3);
            }
        });
        SearchByGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                Intent intent = new Intent(Main.this,SearchByType.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list",AcademyList);
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

    private ArrayList<ArrayList<User>> resetChildlist(ArrayList<ArrayList<User>> childList) {
        ArrayList<ArrayList<User>> temp = new ArrayList<>();
        for (ArrayList<User> list: childList)
        {
            ArrayList<User> item = new ArrayList<>();
            for (User user : list)
            {
                if (ifexist(user.getUsername()))
                    continue;
                else
                    item.add(user);
            }
            temp.add(item);
        }
        return temp;
    }
    private static final String[] PHONES_PROJECT=new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
    public boolean ifexist(String name)
    {
        boolean check = false;
        ContentResolver resolver = Main.this.getContentResolver();
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
            else if (requestCode==3)
            {
                Bundle bundle;
                bundle = data.getExtras();
                User temp = (User) bundle.getSerializable("tempUser");
                user = (User) bundle.getSerializable("user");
                temp.update(user.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null)
                        {
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
                            Toast.makeText(getApplicationContext(),"更新成功",Toast.LENGTH_LONG).show();
                            BmobHandle.UpdataUser(myhandle);

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"更新失败",Toast.LENGTH_LONG).show();
                            Log.e("error",e.toString());
                        }
                    }
                });
            }
            else if (requestCode ==4)
            {
                tempsuggest = data.getStringExtra("suggest");
                new Thread(networkTask).start();
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
