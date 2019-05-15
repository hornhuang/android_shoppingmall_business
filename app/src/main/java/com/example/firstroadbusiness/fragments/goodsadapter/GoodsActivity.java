package com.example.firstroadbusiness.fragments.goodsadapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firstroadbusiness.R;
import com.example.firstroadbusiness.bmobmanager.FinalImageLoader;
import com.example.firstroadbusiness.classes.Encyclopedia;
import com.example.firstroadbusiness.classes.Goods;
import com.example.firstroadbusiness.classes.User;
import com.example.firstroadbusiness.fragments.encyclopediaadapter.EncyclopediaDetailActivity;
import com.example.firstroadbusiness.utils.MyToast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class GoodsActivity extends AppCompatActivity {

    private String objectId ;
    private Goods goods;

    private ImageView imageViews[];
    private CircleImageView merchantsImage;

    private TextView goodsName, goodsIntroduce, goodsPrice, smerchantsName, goodsHistoryIntroduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        objectId = getIntent().getStringExtra("objectId");

        iniViews();
        iniGoods();
    }

    private void iniViews(){
        imageViews = new ImageView[]{
                findViewById(R.id.image_1),
                findViewById(R.id.image_2)
        };
        merchantsImage = findViewById(R.id.merchants_headicon);
        goodsName = findViewById(R.id.goods_title);
        goodsIntroduce = findViewById(R.id.goods_introduce);
        goodsPrice = findViewById(R.id.goods_price);
        smerchantsName = findViewById(R.id.merchants_name);
        goodsHistoryIntroduce = findViewById(R.id.goods_history_introduce);
    }

    private void iniGoods(){
        BmobQuery<Goods> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<Goods>() {
            @Override
            public void done(Goods object, BmobException e) {
                if(e==null){
                    object.setMerchants(findMerchants(object));
                    int i = 0;
                    for (BmobFile bmobFile : object.getBmobFile()){
                        new FinalImageLoader(imageViews[i++], bmobFile).imageLoad();
                    }
                    goodsName.setText(object.getTitle());
                    goodsIntroduce.setText(object.getIntroduces()[0]);
                    goodsPrice.setText(object.getPrice());
                    goodsHistoryIntroduce.setText(object.getIntroduces()[1]);
                    goods = object;
                }else{
                    MyToast.MyToast(GoodsActivity.this,"goods 查询失败：" + e.getMessage());
                }
            }
        });
    }

    //peishi
    private User findMerchants(Goods goods){
        final User[] user = new User[1];
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(goods.getMerchantsId(), new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if(e==null){
                    // 配适作者信息
                    new FinalImageLoader(merchantsImage, object.getImageFile()).imageLoad();
                    smerchantsName.setText(object.getNickname());
                    user[0] = object;
                }else{
                    MyToast.MyToast(GoodsActivity.this,"查询失败：" + e.getMessage());
                }
            }
        });
        return user[0];
    }

    public static void anctionStart(AppCompatActivity activity, String objectId){
        Intent intent = new Intent(activity, GoodsActivity.class);
        intent.putExtra("objectId", objectId);
        activity.startActivity(intent);
    }

}
