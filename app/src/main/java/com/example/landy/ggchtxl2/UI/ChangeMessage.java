package com.example.landy.ggchtxl2.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.landy.ggchtxl2.Model.Data_Verson;
import com.example.landy.ggchtxl2.Model.User;
import com.example.landy.ggchtxl2.R;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ChangeMessage extends Activity {
    private static final String DATA_VERSIONID ="W2XpGGGP";
    private final String SD_CARD_ROOT = "sdcard";
    User user;
    ImageView back,Icon;
    RelativeLayout ChangeIcon,ChangeLongnum,ChangeShoutnum,ChangeDormitory,SetName,SetAcademy;
    TextView name,longnum,shoutnum,academy,dormitory;
    Button send;
    User temp = new User();
    int Data_Version;
    ProgressDialog progressDialog;
    private String mImageTmpPath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_message);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = (User) bundle.getSerializable("user");
        Data_Version = bundle.getInt("Data_Version");
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
        name.setText(user.getUsername());
        longnum.setText(user.getMobilePhoneNumber());
        if(user.getShoutnum()!=null)
        {
            shoutnum.setText(user.getShoutnum());
        }
        academy.setText(user.getAcademy());
        dormitory.setText(user.getDormitory());
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
                setIconLocal();
            }
        });
        ChangeDormitory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangeMessage.this,ChangeItem.class);
                i.putExtra("Type","宿舍");
                i.putExtra("content",user.getDormitory());
                startActivityForResult(i,0);
            }
        });
        ChangeLongnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangeMessage.this,ChangeItem.class);
                i.putExtra("Type","长号");
                i.putExtra("content",user.getMobilePhoneNumber());
                startActivityForResult(i,0);
            }
        });
        ChangeShoutnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangeMessage.this,ChangeItem.class);
                i.putExtra("Type","短号");
                if (user.getShoutnum()!=null)
                {
                    i.putExtra("content",user.getShoutnum());
                }
                startActivityForResult(i,0);

            }
        });
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showprogress("数据提交中");
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
                                        Intent intent1 = new Intent();
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putSerializable("user",user);
                                        bundle1.putInt("Data_Version",Data_Version);
                                        intent1.putExtras(bundle1);
                                        setResult(RESULT_OK,intent1);
                                        progressDialog.dismiss();
                                        finish();
                                        Toast.makeText(getApplicationContext(),"更新成功",Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"更新失败",Toast.LENGTH_LONG).show();
                                        Log.e("message",e.toString());
                                    }
                                }
                            });

                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"更新失败",Toast.LENGTH_LONG).show();
                            Log.e("message",e.toString());
                        }
                    }
                });

            }
        });
    }



    private void setIconLocal() {

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
            int IMAGE_CODE = 1;
            int IMAGE_CUT = 2;
            int IMAGE_CAMERA=3;
            if (requestCode == IMAGE_CODE)
            {
                try{
                    uri =data.getData();
                    Log.e("uri",uri.toString());
                    Intent intent = new Intent();
                    intent.setAction("com.android.camera.action.CROP");
                    String IMAGE_TYPE = "image/*";
                    intent.setDataAndType(uri, IMAGE_TYPE);
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 1);// 裁剪框比例
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 150);// 输出图片大小
                    intent.putExtra("outputY", 150);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, IMAGE_CUT);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (requestCode == IMAGE_CUT)
            {
                iconbm = data.getParcelableExtra("data");
                Icon.setImageBitmap(iconbm);
                FileOutputStream fileOutputStream = null;
                String path = "/data/data/com.example.landy.ggchtxl2/";
                try {
                    Log.e("path", path +user.getUsername()+".png");
                    fileOutputStream = new FileOutputStream(path +user.getUsername()+".png");
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
                final BmobFile bmobFile = new BmobFile(new File(path +user.getUsername()+".png"));
                if (user.getPic()!=null)
                {
                    showprogress("图片上传中");
                    BmobFile delete = new BmobFile();
                    delete.setUrl(user.getPic().getUrl());
                    delete.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null)
                            {
                                Log.e("message","删除成功");
                                bmobFile.uploadblock(new UploadFileListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e==null)
                                        {
                                            bmobFile.getFileUrl();
                                            user.setPic(bmobFile);
                                            temp.setPic(bmobFile);
                                            Toast.makeText(getApplicationContext(),"上传成功",Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"上传失败",Toast.LENGTH_LONG).show();
                                            Log.e("error",e.toString());
                                            progressDialog.dismiss();
                                        }

                                    }
                                });
                            }
                            else
                            {
                                Log.e("message",e.toString());
                                bmobFile.uploadblock(new UploadFileListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e==null)
                                        {
                                            bmobFile.getFileUrl();
                                            user.setPic(bmobFile);
                                            temp.setPic(bmobFile);
                                            Toast.makeText(getApplicationContext(),"上传成功",Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"上传失败",Toast.LENGTH_LONG).show();
                                            Log.e("error",e.toString());
                                            progressDialog.dismiss();
                                        }

                                    }
                                });
                            }
                        }
                    });
                }
                else
                {
                    showprogress("图片上传中");
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null)
                            {
                                bmobFile.getFileUrl();
                                user.setPic(bmobFile);
                                temp.setPic(bmobFile);
                                Toast.makeText(getApplicationContext(),"上传成功",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"上传失败",Toast.LENGTH_LONG).show();
                                Log.e("error",e.toString());
                                progressDialog.dismiss();
                            }

                        }
                    });
                }


            }
            else if (requestCode ==0)
            {
                String type = data.getStringExtra("Type");
                String content = data.getStringExtra("content");
                if (type.equals("长号"))
                {
                    user.setMobilePhoneNumber(content);
                    temp.setMobilePhoneNumber(content);
                    longnum.setText(content);
                }
                if (type.equals("短号"))
                {
                    user.setShoutnum(content);
                    temp.setShoutnum(content);
                    shoutnum.setText(content);
                }
                if (type.equals("宿舍"))
                {
                    user.setDormitory(content);
                    temp.setDormitory(content);
                    dormitory.setText(content);
                }
            }
        }


    }
    private void showprogress(String temp)
    {
        progressDialog = ProgressDialog.show(this,"提示",temp,true,false);
    }
}
