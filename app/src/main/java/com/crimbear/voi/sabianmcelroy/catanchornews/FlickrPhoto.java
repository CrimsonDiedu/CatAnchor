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
        https://farm1.staticflickr.com/2/1418878_1e92283336_m.jpg

farm-id: 1
server-id: 2
photo-id: 1418878
secret: 1e92283336
size: m
        https://farm5.staticflickr.com/4901/31019866107_7245bed2dd77c8dc.jpg
url = https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
url = "https://farm"+farm_id+"staticflickr.com/"+server_id+'/'+photo_id+'_'+secret+'_'+size+".jpg";
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
    public FlickrPhoto(String secret, char size,
                       int farm_id, int server_id, String photo_id)
    {
        this.secret = secret;
        this.imSize = imSize;
        this.farm_id = farm_id;
        this.server_id = server_id;
        url = "https://farm"+farm_id+".staticflickr.com/"+server_id+'/'+photo_id+'_'+secret+".jpg";
    }

    public InputStream LoadImageFromWebOperations() {
        return LoadStaticImageFromWebOperations(url);
    }
    public static InputStream LoadStaticImageFromWebOperations(String path) {
        if(path == null){
            Log.e("NOPATH","Missing path statement");
            return null;
        }
        try {//https://farm5.staticflickr.com/4901/31019866107_7245bed2dd77c8dc.jpg
            //Log.e("CREATing url","path: "+path);

            URI uri = new URI(path);
            URL url = uri.toURL();

            //Log.e("URL CREATED ",path+'\n'+url.getHost()+'\n'+url.getUserInfo()+'\n'+url.getAuthority()+'\n'+url.getProtocol()+'\n'+url.getRef());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //Log.e("MM ","is.getClass().getName()");
            InputStream is = (InputStream) connection.getInputStream();
            return is;


        } catch (Exception e) {
            //Log.e("Badti(me).victim = me",e.getMessage()+" sad");
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
