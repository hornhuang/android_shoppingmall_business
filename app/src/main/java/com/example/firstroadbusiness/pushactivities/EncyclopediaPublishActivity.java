package com.example.firstroadbusiness.pushactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firstroadbusiness.R;
import com.example.firstroadbusiness.activities.MainActivity;

public class EncyclopediaPublishActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViews[];
    private TextView textViews[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyclopedia_publish);

        iniViews();
    }

    private void iniViews(){
        imageViews = new ImageView[]{
                (ImageView) findViewById(R.id.image_1),
                (ImageView) findViewById(R.id.image_2),
                (ImageView) findViewById(R.id.image_3),
                (ImageView) findViewById(R.id.image_4),
                (ImageView) findViewById(R.id.image_5),
                (ImageView) findViewById(R.id.image_6)
        };
        textViews = new TextView[]{
                (TextView) findViewById(R.id.text_1_title),
                (TextView) findViewById(R.id.text_2),
                (TextView) findViewById(R.id.text_3),
                (TextView) findViewById(R.id.text_4),
                (TextView) findViewById(R.id.text_5),
        };
    }

    public static void anctionStart(AppCompatActivity activity){
        Intent intent = new Intent(activity, EncyclopediaPublishActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
