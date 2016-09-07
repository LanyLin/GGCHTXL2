package com.example.landy.ggchtxl2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Landy on 2015/11/2.
 */
public class Handle {


    public static ArrayList<User> SearchDate(ArrayList<User> AllUser,String key)
    {
        ArrayList<User> temp = new ArrayList<User>();
        for (User user: AllUser)
        {
            if (user.getUsername().indexOf(key)==0)
            {

                temp.add(user);
            }
        }
        return temp;
    }
    public static ArrayList<User> inserDate(ArrayList<User> AllUser,String type)
    {
        ArrayList<User> temp = new ArrayList<User>();
        for (User user :AllUser)
        {
            if (user.getGrade().equals(type)||user.getAcademy().equals(type))
            {

                temp.add(user);
            }

        }
        return temp;
    }
    public String[] getAllName(List<User> AllUser)
    {
        int i=0;
        String[] temp = new String[AllUser.size()];
        for (User user :AllUser)
        {
            temp[i]=user.getUsername();
            i++;
        }
        return temp;
    }







}
