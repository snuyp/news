package com.example.dima.news.ui.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dima.news.Interface.ItemClickListener;
import com.example.dima.news.ui.ListNewsActivity;
import com.example.dima.news.R;

import com.example.dima.news.mvp.model.news.SourceNews;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Dima on 30.03.2018.
 */
class ListSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ItemClickListener itemClickListener;
    TextView sourceTitle;
    CircleImageView sourceImage;

    public ListSourceViewHolder(View itemView) {
        super(itemView);

        sourceTitle = itemView.findViewById(R.id.source_name);
        sourceImage = itemView.findViewById(R.id.source_image);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}


public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceViewHolder> {
    private List<SourceNews> sources;


    public ListSourceAdapter(List<SourceNews> sources) {
        this.sources = sources;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListSourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.source_card_layout,parent, false);
        return new ListSourceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListSourceViewHolder holder, int position) {

        holder.sourceTitle.setText(sources.get(position).getName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(holder.itemView.getContext(), ListNewsActivity.class);
                intent.putExtra("source", sources.get(position).getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return sources.size();
    }
}
