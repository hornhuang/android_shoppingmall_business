package com.example.firstroadbusiness.fragments.routesadapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firstroadbusiness.R;
import com.example.firstroadbusiness.classes.Encyclopedia;
import com.example.firstroadbusiness.classes.User;
import com.example.firstroadbusiness.fragments.encyclopediaadapter.EncyclopediaAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainMineFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EncyclopediaAdapter adapter;
    private List<Encyclopedia> encyclopediaList;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    public MainMineFragment() {
        // Required empty public constructor
    }

    public static MainMineFragment newInstance(String param1, String param2) {
        MainMineFragment fragment = new MainMineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_mine, container, false);

        iniViews(view);
        iniRecyclerView();
        iniSwipeReflesh();
        return view;
    }

    private void iniViews(View view){
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }

    private void iniRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        encyclopediaList = new ArrayList<>();
        adapter = new EncyclopediaAdapter(queryPostAuthor(), getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void iniSwipeReflesh(){
        swipeRefreshLayout.setProgressViewOffset(false, 200, 400);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                queryPostAuthor();
            }
        });
    }

    // 查询一对一关联，查询当前用户发表的所有帖子
    private List<Encyclopedia> queryPostAuthor() {
        if (BmobUser.isLogin()) {
            BmobQuery<Encyclopedia> query = new BmobQuery<>();
            query.addWhereEqualTo("linkUser", BmobUser.getCurrentUser(User.class));
            query.order("-updatedAt");
            //包含作者信息
            query.include("linkUser");
            query.findObjects(new FindListener<Encyclopedia>() {

                @Override
                public void done(List<Encyclopedia> object, BmobException e) {
                    if (e == null) {
                        encyclopediaList.clear();
                        encyclopediaList.addAll(object);
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    } else {

                    }
                }

            });
        } else {

        }
        return encyclopediaList;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
