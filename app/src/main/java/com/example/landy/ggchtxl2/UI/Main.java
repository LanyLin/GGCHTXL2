package com.example.landy.ggchtxl2.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.example.landy.ggchtxl2.Adapter.AutoTextAdapter;
import com.example.landy.ggchtxl2.Dao.BmobHandle;
import com.example.landy.ggchtxl2.Model.Data_Verson;
import com.example.landy.ggchtxl2.Dao.Handle;
import com.example.landy.ggchtxl2.R;
import com.example.landy.ggchtxl2.Model.User;
import com.squareup.picasso.Picasso;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class Main extends Activity {
    private final String path="/data/data/com.example.landy.ggchtxl2/";
    private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE=1;
    private final int IMAGE_CUT =2;
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
                ChangeMassage();
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
                setIcon();
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
                            editor.apply();
                            AllUsers=Handle.updateAllUser(AllUsers,user);
                            User temp = new User();
                            temp.setPic(bmobFile);
                            temp.update(user.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null)
                                    {
                                        Toast.makeText(getApplicationContext(),"上传成功",Toast.LENGTH_LONG).show();
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
    public void ChangeMassage()
    {
        final AlertDialog.Builder builder= new AlertDialog.Builder(Main.this);
        final LinearLayout l = (LinearLayout)getLayoutInflater().inflate(R.layout.sendsuggest,null);
        final AlertDialog show = builder.create();
        show.setView(l);
        show.setTitle("修改个人信息");
        final EditText longnumChange = (EditText)l.findViewById(R.id.longnumChange);
        final EditText shoutnumChange = (EditText)l.findViewById(R.id.ShoutnumChange);
        final EditText DorimitoryChagne = (EditText)l.findViewById(R.id.DorimitoryChange);
        longnumChange.setHint(user.getMobilePhoneNumber());
        shoutnumChange.setHint(user.getShoutnum());
        DorimitoryChagne.setHint(user.getDormitory());
        show.setButton(AlertDialog.BUTTON_NEGATIVE,"取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                show.dismiss();
            }
        });
        show.setButton(DialogInterface.BUTTON_POSITIVE,"发送",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (longnumChange.getText().toString().equals("")&&shoutnumChange.getText().toString().equals("")&&DorimitoryChagne.getText().toString().equals(""))
                {
                    Toast.makeText(getApplication(),"数据不能为空",Toast.LENGTH_LONG).show();
                    setnotClose(show);
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
                                AllUsers=Handle.updateAllUser(AllUsers,user);
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
    }
    private void setnotClose(AlertDialog dialog)
    {
        try{
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog,false);
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
        drawerLayout.openDrawer(Gravity.START);
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
