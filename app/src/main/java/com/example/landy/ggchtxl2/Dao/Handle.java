package com.example.landy.ggchtxl2.Dao;


import com.example.landy.ggchtxl2.Model.User;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Handle {

    public static ArrayList<ArrayList<User>> returnData(ArrayList<User> AllUser, ArrayList<String> GradeList)
    {
        ArrayList<ArrayList<User>> temp = new ArrayList<>();
        for (int i=0;i<GradeList.size();i++)
        {
            ArrayList<User> item;
            item = inserDate(AllUser,GradeList.get(i));
            temp.add(item);
        }
        return temp;
    }
    public static ArrayList<User> SearchDate(ArrayList<User> AllUser,String key)
    {
        ArrayList<User> temp = new ArrayList<>();
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
        ArrayList<User> temp = new ArrayList<>();
        for (User user :AllUser)
        {
            if (user.getGrade().equals(type)||user.getAcademy().equals(type))
            {

                temp.add(user);
            }

        }
        return temp;
    }
    public static User FindUser(List<User> AllUser,String UserName)
    {
        for (User user:AllUser)
        {
            if (user.getUsername().equals(UserName))
            {
                return user;
            }
        }
        return null;
    }
    public static Map<Integer,User> ChangeType(List<User> list)
    {
        Map<Integer,User> map = new HashMap<>();
        int i=0;
        for (User item: list)
        {
            map.put(i,item);
            i++;
        }
        return map;
    }
    public static byte[] readBytes(InputStream is)
    {
        try
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((len=is.read())!=-1)
            {
                byteArrayOutputStream.write(buffer,0,len);
            }
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
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
    public String[] getAllNamePy(List<User> AllUser)
    {
        int i = 0;
        String[] temp = new String[AllUser.size()];
        for (User user :AllUser)
        {
            temp[i] = user.getPingYin();
            i++;
        }
        return temp;
    }






}
