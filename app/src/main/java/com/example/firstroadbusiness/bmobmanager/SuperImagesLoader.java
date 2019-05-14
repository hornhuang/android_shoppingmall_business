package com.example.firstroadbusiness.bmobmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.firstroadbusiness.classes.Encyclopedia;
import com.example.firstroadbusiness.classes.User;
import com.example.firstroadbusiness.fragments.adapters.EncyclopediaAdapter;
import com.example.firstroadbusiness.fragments.adapters.UserAdapter;
import com.example.firstroadbusiness.utils.MyToast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class SuperImagesLoader {

    private EncyclopediaAdapter articalAdapter;
    private List<Encyclopedia> articalList;

    private UserAdapter summaryRecyclerAdapter;
    private List<User> userList;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x0){
                articalAdapter.notifyDataSetChanged();
            }else if (msg.what == 020){
                summaryRecyclerAdapter.notifyDataSetChanged();
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    public SuperImagesLoader(EncyclopediaAdapter adapter, List<Encyclopedia> encyclopedias, SwipeRefreshLayout swipeRefreshLayout){// size 只用于区分 重载
        this.articalList = encyclopedias;
        this.articalAdapter = adapter;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public SuperImagesLoader(UserAdapter adapter, List<User> users, SwipeRefreshLayout swipeRefreshLayout){
        this.userList = users;
        this.summaryRecyclerAdapter = adapter;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }


    public void encyclopediaLoad(){
        for (int i = 0 ; i < articalList.size() ; i++) {
            final int flag = i;
            new Thread(){
                @Override
                public void run() {
                    //查找Person表里面id为6b6c11c537的数据
                    final Encyclopedia encyclopedia = articalList.get(flag);
                    BmobQuery<User> bmobQuery = new BmobQuery<User>();
                    bmobQuery.getObject(encyclopedia.getmWriterId(), new QueryListener<User>() {
                        @Override
                        public void done(final User object, BmobException e) {
                            if(e==null){
                                new Thread(){
                                    @Override
                                    public void run() {
                                        super.run();
                                        if (object.getImageFile() != null){
                                            object.setUserIcon(getPicture(object.getImageFile().getUrl()));
                                        }
                                    }
                                }.start();
                                encyclopedia.setLinkUser(object);
                            }else{

                            }
                        }
                    });
                    if (encyclopedia.getBmobFiles()[0] != null){
                        encyclopedia.getBitmaps()[0] = (getPicture(encyclopedia.getBmobFiles()[0].getUrl()));
                    }
                    Message message = handler.obtainMessage();
                    message.what = 0x0;
                    handler.sendMessage(message);
                }
            }.start();
        }
    }

    public void userLoad(){
        for (int i = 0 ; i < userList.size() ; i++) {
            final int flag = i;
            new Thread(){
                @Override
                public void run() {
                    User user = userList.get(flag);
                    if (user.getImageFile() != null){
                        user.setUserIcon(getPicture(user.getImageFile().getUrl()));
                    }
                    Message message = handler.obtainMessage();
                    message.what = 020;
                    handler.sendMessage(message);
                }
            }.start();
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

}
