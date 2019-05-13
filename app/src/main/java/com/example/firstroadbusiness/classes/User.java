package com.example.firstroadbusiness.classes;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {

    private String flag;// 0：普通用户 1：商家

    public User(){

    }


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
