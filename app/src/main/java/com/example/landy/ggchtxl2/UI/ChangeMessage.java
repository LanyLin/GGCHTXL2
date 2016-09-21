package com.example.landy.ggchtxl2.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.landy.ggchtxl2.Dao.BmobHandle;
import com.example.landy.ggchtxl2.Model.Data_Verson;
import com.example.landy.ggchtxl2.Model.User;
import com.example.landy.ggchtxl2.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ChangeMessage extends Activity {
    private final String path="/data/data/com.example.landy.ggchtxl2/";
    private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE=1;
    private final int IMAGE_CUT =2;
    private final int UPUser=1006;
    ArrayList<User> AllUsers;
    User user;
    ImageView back,Icon;
    RelativeLayout ChangeIcon,ChangeLongnum,ChangeShoutnum,ChangeDormitory,SetName,SetAcademy;
    TextView name,longnum,shoutnum,academy,dormitory;
    private static final String DATA_VERSIONID ="W2XpGGGP";
    int Data_Version;
    Button send;
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
        setContentView(R.layout.activity_change_message);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = (User) bundle.getSerializable("user");
        Data_Version =bundle.getInt("Data_Version");
        AllUsers = (ArrayList<User>) bundle.getSerializable("AllUser");
        back = (ImageView) findViewById(R.id.back);
        ChangeIcon = (RelativeLayout) findViewById(R.id.ChangeIcon);
        ChangeDormitory= (RelativeLayout) findViewById(R.id.ChangeDormitory);
        ChangeLongnum = (RelativeLayout) findViewById(R.id.ChangeLongnum);
        ChangeShoutnum = (RelativeLayout) findViewById(R.id.ChangeShoutnum);
        SetName = (RelativeLayout) findViewById(R.id.SetName);
        SetAcademy = (RelativeLayout) findViewById(R.id.SetAcademy);
        Icon = (ImageView) ChangeIcon.findViewById(R.id.icon);
        name = (TextView) SetName.findViewById(R.id.name);
        longnum = (TextView) ChangeLongnum.findViewById(R.id.longnum);
        shoutnum = (TextView) ChangeShoutnum.findViewById(R.id.shoutnum);
        academy = (TextView) SetAcademy.findViewById(R.id.academy);
        dormitory = (TextView) ChangeDormitory.findViewById(R.id.dormatory);
        name.setText(user.getUsername().toString());
        longnum.setText(user.getMobilePhoneNumber().toString());
        shoutnum.setText(user.getShoutnum().toString());
        academy.setText(user.getAcademy().toString());
        dormitory.setText(user.getDormitory().toString());
        if (user.getPic()!=null)
        {
            Picasso.with(getApplicationContext()).load(user.getPic().getFileUrl()).into(Icon);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ChangeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIcon();
            }
        });
        ChangeDormitory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangeMessage.this,ChangeItem.class);
                i.putExtra("Type","宿舍");
                i.putExtra("content",user.getDormitory().toString());
                startActivityForResult(i,0);
            }
        });
        ChangeLongnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangeMessage.this,ChangeItem.class);
                i.putExtra("Type","长号");
                i.putExtra("content",user.getMobilePhoneNumber().toString());
                startActivityForResult(i,0);
            }
        });
        ChangeShoutnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangeMessage.this,ChangeItem.class);
                i.putExtra("Type","短号");
                i.putExtra("content",user.getShoutnum().toString());
                startActivityForResult(i,0);

            }
        });
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void setIcon() {

        Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_PICK);
                /* 取得相片后返回本画面 */
        startActivityForResult(intent, 1);
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
                Icon.setImageBitmap(iconbm);
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
            else if (requestCode ==0)
            {
                String type = data.getStringExtra("Type");
                String content = data.getStringExtra("content");
                if (type.equals("长号"))
                {
                    user.setMobilePhoneNumber(content);
                    longnum.setText(content);
                }
                else if (type.equals("短号"))
                {
                    user.setShoutnum(content);
                    shoutnum.setText(content);
                }
                else if (type.equals("宿舍"))
                {
                    user.setDormitory(content);
                    dormitory.setText(content);
                }
            }
        }


    }
}
