package com.example.firstroadbusiness.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.firstroadbusiness.R;
import com.example.firstroadbusiness.bmobmanager.SuperImagesLoader;
import com.example.firstroadbusiness.classes.Encyclopedia;
import com.example.firstroadbusiness.classes.Goods;
import com.example.firstroadbusiness.fragments.encyclopediaadapter.EncyclopediaAdapter;
import com.example.firstroadbusiness.fragments.goodsadapter.GoodsActivity;
import com.example.firstroadbusiness.fragments.goodsadapter.GoodsAdapter;
import com.example.firstroadbusiness.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainGoodsFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GoodsAdapter adapter;
    private List<Goods> encyclopediaList;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    public MainGoodsFragment() {
        // Required empty public constructor
    }

    public static MainGoodsFragment newInstance(String param1, String param2) {
        MainGoodsFragment fragment = new MainGoodsFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_goods, container, false);

        iniViews(view);
        iniRecyclerView();
        iniSwipeReflesh();

        return view;
    }

    private void iniViews(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refleshlayout);
    }

    public void iniRecyclerView(){
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);
        encyclopediaList = new ArrayList<>();
        adapter = new GoodsAdapter(getData(), getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void iniSwipeReflesh(){
        swipeRefreshLayout.setProgressViewOffset(false, 200, 400);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
    }

    /*
    从 Bmob 获得所有用户信息
     */
    public List<Goods> getData(){
        BmobQuery<Goods> query = new BmobQuery<>();
        query.setLimit(8).setSkip(0).order("-createdAt")
                .findObjects(new FindListener<Goods>() {
                    @Override
                    public void done(List<Goods> object, BmobException e) {
                        if (e == null) {
                            encyclopediaList.clear();
                            encyclopediaList.addAll(object);
                            adapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            MyToast.MyToast((AppCompatActivity) getActivity(), "失败，请检查网络--" + e.getMessage());
                        }
                    }
                });
        return encyclopediaList;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.truth_goods:
//                startActivity(new Intent(getActivity(), GoodsActivity.class));
//                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
