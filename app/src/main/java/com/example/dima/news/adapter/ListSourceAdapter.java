package com.example.dima.news.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dima.news.Interface.IconBetterIdeaService;
import com.example.dima.news.Interface.ItemClickListener;
import com.example.dima.news.ListNews;
import com.example.dima.news.R;
import com.example.dima.news.common.Common;
import com.example.dima.news.model.icons.IconBetterIdea;
import com.example.dima.news.model.news.WebSite;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Dima on 30.03.2018.
 */
class ListSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ItemClickListener itemClickListener;
    TextView sourceTitle;
    CircleImageView sourceImage;

    public ListSourceViewHolder(View itemView) {
        super(itemView);

        sourceImage = itemView.findViewById(R.id.source_image);
        sourceTitle = itemView.findViewById(R.id.source_name);

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
    private IconBetterIdeaService ideaService;

    public ListSourceAdapter(Context context, WebSite webSite) {
        this.context = context;
        this.webSite = webSite;
        ideaService = Common.getIconService();
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
        StringBuilder  iconBetterApi = new StringBuilder("https://icons.better-idea.org/allicons.jso n?url=");
        iconBetterApi.append(webSite.getSources().get(position).getUrl());
        ideaService.getIconUrl(iconBetterApi.toString()).enqueue(new Callback<IconBetterIdea>() {

            @Override
            public void onResponse(Call<IconBetterIdea> call, Response<IconBetterIdea> response)  {
                if(response.body().getIcons().size()>0)
                {
                    Picasso.with(context)
                            .load(response.body().getIcons().get(0).getUrl())
                            .into(holder.sourceImage);
                }
            }

            @Override
            public void onFailure(Call<IconBetterIdea> call, Throwable t) {

            }
        });

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
