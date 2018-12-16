package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArticleRecycleAdapter extends RecyclerView.Adapter<ArticleRecycleAdapter.ViewHolder>{
    FavoritesManager favoritesManager;
    Context mContext;
    ArrayList<Article> mArticles;

    public void AddArticle(Article a){
        if(mArticles == null)
            mArticles = new ArrayList<>();

        mArticles.add(a);
    }

    public ArticleRecycleAdapter(ArrayList<Article> mArticles, Context mContext) {
        this.mArticles = mArticles;
        this.mContext = mContext;
        favoritesManager = FavoritesManager.GetInstance(mContext);
    }
    @NonNull
    @Override
    public ArticleRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_article, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ArticleRecycleAdapter.ViewHolder viewHolder, final int i) {
        Article article = mArticles.get(i);
        Log.i("Binding", Integer.toString(i));
        if (article.urlToImage != "" && article.urlToImage != null)
        {
            Picasso.with(mContext)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.rounded_button)
                    .error(R.drawable.rounded_button)
                    .into(viewHolder.ivFragImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Log.e("onSuccess", "Loaded image sucessfully");
                        }

                        @Override
                        public void onError() {
                            Log.e("onError", "Failed to load image");
                        }
                    });
        }
        viewHolder.ivFragImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Fuck you again", "onClick: " + i);
            }
        });
        viewHolder.tvFragTitle.setText("Title: " + article.title);
        viewHolder.tvFragDescription.setText(article.description);
        viewHolder.article = article;
        viewHolder.btnFavoriteArticle.setChecked(favoritesManager.HasArticle(article.title));
        viewHolder.btnFavoriteArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((ToggleButton)v).isChecked()){
                    favoritesManager.AddArticle(viewHolder.GetArticle());
                }
                else {
                    favoritesManager.RemoveArticle(viewHolder.GetArticle());
                }

                favoritesManager.apply();
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.i("ItemCount", Integer.toString(mArticles.size()));
        return mArticles.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivFragImage;
        public Context context;
        TextView tvFragTitle,tvFragDescription;
        ToggleButton btnFavoriteArticle;
        ConstraintLayout parentLayout;
        Article article;

        public ViewHolder(View itemView) {
            super(itemView);

            ivFragImage = itemView.findViewById(R.id.ivFragImage);
            tvFragDescription = itemView.findViewById(R.id.tvFragDescritpion);
            tvFragTitle = itemView.findViewById(R.id.tvFragTitle);

            parentLayout = itemView.findViewById(R.id.clytArticle);
            btnFavoriteArticle = itemView.findViewById(R.id.btnFavoriteArticle);
        }
        public Article GetArticle(){
            return article;
        }

        public void SetTitle(String title){
            tvFragTitle.setText(title);
        }
        public void InitFromArticle(Article article){
            tvFragTitle.setText("Title: " + article.title + "\nSource: " + article.source.name);
            tvFragDescription.setText(article.description);
            article = article;
            //tvSourceName.setText("Source: " + article.source.name);
            Picasso.with(ivFragImage.getContext()).load(article.urlToImage).into(ivFragImage);
        }
    }
}
