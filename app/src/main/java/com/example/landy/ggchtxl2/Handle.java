package com.example.landy.ggchtxl2;


import java.util.ArrayList;
import java.util.List;



public class Handle {

    public static ArrayList<ArrayList<User>> returnData(ArrayList<User> AllUser,ArrayList<String> GradeList)
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
