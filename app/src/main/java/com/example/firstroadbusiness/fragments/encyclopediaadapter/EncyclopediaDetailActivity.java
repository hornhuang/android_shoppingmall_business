package com.example.firstroadbusiness.fragments.encyclopediaadapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.firstroadbusiness.R;
import com.example.firstroadbusiness.bmobmanager.FinalImageLoader;
import com.example.firstroadbusiness.classes.Comment;
import com.example.firstroadbusiness.classes.Encyclopedia;
import com.example.firstroadbusiness.classes.User;
import com.example.firstroadbusiness.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class EncyclopediaDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private String objectId ;

    private ScrollView scrollView;
    private RecyclerView recyclerView;
    private List<Comment> commentList;
    private TextView commentSubmit;
    private ColopediaAdapter adapter;
    private EditText editText;

    private ImageView images[] ;
    private ImageView userImage ;
    private TextView textViews[] ;

    private CircleImageView mUserTopImage;
    private TextView mUserTopName, mUserTopMotto;

    private CircleImageView mUserBottomImage;
    private TextView  mUserBottomMotto;

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
        scrollView = findViewById(R.id.scrollView);
        recyclerView = (RecyclerView) findViewById(R.id.colopedia_recycler);
        commentSubmit = (TextView) findViewById(R.id.comment_submit);
        editText = (EditText) findViewById(R.id.colopedia_comment_content);

        commentSubmit.setOnClickListener(this);

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

    private void iniRecyclerView(Encyclopedia encyclopedia){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        commentList =  new ArrayList<>();
        adapter = new ColopediaAdapter(getComments(encyclopedia));
        recyclerView.setAdapter(adapter);
    }

    private void iniEncyclopedia(){
        BmobQuery<Encyclopedia> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<Encyclopedia>() {
            @Override
            public void done(Encyclopedia object, BmobException e) {
                if(e==null){
                    encyclopedia = object;
                    object.setLinkUser(findWritter(object));
                    int i = 0;
                    for (BmobFile bmobFile : object.getBmobFiles()){
                        new FinalImageLoader(images[i++], bmobFile).imageLoad();
                    }
                    for (int j = 0 ; j < textViews.length ; j++){
                        textViews[j].setText(object.getIntroduces()[j]);
                    }
                    iniRecyclerView(object);
                }else{
                    MyToast.MyToast(EncyclopediaDetailActivity.this,"查询失败：" + e.getMessage());
                }
            }
        });
    }

    // 查询所有评论
    private List<Comment> getComments(Encyclopedia encyclopedia){
        for (int i = 0 ; i < encyclopedia.getCommentsId().size() ; i++){
            //查找Person表里面id为6b6c11c537的数据
            BmobQuery<Comment> bmobQuery = new BmobQuery<Comment>();
            bmobQuery.getObject(encyclopedia.getCommentsId().get(i), new QueryListener<Comment>() {
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
                                    MyToast.MyToast(EncyclopediaDetailActivity.this,"查询失败 comment - user ：" + e.getMessage());
                                }
                            }
                        });
                    }else{
                        MyToast.MyToast(EncyclopediaDetailActivity.this,"查询失败 comment ：" + e.getMessage());
                    }
                }
            });
        }
        return commentList;
    }

    //peishi
    private User findWritter(Encyclopedia encyclopedia){
        final User[] user = new User[1];
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(encyclopedia.getmWriterId(), new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if(e==null){
                    // 配适作者信息
                    new FinalImageLoader(mUserTopImage, object.getImageFile()).imageLoad();
                    mUserTopName.setText(object.getNickname());
                    mUserTopMotto.setText(object.getMotto());
                    new FinalImageLoader(mUserBottomImage, object.getImageFile()).imageLoad();
                    mUserBottomMotto.setText(object.getMotto());
                    user[0] = object;
                }else{
                    MyToast.MyToast(EncyclopediaDetailActivity.this,"查询失败：" + e.getMessage());
                }
            }
        });
        return user[0];
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
                        MyToast.MyToast(EncyclopediaDetailActivity.this,"创建数据失败：" + e.getMessage());
                    }
                }
            });
        }
    }

    private void refleshEncyclopedia(final Comment conmment){
        encyclopedia.getCommentsId().add(objectId);
        encyclopedia.update(encyclopedia.getObjectId(), new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    MyToast.MyToast(EncyclopediaDetailActivity.this,"发布成功");
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
                    MyToast.MyToast(EncyclopediaDetailActivity.this,"发布失败：" + e.getMessage());
                }
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comment_submit:
                // 提交评论
                pushComment();
                // 修改文章评论表
                break;

            default:

                break;
        }

    }

    public static void anctionStart(AppCompatActivity activity, String objectId){
        Intent intent = new Intent(activity, EncyclopediaDetailActivity.class);
        intent.putExtra("objectId", objectId);
        activity.startActivity(intent);
    }

}
