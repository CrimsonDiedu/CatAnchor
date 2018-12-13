package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

public class FavoritesManager {
    ArrayList<Article> favoriteArticles;

    SharedPreferences preferences;
    static  FavoritesManager instance;
    private FavoritesManager(Context context){
        favoriteArticles = new ArrayList<>();

        preferences = context.getSharedPreferences("FavoriteArticles",Context.MODE_PRIVATE);

        String allFavorites = preferences.getString("Favorites","");
        if(allFavorites.equals(""))
            return;

        String[] articleStrings = allFavorites.split(";"),article;
        String id,name,author,title,description,url,urltoImage,publishedAt,content;
        for(int i = 0; i < articleStrings.length; i++){
            article = articleStrings[i].split(",");
            if(article.length != 9)
                continue;
            id = article[0];
            name = article[1];
            author = article[2];
            title = article[3];
            description = article[4];
            url = article[5];
            urltoImage = article[6];
            publishedAt = article[7];
            content = article[8];
            favoriteArticles.add(new Article(id,name,author,title,description,url,urltoImage,publishedAt,content));
            //public Article(String id,String name, String author, String title, String description, String url, String urlToImage, String publishedAt, String content)

            Log.e("Info","Started FavoritesManager");
        }
    }

    public void apply(){
        String allFavorites = "";
        Article article;
        for(int i = 0; i < favoriteArticles.size();i++)
        {//loop through all articles
            article = favoriteArticles.get(i);
            allFavorites += article.source.id+",";
            allFavorites += article.source.name+",";
            allFavorites += article.author+",";
            allFavorites += article.title+",";
            allFavorites += article.description+",";
            allFavorites += article.url+",";
            allFavorites += article.urlToImage+",";
            allFavorites += article.publishedAt+",";
            allFavorites += article.content+";";

        }
        preferences.edit().putString("Favorites",allFavorites).apply();
        Log.e("Info","Saved FavoritesManager");
    }

    public void AddArticle(Article a){
        favoriteArticles.add(a);
        Log.e("Info","Adding to FavoritesManager: "+a.title);
    }
    public void RemoveArticle(Article a){
        for (int i = 0; i < favoriteArticles.size(); i++){
            if(favoriteArticles.get(i).title.contentEquals(a.title)){
                Log.e("Info","Removing from FavoritesManager: "+a.title);
                favoriteArticles.remove(i);
                i--;
            }
        }
    }
    public boolean HasArticle(String title){
        boolean hasArticle = false;
        for (int i = 0; i < favoriteArticles.size();i++)
        {
            if(favoriteArticles.get(i).title.contentEquals(title)){
                hasArticle = true;
                break;
            }
        }
        return hasArticle;
    }

    public static FavoritesManager GetInstance(Context context){
        if(instance == null){
            instance = new FavoritesManager(context);
        }

        return instance;
    }
}
