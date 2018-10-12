package com.example.dima.news.ui.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dima.news.ui.DetailArticle;
import com.example.dima.news.Interface.ItemClickListener;
import com.example.dima.news.R;
import com.example.dima.news.common.ISO8601Parse;
import com.example.dima.news.mvp.model.news.Article;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

    public ListNewsAdapter(List<Article> articleList) {
        this.articleList = articleList;
        notifyDataSetChanged();
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
            Glide.with(holder.itemView.getContext())
                    .load(articleList.get(position).getUrlToImage())
                    .apply(new RequestOptions().placeholder(R.drawable.news))
                    .into(holder.articleImage);
            if (articleList.get(position).getTitle().length() > 65) {
                holder.articleTitle.setText(articleList.get(position).getTitle().substring(0, 65) + "...");
            } else if(String.valueOf(articleList.get(position).getTitle().charAt(0)).equals("�")) {
                holder.articleTitle.setText(R.string.title_not_found);
            } else {
                holder.articleTitle.setText(articleList.get(position).getTitle());
            }
            Date date = ISO8601Parse.parse(articleList.get(position).getPublishedAt());
            if (date != null) {
                holder.articleTime.setReferenceTime(date.getTime());
            }
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    Intent intent = new Intent(holder.itemView.getContext(), DetailArticle.class);
                    intent.putExtra("webURL", articleList.get(position).getUrl());
                    holder.itemView.getContext().startActivity(intent);
                }
            });

        } catch (NullPointerException e) {
            Log.e("ERROR", e.getMessage());
        } catch (ParseException | IllegalArgumentException e) {
            Log.e("ParseException", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
