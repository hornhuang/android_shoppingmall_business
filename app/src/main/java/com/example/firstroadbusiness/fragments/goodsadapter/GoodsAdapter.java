package com.example.firstroadbusiness.fragments.goodsadapter;

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
import com.example.firstroadbusiness.classes.Goods;

import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder>{

    private List<Goods> mList;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView title, introduce, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.goods_image);
            title = itemView.findViewById(R.id.goods_title);
            introduce = itemView.findViewById(R.id.goods_introduce);
            price = itemView.findViewById(R.id.goods_price);
        }
    }

    public GoodsAdapter(List<Goods> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goods_items_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Goods goods = mList.get(i);
        new FinalImageLoader(viewHolder.imageView, goods.getBmobFile()[0]).imageLoad();
        viewHolder.title.setText(goods.getTitle());
        viewHolder.introduce.setText(goods.getTitle());
        viewHolder.price.setText("惊爆价 ¥" + goods.getPrice() + " ！");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsActivity.anctionStart((AppCompatActivity) context, goods.getObjectId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
