package com.example.firstroadbusiness.fragments.routesadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.firstroadbusiness.R;
import com.example.firstroadbusiness.bmobmanager.FinalImageLoader;
import com.example.firstroadbusiness.classes.Comment;
import com.example.firstroadbusiness.classes.Encyclopedia;
import com.example.firstroadbusiness.classes.Routes;
import com.example.firstroadbusiness.classes.User;
import com.example.firstroadbusiness.fragments.encyclopediaadapter.ColopediaAdapter;
import com.example.firstroadbusiness.utils.MyToast;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RoutesDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String objectId;

    private List<String> titles = new ArrayList<>();
    private List<Comment> commentList;
    private ColopediaAdapter adapter;
    private Routes routes;
    private List<Bitmap> images;

    private Banner banner;
    private TextView[] textViews;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private EditText editText;
    private Button push;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Banner banner = (Banner) findViewById(R.id.banner);
            // banner.setImages(images).setImageLoader(new GlideImageLoader());
            banner.setImages(images).setImageLoader(new MyImageLoader());

            // 设置banner样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
            //设置图片加载器
            // banner.setImageLoader(new GlideImageLoader());
            banner.setImageLoader(new MyImageLoader());
            // 设置图片集合
            banner.setImages(images);
            // 设置banner动画效果
            banner.setBannerAnimation(Transformer.DepthPage);
            // 设置标题集合（当banner样式有显示title时）
            banner.setBannerTitles(titles);
            // 设置自动轮播，默认为true
            banner.isAutoPlay(true);
            // 设置轮播时间
            banner.setDelayTime(1500);
            // 设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER);
            // banner设置方法全部调用完毕时最后调用
            banner.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_detail);

        objectId = getIntent().getStringExtra("objectId");

        iniViews();
        iniRoutes();
    }

    private void iniViews(){
        textViews = new TextView[]{
                findViewById(R.id.scenic_name),
                findViewById(R.id.label_1),
                findViewById(R.id.label_2),
                findViewById(R.id.scenic_introduce),
                findViewById(R.id.routes_1),
                findViewById(R.id.routes_2)
        };
        banner = findViewById(R.id.banner);
        imageView = findViewById(R.id.imageView);
        editText = findViewById(R.id.edit);
        push = findViewById(R.id.publish);
        recyclerView = findViewById(R.id.recyclerview);

        push.setOnClickListener(this);
    }

    private void iniRoutes(){
        BmobQuery<Routes> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<Routes>() {
            @Override
            public void done(Routes object, BmobException e) {
                if(e==null){
                    routes = object;
                    for (int j = 4 ; j < textViews.length ; j++){
                        textViews[j].setText(object.getIntroduces()[j-4]);
                    }
                    object.setLinkUser(findWritter(object));
                    new FinalImageLoader(imageView, routes.getBmobFiles()[routes.getBmobFiles().length-1]).imageLoad();
                    iniRecyclerView(object);
                    iniBanner();
                }else{
                    MyToast.MyToast(RoutesDetailActivity.this,"Routes查询失败：" + e.getMessage());
                }
            }
        });
    }

    //peishi
    private User findWritter(Routes routes){
        final User[] user = new User[1];
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(routes.getmWriterId(), new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if(e==null){
                    // 配适作者信息
//                    new FinalImageLoader(mUserTopImage, object.getImageFile()).imageLoad();
//                    mUserTopName.setText(object.getNickname());
//                    mUserTopMotto.setText(object.getMotto());
//                    new FinalImageLoader(mUserBottomImage, object.getImageFile()).imageLoad();
//                    mUserBottomMotto.setText(object.getMotto());
                    user[0] = object;
                }else{
                    MyToast.MyToast(RoutesDetailActivity.this,"Writter 查询失败：" + e.getMessage());
                }
            }
        });
        return user[0];
    }

    private void iniRecyclerView(Routes routes){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        commentList =  new ArrayList<>();
        adapter = new ColopediaAdapter(getComments(routes));
        recyclerView.setAdapter(adapter);
    }

    // 查询所有评论
    private List<Comment> getComments(Routes routes){
        for (int i = 0 ; i < routes.getCommentsId().size() ; i++){
            //查找Person表里面id为6b6c11c537的数据
            BmobQuery<Comment> bmobQuery = new BmobQuery<Comment>();
            bmobQuery.getObject(routes.getCommentsId().get(i), new QueryListener<Comment>() {
                @Override
                public void done(Comment object,BmobException e) {
                    if(e==null){
                        final Comment comment = object;
                        BmobQuery<User> bmobQuery = new BmobQuery<>();
                        bmobQuery.getObject(object.getAuthorId(), new QueryListener<User>() {
                            @Override
                            public void done(User object, BmobException e) {
                                if(e==null){
                                    // 配适作者信息
                                    comment.setWriter(object);
                                    commentList.add(comment);
                                    adapter.notifyDataSetChanged();
                                }else{
                                    MyToast.MyToast(RoutesDetailActivity.this,"查询失败 comment - user ：" + e.getMessage());
                                }
                            }
                        });
                    }else{
                        MyToast.MyToast(RoutesDetailActivity.this,"查询失败 comment ：" + e.getMessage());
                    }
                }
            });
        }
        return commentList;
    }

    // 发布评论
    private void pushComment(){
        if (BmobUser.isLogin()){
            final Comment conmment = new Comment();
            conmment.setWriter(BmobUser.getCurrentUser(User.class));
            conmment.setAuthorId(BmobUser.getCurrentUser(User.class).getObjectId());
            conmment.setCommentContent(editText.getText().toString());
            conmment.save(new SaveListener<String>() {
                @Override
                public void done(String objectId,BmobException e) {
                    if(e==null){
                        refleshEncyclopedia(conmment);
                    }else{
                        MyToast.MyToast(RoutesDetailActivity.this,"创建数据失败：" + e.getMessage());
                    }
                }
            });
        }
    }

    private void refleshEncyclopedia(final Comment conmment){
        routes.getCommentsId().add(conmment.getObjectId());
        routes.update(routes.getObjectId(), new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    MyToast.MyToast(RoutesDetailActivity.this,"发布成功");
                    editText.setText("");
                    //查找Person表里面id为6b6c11c537的数据
                    BmobQuery<User> bmobQuery = new BmobQuery<User>();
                    bmobQuery.getObject(BmobUser.getCurrentUser(User.class).getObjectId(), new QueryListener<User>() {
                        @Override
                        public void done(User object,BmobException e) {
                            if(e==null){
                                conmment.setWriter(object);
                                commentList.add(conmment);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }else{
                    MyToast.MyToast(RoutesDetailActivity.this,"发布失败：" + e.getMessage());
                }
            }

        });
    }

    private void iniBanner(){
        images = new ArrayList<>();
        for (int i = 0 ; i < 4 ; i++){
            final int j = i;
            new Thread(){
                @Override
                public void run() {
                    super.run();
//                    Log.d("123123", j+"-- " + routes.getBmobFiles().length);
                    images.add(getPicture(routes.getBmobFiles()[j].getUrl()));
                }
            }.start();
        }
        titles.add(routes.getIntroduces()[0]);
        titles.add(routes.getIntroduces()[1]);
        titles.add(routes.getIntroduces()[2]);
        titles.add(routes.getIntroduces()[3]);
        for (int i = 0 ; i < 4 ; i++){
            //images.add("http://47.107.132.227/form");
        }

        new Thread(){
            @Override
            public void run() {
                super.run();
                while (images.size()<4){

                }
                Message message = new Message();
                handler.sendMessage(message);
            }
        }.start();
    }

    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
        }
    }

    public Bitmap getPicture(String path){
        Bitmap bm = null;
        try{
            URL url = new URL(path);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bm = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return bm;
    }

    public static void actionStart(AppCompatActivity activity, String objectId){
        Intent intent = new Intent(activity, RoutesDetailActivity.class);
        intent.putExtra("objectId", objectId);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish:
                pushComment();
                break;
        }
    }
}
