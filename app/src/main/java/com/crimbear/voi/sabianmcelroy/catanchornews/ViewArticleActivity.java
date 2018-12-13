package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
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
    SharedPreferences preferences;
    TextView tvContent,tvAuthor,tvTitle;
    ImageView ivArticleImage;
    int picInd = 0;
    boolean viewFlickrImg;
    RequestQueue requestQueue;
    public Article getArticle() {
        return article;
    }
    Context context = this;
    public void TogglePicture(View v){
        if(viewFlickrImg) {
            LoadFlickrImage();
            picInd++;
        }
        else{
            LoadArticleImg();
        }
        viewFlickrImg = !viewFlickrImg;
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

        tvContent.setText("\t\t   " + article.content);
        tvAuthor.setText("Article By " + article.author);
        tvTitle.setText(article.title);
        preferences = getSharedPreferences("SharedProperties",MODE_PRIVATE);
        viewFlickrImg = preferences.getBoolean("LoadImageFromArticle",false);
        requestQueue = Volley.newRequestQueue(this);

        if(!viewFlickrImg) {
            LoadArticleImg();

        }
        else{
            LoadFlickrImage();
            picInd++;
        }
    }
    void LoadFlickrImage(){
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=2f0b4021021997cc3a82a0aeed6a700d&tags=" + preferences.getString("FlickrTags","Cat") + "&per_page=10&format=json&nojsoncallback=1";//"http://www.google.com";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.e("onResponse", response.toString());
                        //tvHelloWorld.setText(response);
                        Gson g = new Gson();

                        FlickrResults results = g.fromJson(response,FlickrResults.class);
                        //tvHelloWorld.setText(response);
                        FlickrResults.Photos.Photo photo = results.photos.photo[picInd];
                        //(String secret, char size,
                        //                       int farm_id, int server_id, String photo_id)
                        //    {
                        FlickrPhoto flickrPhoto = new FlickrPhoto(photo.secret,'m',photo.farm,photo.server,photo.id);
                        Picasso.with(context).load(flickrPhoto.url).into(ivArticleImage);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse ", "That didn't work!");
            }
        });
        requestQueue.add(stringRequest);
    }
    void LoadArticleImg(){
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
                        }
                );
    }
}

