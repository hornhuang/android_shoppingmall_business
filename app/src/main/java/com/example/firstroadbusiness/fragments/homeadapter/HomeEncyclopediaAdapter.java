package com.example.firstroadbusiness.fragments.homeadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firstroadbusiness.R;
import com.example.firstroadbusiness.bmobmanager.FinalImageLoader;
import com.example.firstroadbusiness.classes.Encyclopedia;
import com.example.firstroadbusiness.fragments.encyclopediaadapter.EncyclopediaDetailActivity;

import java.util.List;

public class HomeEncyclopediaAdapter extends RecyclerView.Adapter<HomeEncyclopediaAdapter.ViewHolder> {

    private List<Encyclopedia> mList;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image_huge, image_small;
        private TextView text_title, text_introduce;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_huge = itemView.findViewById(R.id.image_1);
            image_small = itemView.findViewById(R.id.image_2);
            text_title = itemView.findViewById(R.id.encyclopedia_name);
            text_introduce = itemView.findViewById(R.id.encyclopedia_introduce);
        }
    }

    public HomeEncyclopediaAdapter(List<Encyclopedia> mList, Context context){
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.home_encyclopedia_list_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i){
        final Encyclopedia encyclopedia = mList.get(i);
        new FinalImageLoader(viewHolder.image_huge, encyclopedia.getBmobFiles()[0]).imageLoad();
        new FinalImageLoader(viewHolder.image_small, encyclopedia.getBmobFiles()[1]).imageLoad();
        viewHolder.text_title.setText(encyclopedia.getIntroduces()[0]);
        viewHolder.text_introduce.setText(encyclopedia.getIntroduces()[1]);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EncyclopediaDetailActivity.anctionStart((AppCompatActivity) context, encyclopedia.getObjectId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
