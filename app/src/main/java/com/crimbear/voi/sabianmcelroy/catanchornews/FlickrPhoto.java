package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/*
* https://farm1.staticflickr.com/2/1418878_1e92283336_m.jpg
*

farm-id: 1
server-id: 2
photo-id: 1418878
secret: 1e92283336
size: m
*
*/
public class FlickrPhoto{
    String url,secret,imSize;
    int farm_id,server_id;

    public  FlickrPhoto(String url){
        String read = url + '|',data = "";
        int ind = 0;
        for(int i = 0; i < read.length(); i++){
            data+=read.charAt(i);
        }

    }
    public static Drawable LoadImageFromWebOperations(String path, ImageView destination) {

        try {
            URI uri = new URI(path);
            URL url = uri.toURL();

            Log.e("URL CREATED ",path+'\n'+url.getHost()+'\n'+url.getUserInfo()+'\n'+url.getAuthority()+'\n'+url.getProtocol()+'\n'+url.getRef());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            Log.e("MM ","is.getClass().getName()");
            InputStream is = (InputStream) connection.getInputStream();


            Log.e("DEWAE ","is.getClass().getName()");


            return Drawable.createFromStream(is,null);
        } catch (Exception e) {
            Log.e("Badti(me).victim = me",e.getMessage()+" sad");
            e.printStackTrace();
            return null;
        }
    }
    //region fuck
    /*
    public static Drawable LoadImageFromWebOperations(Context context,String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        return null;
    }
    */


    /*

    public static Drawable LoadImageFromWebOperations(String url) {
        HttpURLConnection connection;
        try {

            URL site = new URL(url);
            Log.e("URL CREATED ",url);

            connection = (HttpURLConnection)site.openConnection();
            Log.e("e","CONNECTING");
            connection.connect();
            Log.e("CONNECTED ",url);

            InputStream is =  connection.getInputStream();
            Log.e("DEWAE ",is.getClass().getName());
            Drawable d = Drawable.createFromStream(is,null);
            return d;
        } catch (Exception e) {
            Log.e("Badti(me).victim = me",e.getMessage()+" sad");


            return null;
        }
    }*/
    //endregion

}
