package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewArticleActivity extends AppCompatActivity {
    /*

            public Source source;
            public String author,title,description,url,urlToImage,publishedAt,content;
            class Source{
                public String id,name;
            }
    */
    //article.source.id,article.source.name,article.author,article.title,article.description,article.url,article.urlToImage,article.publishedAt,article.content
    Article article;

    TextView tvContent,tvAuthor,tvTitle;
    ImageView ivArticleImage;
    public Article getArticle() {
        return article;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);
        String[] values = getIntent().getStringArrayExtra("Values");
        article = new Article(values[0],values[1],values[2],values[3],values[4],values[5],values[6],values[7],values[8]);

        tvContent = findViewById(R.id.tvContent);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvTitle = findViewById(R.id.tvTitle);
        ivArticleImage = findViewById(R.id.ivArticleImage);

        tvContent.setText("\t\t   "+article.content);
        tvAuthor.setText("Article By " + article.author);
        tvTitle.setText(article.title);
        Picasso.with(this)
                .load(article.urlToImage)
                .placeholder(R.drawable.rounded_button)
                .error(R.drawable.rounded_button)
                .into(ivArticleImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

    }
}
