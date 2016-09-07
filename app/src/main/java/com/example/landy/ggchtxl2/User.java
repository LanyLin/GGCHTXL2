package com.example.landy.ggchtxl2;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Landy on 2016/9/5.
 */
public class User extends BmobUser implements Serializable{
    //private String mobilePhoneNumber;
    private String Dormitory;
    private String Academy;
    private BmobFile Pic;
    private String Grade;
    private String shoutnum;

    public String getShoutnum() {
        return shoutnum;
    }

    public void setShoutnum(String shoutnum) {
        this.shoutnum = shoutnum;
    }

    public BmobFile getPic() {
        return Pic;
    }

    public void setPic(BmobFile pic) {
        Pic = pic;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public String getAcademy() {
        return Academy;
    }

    public String getDormitory() {
        return Dormitory;
    }

    public void setAcademy(String academy) {
        Academy = academy;
    }

    public void setDormitory(String dormitory) {
        Dormitory = dormitory;
    }



}
