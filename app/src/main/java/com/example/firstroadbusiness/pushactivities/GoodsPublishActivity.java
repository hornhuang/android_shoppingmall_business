package com.example.firstroadbusiness.pushactivities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firstroadbusiness.R;
import com.example.firstroadbusiness.activities.MainActivity;
import com.example.firstroadbusiness.classes.Goods;
import com.example.firstroadbusiness.classes.User;
import com.example.firstroadbusiness.fragments.goodsadapter.GoodsActivity;
import com.example.firstroadbusiness.utils.MyLog;
import com.example.firstroadbusiness.utils.MyToast;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

import static com.example.firstroadbusiness.pushactivities.EncyclopediaPublishActivity.MAX_PROGRESS;

public class GoodsPublishActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView[] imageViews;
    private EditText editName, editIntroduce, editPrice, editHistory;
    private Button upLoad;

    private int imageFlag;
    private String path[] = new String[2];
    private Goods goods;

    final static int MAX_PROGRESS = 100;
    //虚拟 填充长度为100的数组
    private int[] data = new int[50];
    //记录进度对话框完成百分比
    int progressStatus = 0;
    int hasDta = 0;
    private ProgressDialog progressDialog02;

    //创建一个负责更新进度的Handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //表明消息是本程序发送的
            if (msg.what == 0x111){
                progressDialog02.setProgress(progressStatus);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_publish);

        iniViews();
    }

    private void iniViews(){
        imageViews = new ImageView[]{
                findViewById(R.id.goods_image_1),
                findViewById(R.id.goods_image_2)
        };

        editName = findViewById(R.id.goods_name);
        editIntroduce = findViewById(R.id.goods_introduce);
        editPrice = findViewById(R.id.goods_price);
        editHistory = findViewById(R.id.goods_history_introduce);
        upLoad = findViewById(R.id.push);

        upLoad.setOnClickListener(this);
        imageViews[0].setOnClickListener(this);
        imageViews[1].setOnClickListener(this);
    }

    private void pushGoods(){
        goods = new Goods();
        goods.setMerchants(BmobUser.getCurrentUser(User.class));
        goods.setMerchantsId(BmobUser.getCurrentUser(User.class).getObjectId());
        goods.setTitle(editName.getText().toString());
        goods.setIntroduces(new String[2]);
        goods.getIntroduces()[0] = (editIntroduce.getText().toString());
        goods.getIntroduces()[1] = (editHistory.getText().toString());
        goods.setPrice(editPrice.getText().toString());
        goods.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    upLoadPicture();
                }
            }
        });
    }

    public static void anctionStart(AppCompatActivity activity){
        Intent intent = new Intent(activity, GoodsPublishActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.push:showProgress(GoodsPublishActivity.this.getWindow().getDecorView());
                pushGoods();
                break;

            case R.id.goods_image_1:
                imageFlag = 0;
                loadImage();
                break;

            case R.id.goods_image_2:
                imageFlag = 1;
                loadImage();
                break;


            default:

                break;
        }
    }

    private void loadImage(){
        if(ContextCompat.checkSelfPermission(GoodsPublishActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(GoodsPublishActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },2);
        }
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2://这里的requestCode是我自己设置的，就是确定返回到那个Activity的标志
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        path[imageFlag] = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 1;
                        Bitmap bitmap = BitmapFactory.decodeFile(path[imageFlag],options);
                        bitmap=Bitmap.createScaledBitmap(bitmap, 700, 400, false);
                        imageViews[imageFlag].setImageBitmap(bitmap);
                        Toast.makeText(GoodsPublishActivity.this,path[imageFlag],Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:

                break;
        }
    }

    private void upLoadPicture(){

        BmobFile.uploadBatch(path, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if(urls.size()==path.length){//如果数量相等，则代表文件全部上传完成
                    BmobFile bmobFiles[] = new BmobFile[imageViews.length];
                    for (int i = 0 ; i < files.size() ; i++){
                        bmobFiles[i] = files.get(i);
                    }
                    goods.setBmobFile(bmobFiles);
                    goods.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                MyToast.MyToast(GoodsPublishActivity.this, "发布成功");
                                finish();
                            } else {
                                MyToast.MyToast(GoodsPublishActivity.this, "发布失败" + e.getMessage());
                            }
                        }
                    });
                }

            }

            @Override
            public void onError(int statuscode, String errormsg) {
                MyToast.MyToast(GoodsPublishActivity.this, "错误码"+statuscode +",错误描述："+errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
                progressStatus = totalPercent;
            }
        });
    }

    public void showProgress(View source){
        //将进度条完成重设为0
        progressStatus = 0;
        //重新开始填充数组
        hasDta = 0;
        progressDialog02 = new ProgressDialog(GoodsPublishActivity.this);
        progressDialog02.setMax(MAX_PROGRESS);
        //设置对话框标题
        progressDialog02.setTitle("任务正在执行中");
        //设置对话框执行内容
        progressDialog02.setMessage("任务正在执行中敬请等待~~~");
        //设置对话框“取消” 按钮关闭
        progressDialog02.setCancelable(false);
        //设置对话框进度条风格
        progressDialog02.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //设置进度条是否显示进度
        progressDialog02.setIndeterminate(false);
        progressDialog02.show();
        new Thread(){//模拟耗时操作
            @Override
            public void run() {
                while (progressStatus < MAX_PROGRESS){
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    //更新ProgressBar
                    mHandler.sendEmptyMessage(0x111);
                }
                //任务完成进度条关闭
                progressDialog02.dismiss();
            }
        }.start();
    }

}
