package com.example.firstroadbusiness.pushactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.firstroadbusiness.R;
import com.example.firstroadbusiness.activities.MainActivity;

public class RoutesPublishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_publish);
    }

    public static void anctionStart(AppCompatActivity activity){
        Intent intent = new Intent(activity, RoutesPublishActivity.class);
        activity.startActivity(intent);
    }
}
