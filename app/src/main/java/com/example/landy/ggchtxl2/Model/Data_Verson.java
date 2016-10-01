package com.example.landy.ggchtxl2.Model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Landy on 2016/9/9.
 */
public class Data_Verson extends BmobObject implements Serializable {
    private int Version;
    private BmobFile Theme;

    public BmobFile getTheme() {
        return Theme;
    }

    public void setTheme(BmobFile theme) {
        Theme = theme;
    }

    public int getVersion() {
        return Version;
    }

    public void setVersion(int Version) {
        Version = Version;
    }
}
