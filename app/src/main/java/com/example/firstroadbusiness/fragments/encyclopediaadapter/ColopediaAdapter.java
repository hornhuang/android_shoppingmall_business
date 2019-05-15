package com.example.firstroadbusiness.fragments.encyclopediaadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.firstroadbusiness.R;
import com.example.firstroadbusiness.bmobmanager.FinalImageLoader;
import com.example.firstroadbusiness.classes.Comment;

import java.util.List;

public class ColopediaAdapter extends RecyclerView.Adapter<ColopediaAdapter.ViewHolder>{

    private List<Comment> mList ;

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView authorIcon;
        private TextView  authorName;
        private TextView  commontContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            authorIcon = itemView.findViewById(R.id.comment_author_image);
            authorName = itemView.findViewById(R.id.comment_author_name);
            commontContent = itemView.findViewById(R.id.comment_content);
        }
    }

    public ColopediaAdapter(List<Comment> mList){
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.clopedia_list_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        if (mList.get(i).getWriter().getImageFile() != null){
            new FinalImageLoader(viewHolder.authorIcon, mList.get(i).getWriter().getImageFile()).imageLoad();
            viewHolder.authorName.setText(mList.get(i).getWriter().getNickname());
            viewHolder.commontContent.setText(mList.get(i).getCommentContent());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
