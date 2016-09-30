package com.example.landy.ggchtxl2.Model;

/**
 * Created by Landy on 2016/9/25.
 */

public class UserExist {
    int id;
    private String create_at;
    private String Username;
    private int Exist;

    public int getId() {
        return id;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getExist() {
        return Exist;
    }

    public void setExist(int exist) {
        Exist = exist;
    }
}
