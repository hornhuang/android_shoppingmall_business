package com.example.firstroadbusiness.classes;

import android.graphics.Bitmap;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Routes extends BmobObject {

    private BmobFile bmobFiles[];
    private String introduces[];
    private Bitmap bitmaps[];
    private List<String> commentsId;

    private User linkUser;
    private String mWriterId;//用于识别LostItem对象的Id,在构造方法中已经初始化

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

    public Bitmap[] getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(Bitmap[] bitmaps) {
        this.bitmaps = bitmaps;
    }

    public List<String> getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(List<String> commentsId) {
        this.commentsId = commentsId;
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
}
