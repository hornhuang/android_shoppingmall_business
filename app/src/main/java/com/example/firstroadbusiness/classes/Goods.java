package com.example.firstroadbusiness.classes;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Goods extends BmobObject {

    private BmobFile bmobFile[];
    private String title;
    private String introduces[];
    private String price;

    private User merchants;
    private String merchantsId;

    public Goods(){

    }

    public BmobFile[] getBmobFile() {
        return bmobFile;
    }

    public void setBmobFile(BmobFile[] bmobFile) {
        this.bmobFile = bmobFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getIntroduces() {
        return introduces;
    }

    public void setIntroduces(String[] introduces) {
        this.introduces = introduces;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public User getMerchants() {
        return merchants;
    }

    public void setMerchants(User merchants) {
        this.merchants = merchants;
    }

    public String getMerchantsId() {
        return merchantsId;
    }

    public void setMerchantsId(String merchantsId) {
        this.merchantsId = merchantsId;
    }
}
