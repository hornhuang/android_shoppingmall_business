package com.example.firstroadbusiness.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.firstroadbusiness.R;
import com.example.firstroadbusiness.classes.Encyclopedia;
import com.example.firstroadbusiness.fragments.MainGoodsFragment;
import com.example.firstroadbusiness.fragments.MainHomeFragment;
import com.example.firstroadbusiness.fragments.MainRoutesFragment;
import com.example.firstroadbusiness.fragments.MaincyClopediaFragment;
import com.example.firstroadbusiness.fragments.encyclopediaadapter.EncyclopediaAdapter;
import com.example.firstroadbusiness.fragments.routesadapter.MainMineFragment;
import com.example.firstroadbusiness.pushactivities.EncyclopediaPublishActivity;
import com.example.firstroadbusiness.pushactivities.GoodsPublishActivity;
import com.example.firstroadbusiness.pushactivities.RoutesPublishActivity;
import com.example.firstroadbusiness.utils.MyToast;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends AppCompatActivity {

    private List<Encyclopedia> articalList;
    private EncyclopediaAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter = new EncyclopediaAdapter(articalList, MainActivity.this);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener(){

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.encycpedia:
                    replaceFragment(new MaincyClopediaFragment());
                    break;

                case R.id.goods:
                    replaceFragment(new MainGoodsFragment());
                    break;

                case R.id.main:
                    replaceFragment(new MainHomeFragment());
                    break;

                case R.id.routes:
                    replaceFragment(new MainRoutesFragment());
                    break;

                case R.id.mine:
                    replaceFragment(new MainMineFragment());
                    break;

                default:

                    break;
            }
            return true;
        }
    };

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);
        bottomNavigationView.setSelectedItemId(R.id.goods);
        iniFloatButton();
    }

    // 设置三个悬浮按钮的监听事件
    private void iniFloatButton(){
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
