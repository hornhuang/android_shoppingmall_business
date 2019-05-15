package com.example.firstroadbusiness.classes;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {

    private int imageId;          // 用户头像
    private String authorId;      // 用户名称
    private String commentContent;// 评论内容
    private int praiseNum;        // 点赞数目
    private boolean isPraised = false;//判断是否点赞

    private User writer;

    public Comment(){

    }

    // get()  &  set()
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public boolean isPraised() {
        return isPraised;
    }

    public void setPraised(boolean praised) {
        isPraised = praised;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }
}
