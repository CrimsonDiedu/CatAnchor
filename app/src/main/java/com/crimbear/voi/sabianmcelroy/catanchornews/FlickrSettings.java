package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TableLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class FlickrSettings extends AppCompatActivity {

    SearchView svSearchTag;
    RequestQueue requestQueue;
    TableLayout tlytPictures;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr_settings);
        svSearchTag = findViewById(R.id.svSearchTag);
        requestQueue = Volley.newRequestQueue(this);
    }
    public void LoadImages(View v){
        String flickrUrl = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=2f0b4021021997cc3a82a0aeed6a700d&text=" + svSearchTag.getQuery().toString() + "&per_page=10&&format=json&nojsoncallback=1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, flickrUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // Log.e("onResponse", response.toString());
                        Gson g = new Gson();
                        FlickrResults results = g.fromJson(response,FlickrResults.class);
                        //tvHelloWorld.setText(response);
                        int i = 1%results.photos.photo.length;
                        FlickrResults.Photos.Photo photo = results.getPhoto(i);
                        //Log.e("Image Index",i+"");
                        FlickrPhoto flickrPhoto = new FlickrPhoto(photo.secret,'m',photo.farm,photo.server, photo.id);
                                    /*
    public FlickrPhoto(String secret, char size,
                       int farm_id, int server_id, int photo_id)
                                     */
                        Log.e("Image URL",flickrPhoto.url);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse ", "That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }
}
