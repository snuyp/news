package com.example.dima.news.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dima.news.DetailArticle;
import com.example.dima.news.Interface.ItemClickListener;
import com.example.dima.news.R;
import com.example.dima.news.common.ISO8601Parse;
import com.example.dima.news.model.news.Article;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dima on 03.04.2018.
 */
class ListNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ItemClickListener itemClickListener;

    TextView articleTitle;
    RelativeTimeTextView articleTime;
    CircleImageView articleImage;

    public ListNewsViewHolder(View itemView) {
        super(itemView);
        articleTitle = itemView.findViewById(R.id.article_title);
        articleTime = itemView.findViewById(R.id.article_time);
        articleImage = itemView.findViewById(R.id.article_image);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        try {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        } catch (NullPointerException e) {

        }
    }
}

public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsViewHolder> {
    private List<Article> articleList;
    private Context context;

    public ListNewsAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.news_layout, parent, false);
        return new ListNewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListNewsViewHolder holder, int position) {

        try {
            if (articleList.get(position).getUrlToImage().isEmpty()) {
                Picasso.with(context)
                        .load(R.drawable.ic_terrain_black_24dp)
                        .into(holder.articleImage);
            } else {
                Picasso.with(context)
                        .load(articleList.get(position).getUrlToImage())
                        .error(R.drawable.ic_terrain_black_24dp)
                        .into(holder.articleImage);
            }

            if (articleList.get(position).getTitle().length() > 65) {
                holder.articleTitle.setText(articleList.get(position).getTitle().substring(0, 65) + "...");
            } else {
                holder.articleTitle.setText(articleList.get(position).getTitle());
            }
            Date date = null;
            try {
                date = ISO8601Parse.parse(articleList.get(position).getPublishedAt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                holder.articleTime.setReferenceTime(date.getTime());
            }

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    Intent intent = new Intent(context, DetailArticle.class);
                    intent.putExtra("webURL", articleList.get(position).getUrl());
                    context.startActivity(intent);
                }
            });
        } catch (NullPointerException e) {
            Log.e("ERROR", e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
