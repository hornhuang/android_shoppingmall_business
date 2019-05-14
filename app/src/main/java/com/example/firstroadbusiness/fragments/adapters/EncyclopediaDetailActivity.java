package com.example.firstroadbusiness.fragments.adapters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firstroadbusiness.R;
import com.example.firstroadbusiness.bmobmanager.FinalImageLoader;
import com.example.firstroadbusiness.classes.Encyclopedia;
import com.example.firstroadbusiness.utils.MyToast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class EncyclopediaDetailActivity extends AppCompatActivity {

    private String objectId ;

    private ImageView images[] ;
    private ImageView userImage ;
    private TextView textViews[] ;

    private ImageView mUserTopImage;
    private TextView mUserTopName, mUserTopMotto;

    private ImageView mUserBottomImage;
    private TextView mUserBottomName, mUserBottomMotto;

    private Encyclopedia encyclopedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyclopedia_detail);

        objectId = getIntent().getStringExtra("objectId");

        iniViews();
        iniEncyclopedia();
    }

    private void iniViews(){
        images = new ImageView[]{
                (ImageView) findViewById(R.id.image_1),
                (ImageView) findViewById(R.id.image_2),
                (ImageView) findViewById(R.id.image_3),
                (ImageView) findViewById(R.id.image_4),
                (ImageView) findViewById(R.id.image_5),
                (ImageView) findViewById(R.id.image_6)
        };

        textViews = new TextView[]{
                (TextView) findViewById(R.id.text_0),
                (TextView) findViewById(R.id.text_1),
                (TextView) findViewById(R.id.text_2),
                (TextView) findViewById(R.id.text_3),
                (TextView) findViewById(R.id.text_4)
        };

        mUserTopImage = findViewById(R.id.user_image_top);
        mUserTopName = findViewById(R.id.user_name_top);
        mUserTopMotto = findViewById(R.id.user_motto_top);

        mUserBottomImage = findViewById(R.id.user_image_bottom);
        mUserBottomMotto = findViewById(R.id.user_motto_bottom);
    }

    private void iniEncyclopedia(){
        //查找Person表里面id为6b6c11c537的数据
        BmobQuery<Encyclopedia> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<Encyclopedia>() {
            @Override
            public void done(Encyclopedia object, BmobException e) {
                if(e==null){
                    int i = 0;
                    for (BmobFile bmobFile : object.getBmobFiles()){
                        new FinalImageLoader(images[i++], bmobFile).imageLoad();
                    }
                    for (int j = 0 ; j < textViews.length ; i++){
                        textViews[j].setText(object.getIntroduces()[j]);
                    }
                }else{
                    MyToast.MyToast(EncyclopediaDetailActivity.this,"查询失败：" + e.getMessage());
                }
            }
        });
    }

    public static void anctionStart(AppCompatActivity activity, String objectId){
        Intent intent = new Intent(activity, EncyclopediaDetailActivity.class);
        intent.putExtra("objectId", objectId);
        activity.startActivity(intent);
    }

}
