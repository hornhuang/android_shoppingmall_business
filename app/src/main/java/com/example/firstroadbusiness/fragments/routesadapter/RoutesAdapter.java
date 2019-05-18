package com.example.firstroadbusiness.fragments.routesadapter;

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
import com.example.firstroadbusiness.classes.Routes;

import java.util.List;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder>{

    private List<Routes> mList;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView scenicName;
        private TextView label_1, label_2;
        private TextView introduce;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            scenicName = itemView.findViewById(R.id.scenicName);
            label_1 = itemView.findViewById(R.id.label_1);
            label_2 = itemView.findViewById(R.id.label_2);
            introduce = itemView.findViewById(R.id.introduce);
        }
    }

    public RoutesAdapter(List<Routes> mList, Context context){
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.routes_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Routes routes = mList.get(i);
        new FinalImageLoader(viewHolder.imageView, routes.getBmobFiles()[0]).imageLoad();
        viewHolder.scenicName.setText(routes.getIntroduces()[0]);
        viewHolder.label_1.setText(routes.getIntroduces()[5]);
        viewHolder.label_2.setText(routes.getIntroduces()[6]);
        viewHolder.introduce.setText(routes.getIntroduces()[7]);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoutesDetailActivity.actionStart((AppCompatActivity) context, routes.getObjectId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
