package com.example.landy.ggchtxl2;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class BmobHandle {
    private static final int GO_MAIN=1000;
    private static final int CHECK_AGREE=1001;
    private static final int CHECK_AUTH=1002;
    private static final int Go_ERROR=1003;

    /**
     *
     * 获取所有数据
     */
    public static  void getAllUser(final Handler handler)
    {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereNotEqualTo("username","");
        query.setLimit(500);
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(7));
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

}
