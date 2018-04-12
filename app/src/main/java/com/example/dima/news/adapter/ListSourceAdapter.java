package com.example.dima.news.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dima.news.Interface.ItemClickListener;
import com.example.dima.news.ListNews;
import com.example.dima.news.R;

import com.example.dima.news.model.news.WebSite;
import com.squareup.picasso.Picasso;

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
    private Context context;
    private WebSite webSite;


    public ListSourceAdapter(Context context, WebSite webSite) {
        this.context = context;
        this.webSite = webSite;

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

        holder.sourceTitle.setText(webSite.getSources().get(position).getName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, ListNews.class);
                intent.putExtra("source", webSite.getSources().get(position).getId());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return webSite.getSources().size();
    }
}
