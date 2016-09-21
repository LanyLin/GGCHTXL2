package com.example.landy.ggchtxl2.Dao;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.landy.ggchtxl2.Model.Data_Verson;
import com.example.landy.ggchtxl2.Model.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class BmobHandle {
    private static final int GO_MAIN=1000;
    private static final int CHECK_AGREE=1001;
    private static final int CHECK_AUTH=1002;
    private static final int Go_ERROR=1003;
    private static final String DATA_VERSIONID ="W2XpGGGP";
    private static final int Get_Version=1004;
    private static final int CHECK_ERROR=1005;
    private static final int UPUser=1006;
    public static void UpdataUser(final Handler handler)
    {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereNotEqualTo("username","");
        query.setLimit(500);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e==null)
                {
                    Message msg = new Message();
                    msg.obj=list;
                    msg.what = UPUser;
                    handler.sendMessage(msg);
                }
            }
        });
    }
    /**
     *
     * 获取所有数据
     */
    public static  void getAllUser(final Handler handler, Boolean Need_Update)
    {
        final BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereNotEqualTo("username","");
        query.order("Grade");
        query.setLimit(500);
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        if (Need_Update)
        {
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e==null)
                    {
                        query.clearCachedResult(User.class);
                        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
                        query.findObjects(new FindListener<User>() {
                            @Override
                            public void done(List<User> list, BmobException e) {
                                if (e==null)
                                {
                                    Message msg = new Message();
                                    msg.what=GO_MAIN;
                                    msg.obj=list;
                                    handler.sendMessageDelayed(msg,0);
                                }
                                else
                                {
                                    Message msg = new Message();
                                    msg.what=Go_ERROR;

                                    Log.e("error","获取数据失败"+e.getErrorCode()+e.toString());
                                    handler.sendMessage(msg);
                                }
                            }
                        });
                    }
                    else
                    {
                        Message msg = new Message();
                        msg.what=Go_ERROR;
                        Log.e("error","获取数据失败"+e.getErrorCode()+e.toString());
                        handler.sendMessage(msg);
                    }
                }
            });
        }
        else
        {
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e==null)
                    {
                        Message msg = new Message();
                        msg.what=GO_MAIN;
                        msg.obj=list;
                        handler.sendMessageDelayed(msg,2000);
                    }
                    else
                    {
                        Message msg = new Message();
                        msg.what=Go_ERROR;
                        Log.e("error","获取数据失败"+e.getErrorCode()+e.toString());
                        handler.sendMessage(msg);
                    }
                }
            });
        }
    }
    public static void checkagreement(String phoneNumber,final Handler handler)
    {
        final List<User>[] userList = new List[1];
        BmobQuery<User> query =new BmobQuery<>();
        query.addWhereEqualTo("mobilePhoneNumber",phoneNumber);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e==null)
                {

                    Message msg = new Message();
                    msg.what=CHECK_AGREE;
                    if (list.size()==0)
                    {
                        handler.handleMessage(msg);
                    }
                    else
                    {
                        msg.obj = list.get(0);
                        handler.handleMessage(msg);
                    }
                }
                else
                {
                    handler.sendEmptyMessage(Go_ERROR);
                }
            }
        });

    }
    public static void checkagreement(String username, final String phone, final Handler handler)
    {
        final List<User>[] userlist = new List[1];
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("username",username);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e==null)
                {
                    userlist[0]=list;
                    Message msg = new Message();
                    msg.what=CHECK_AUTH;
                    User user= userlist[0].get(0);
                    if (user.getMobilePhoneNumber().equals(phone))
                    {
                        msg.obj=userlist[0].get(0);
                        handler.handleMessage(msg);
                    }
                    else
                    {
                        handler.sendEmptyMessage(CHECK_ERROR);
                    }

                }
                else {
                    handler.sendEmptyMessage(Go_ERROR);
                }
            }
        });

    }
    public static ArrayList<String> getKeyValue(final String key,ArrayList<User> AllUser)
    {
        final ArrayList<String> temp_list = new ArrayList<>();
        if (key.equals("Grade"))
        {
            for (User user : AllUser)
            {
                if (!temp_list.contains(user.getGrade()))
                    temp_list.add(user.getGrade());
            }
        }
        else if(key.equals("Academy"))
        {
            for (User user : AllUser)
            {
                if (!temp_list.contains(user.getAcademy()))
                    temp_list.add(user.getAcademy());
            }
        }

        return temp_list;
    }
    public static void GetNet_Version(final Handler handler)
    {
        BmobQuery<Data_Verson> query = new BmobQuery<>();
        query.getObject(DATA_VERSIONID, new QueryListener<Data_Verson>() {
            @Override
            public void done(Data_Verson dataVerson, BmobException e) {
                if (e==null)
                {
                    Message msg = new Message();
                    msg.what=Get_Version;
                    msg.arg1= dataVerson.getVersion();
                    handler.sendMessage(msg);
                }
                else
                {
                    Log.e("error",e.toString());
                    Message msg = new Message();
                    msg.what=Go_ERROR;

                    Log.e("error","获取数据失败"+e.getErrorCode()+e.toString());
                    handler.sendMessage(msg);
                }
            }
        });

    }

}
