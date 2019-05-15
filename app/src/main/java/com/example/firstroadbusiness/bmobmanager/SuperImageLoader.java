package com.example.firstroadbusiness.bmobmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.example.firstroadbusiness.classes.Encyclopedia;
import com.example.firstroadbusiness.classes.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuperImageLoader {

    private ImageView imageView;
    private CircleImageView circleImageView;

    private User user;
    private Encyclopedia encyclopedia;

    private Bitmap bitmap;
    private int flag;// 用于匹配构造方法

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                imageView.setImageBitmap(user.getUserIcon());
            }else if (msg.what == 1){
                circleImageView.setImageBitmap(user.getUserIcon());
            }else if (msg.what == 2){
                imageView.setImageBitmap(encyclopedia.getBitmaps()[0]);
            }else if (msg.what == 3){
                circleImageView.setImageBitmap(encyclopedia.getBitmaps()[0]);
            }
        }
    };

    public SuperImageLoader(ImageView imageView, User user){
        this.imageView = imageView;
        this.user = user;
        this.flag = 0;
    }

    public SuperImageLoader(CircleImageView imageView, User user){
        this.circleImageView = imageView;
        this.user = user;
        this.flag = 1;
    }

    public SuperImageLoader(ImageView imageView, Encyclopedia encyclopedia){
        this.imageView = imageView;
        this.encyclopedia = encyclopedia;
        this.flag = 2;
    }

    public SuperImageLoader(CircleImageView imageView, Encyclopedia encyclopedia){
        this.circleImageView = imageView;
        this.encyclopedia = encyclopedia;
        this.flag = 3;
    }

    public void articalLoad(){
        new Thread(){
            @Override
            public void run() {
                if (encyclopedia.getBmobFiles()[0] != null){
                    encyclopedia.getBitmaps()[0] = (getPicture(encyclopedia.getBmobFiles()[0].getUrl()));
                }
                Message message = handler.obtainMessage();
                message.what = flag;
                handler.sendMessage(message);
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void userLoad(){
        new Thread(){
            @Override
            public void run() {
                if (user.getImageFile() != null){
                    user.setUserIcon(getPicture(user.getImageFile().getUrl()));
                }
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = handler.obtainMessage();
                message.what = flag;
                handler.sendMessage(message);
            }
        }.start();
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
