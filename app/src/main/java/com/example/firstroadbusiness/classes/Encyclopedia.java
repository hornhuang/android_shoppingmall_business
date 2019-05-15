package com.example.firstroadbusiness.classes;

import android.graphics.Bitmap;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Encyclopedia extends BmobObject {

    private BmobFile bmobFiles[];
    private String introduces[];
    private Bitmap bitmaps[];
    private Bitmap userbitmap;
    private int awesomes;
    private List<String> commentsId;

    private User linkUser;
    private String mWriterId;//用于识别LostItem对象的Id,在构造方法中已经初始化

    public Encyclopedia(){

    }

    public BmobFile[] getBmobFiles() {
        return bmobFiles;
    }

    public void setBmobFiles(BmobFile[] bmobFiles) {
        this.bmobFiles = bmobFiles;
    }

    public String[] getIntroduces() {
        return introduces;
    }

    public void setIntroduces(String[] introduces) {
        this.introduces = introduces;
    }

    public User getLinkUser() {
        return linkUser;
    }

    public void setLinkUser(User linkUser) {
        this.linkUser = linkUser;
    }

    public String getmWriterId() {
        return mWriterId;
    }

    public void setmWriterId(String mWriterId) {
        this.mWriterId = mWriterId;
    }

    public Bitmap[] getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(Bitmap[] bitmaps) {
        this.bitmaps = bitmaps;
    }

    public int getAwesomes() {
        return awesomes;
    }

    public void setAwesomes(int awesomes) {
        this.awesomes = awesomes;
    }

    public Bitmap getUserbitmap() {
        return userbitmap;
    }

    public void setUserbitmap(Bitmap userbitmap) {
        this.userbitmap = userbitmap;
    }

    public List<String> getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(List<String> commentsId) {
        this.commentsId = commentsId;
    }
}
