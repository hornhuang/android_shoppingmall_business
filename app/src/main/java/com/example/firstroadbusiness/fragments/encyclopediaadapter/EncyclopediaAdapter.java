package com.example.firstroadbusiness.fragments.encyclopediaadapter;

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
import com.example.firstroadbusiness.bmobmanager.SuperImageLoader;
import com.example.firstroadbusiness.classes.Encyclopedia;

import java.util.List;

public class EncyclopediaAdapter extends RecyclerView.Adapter<EncyclopediaAdapter.ViewHolder>{

    private List<Encyclopedia> mLists;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private ImageView writer;

        private TextView writer_name;
        private TextView encolopedia_name;
        private TextView getEncolopedia_introduce;
        private TextView awesomes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            writer = (ImageView) itemView.findViewById(R.id.writer);
            writer_name = (TextView) itemView.findViewById(R.id.writer_name);
            encolopedia_name = (TextView) itemView.findViewById(R.id.encolopedia_name);
            getEncolopedia_introduce = (TextView) itemView.findViewById(R.id.getEncolopedia_introduce);
            awesomes = (TextView) itemView.findViewById(R.id.awesomes);
        }
    }

    public EncyclopediaAdapter(List<Encyclopedia> mList, Context context){
        this.mLists = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.encolopedia_list_item,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Encyclopedia encyclopedia = mLists.get(i);
        if (encyclopedia.getBitmaps() != null){
            viewHolder.imageView.setImageBitmap(encyclopedia.getBitmaps()[0]);
        }
        viewHolder.writer_name.setText(encyclopedia.getLinkUser().getFlag());// nikename
//        viewHolder.awesomes.setText(encyclopedia.getAwesomes());
        if (encyclopedia.getIntroduces() != null){
            viewHolder.encolopedia_name.setText(encyclopedia.getIntroduces()[0]);
            viewHolder.getEncolopedia_introduce.setText(encyclopedia.getIntroduces()[1]);
        }
        viewHolder.writer.setImageBitmap(encyclopedia.getLinkUser().getUserIcon());
        new SuperImageLoader(viewHolder.writer, encyclopedia.getLinkUser()).userLoad();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EncyclopediaDetailActivity.anctionStart((AppCompatActivity) context, encyclopedia.getObjectId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }
}
