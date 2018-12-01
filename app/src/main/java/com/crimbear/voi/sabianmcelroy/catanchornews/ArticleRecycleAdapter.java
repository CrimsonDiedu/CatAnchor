package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleRecycleAdapter extends RecyclerView.Adapter<ArticleRecycleAdapter.ViewHolder>{

    Context mContext;
    ArrayList<Article> mArticles;

    public ArticleRecycleAdapter(ArrayList<Article> mArticles, Context mContext) {
        this.mArticles = mArticles;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ArticleRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_article, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleRecycleAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivFragImage;
        TextView tvFragTitle,tvFragDescription;
        ConstraintLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);

            ivFragImage = itemView.findViewById(R.id.ivFragImage);
            tvFragDescription = itemView.findViewById(R.id.tvFragDescritpion);
            tvFragTitle = itemView.findViewById(R.id.tvFragTitle);

            parentLayout = itemView.findViewById(R.id.cvArticle);
        }
    }
}
