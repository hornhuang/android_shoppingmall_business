package com.example.firstroadbusiness.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.firstroadbusiness.R;
import com.example.firstroadbusiness.pushactivities.EncyclopediaPublishActivity;
import com.example.firstroadbusiness.pushactivities.GoodsPublishActivity;
import com.example.firstroadbusiness.pushactivities.RoutesPublishActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniFloatButton();
    }

    private void iniFloatButton(){
        /*
        设置三个悬浮按钮的监听事件
         */
        //
        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EncyclopediaPublishActivity.anctionStart(MainActivity.this);
            }
        });
        //跳转到 FromPointToPoint 活动
        final FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsPublishActivity.anctionStart(MainActivity.this);
            }
        });
        //弹出提示
        final FloatingActionButton actionC = (FloatingActionButton) findViewById(R.id.action_c);
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoutesPublishActivity.anctionStart(MainActivity.this);
            }
        });
    }

    public static void anctionStart(AppCompatActivity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}
