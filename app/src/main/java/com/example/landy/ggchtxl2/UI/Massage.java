package com.example.landy.ggchtxl2.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.Contacts;
import android.provider.Contacts.Intents;

import com.example.landy.ggchtxl2.PicTransFormation.BlurImageTransformation;
import com.example.landy.ggchtxl2.PicTransFormation.CircleImageTransformation;
import com.example.landy.ggchtxl2.R;
import com.example.landy.ggchtxl2.Model.User;
import com.squareup.picasso.Picasso;


public class Massage extends Activity {
    TextView name,longnum,shoutnum,acadamy,grade,dormitory;
    ImageView Call,Message,Load,Icon,background;
    String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_massage);
        name = (TextView)findViewById(R.id.name);
        longnum = (TextView)findViewById(R.id.longnum);
        shoutnum = (TextView)findViewById(R.id.shoutnum);
        grade = (TextView)findViewById(R.id.grade);
        acadamy = (TextView)findViewById(R.id.academy);
        dormitory =(TextView)findViewById(R.id.dormatory);
        background  = (ImageView) findViewById(R.id.message_background);
        Intent i = getIntent();
        Bundle bundle =i.getExtras();
        User user = (User) bundle.getSerializable("User");
        if (user!=null)
        {
            name.setText(user.getUsername());
            longnum.setText(user.getMobilePhoneNumber());
            shoutnum.setText(user.getShoutnum());
            grade.setText(user.getGrade());
            acadamy.setText(user.getAcademy());
            dormitory.setText(user.getDormitory());
        }
        Call = (ImageView) this.findViewById(R.id.call);
        Message = (ImageView) this.findViewById(R.id.message);
        Load = (ImageView) this.findViewById(R.id.load);
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String longnum_temp = longnum.getText().toString();
                String shoutnum_temp = shoutnum.getText().toString();
                if (!longnum_temp.equals("")&&shoutnum_temp.equals(""))
                {
                    Intent i  =new Intent();
                    i.setAction(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:" + longnum_temp));
                    startActivity(i);
                }
                else
                {
                    String[] temp={longnum_temp,shoutnum_temp};
                    Choice_Num_Call(v, temp);
                }
            }
        });
        Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String longnum_temp = longnum.getText().toString();
                String shoutnum_temp = shoutnum.getText().toString();
                if (!longnum_temp.equals("")&&shoutnum_temp.equals(""))
                {
                    Intent i  =new Intent();
                    i.setAction(Intent.ACTION_SENDTO);
                    Uri uri= Uri.parse("smsto:"+longnum_temp);
                    i.setData(uri);
                    startActivity(i);
                }
                else
                {
                    String[] temp={longnum_temp,shoutnum_temp};
                    Choice_Num_Send(v, temp);
                }
            }
        });
        Load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri insert = Contacts.People.CONTENT_URI;
                Intent i = new Intent(Intent.ACTION_INSERT,insert);
                i.putExtra(Intents.Insert.NAME, name.getText().toString());
                i.putExtra(Intents.Insert.PHONE,longnum.getText().toString());
                i.putExtra(Intents.Insert.PHONE_TYPE, "长号");
                i.putExtra(Intents.Insert.SECONDARY_PHONE,shoutnum.getText().toString());
                i.putExtra(Intents.Insert.SECONDARY_PHONE_TYPE, "短号");
                i.putExtra(Intents.Insert.POSTAL,dormitory.getText().toString());
                i.putExtra(Intents.Insert.POSTAL_TYPE, "宿舍");
                startActivity(i);
            }
        });
        Icon = (ImageView)findViewById(R.id.Icon);
        if (user!=null)
        if (user.getPic()!=null)
        {
            Log.e("pic",user.getPic().getFileUrl());
            Picasso.with(getApplicationContext()).load(user.getPic().getFileUrl()).transform(new BlurImageTransformation()).into(background);
            Picasso.with(getApplicationContext()).load(user.getPic().getFileUrl()).transform(new CircleImageTransformation()).into(Icon);
        }



    }
    private void Choice_Num_Send(View v,final String[] temp)
    {
        num=temp[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(Massage.this);
        builder.setTitle("选择长短号");
        builder.setSingleChoiceItems(temp,1,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                num=temp[which];
            }
        });
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i  =new Intent();
                i.setAction(Intent.ACTION_SENDTO);
                Uri uri= Uri.parse("smsto:"+num);
                i.setData(uri);
                startActivity(i);
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void Choice_Num_Call(View v, final String[] temp) {
        // TODO 自动生成的方法存根
        num=temp[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(Massage.this);
        builder.setTitle("选择长短号");
        builder.setSingleChoiceItems(temp,1,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                num=temp[which];
            }
        });
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i  =new Intent();
                i.setAction(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+num));
                startActivity(i);
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();

    }

    public void Back(View v)
    {
        finish();
    }

}
