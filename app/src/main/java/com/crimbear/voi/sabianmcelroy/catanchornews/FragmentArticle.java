package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentArticle extends Fragment {
    public TextView tvFragTitle,tvFragDescription;
    public ImageView ivFragImage;
    Article article;



    public FragmentArticle(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_article,container,false);

        tvFragTitle = v.findViewById(R.id.tvFragTitle);
        tvFragDescription = v.findViewById(R.id.tvFragDescritpion);
        ivFragImage = v.findViewById(R.id.ivFragImage);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    //android:onClick="ToArticle"
                if(article == null)
                {
                    Toast.makeText(getContext(),"Article does not exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent n = new Intent(v.getContext(),ViewArticleActivity.class);
                String[] articleStr = {article.source.id,article.source.name,article.author,article.title,article.description,article.url,article.urlToImage,article.publishedAt,article.content};
                //  article.source.id,  article.source.name,    article.author, article.title,
                //  article.description,article.url,            article.urlToImage,article.publishedAt,
                //  article.content
                n.putExtra("Values",articleStr);
                getContext().startActivity(n);
            }
        });

        return v;
    }


}
