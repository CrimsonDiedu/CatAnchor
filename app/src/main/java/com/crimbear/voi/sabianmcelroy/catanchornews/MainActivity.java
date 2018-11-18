package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.StrictMode;
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

public class MainActivity extends AppCompatActivity {
    TextView tvHelloWorld;
    ImageView ivPicture;
    IVThread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivPicture = findViewById(R.id.ivPicture);
        tvHelloWorld = findViewById(R.id.tvHelloWorld);
        final Context context = this;
        String flickrURL = "https://farm1.staticflickr.com/2/1418878_1e92283336_m.jpg";//"https://api.flickr.com/services/rest/?method=flickr.test.echo&name=value";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//region no
        /*
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.GET,
            flickrURL,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("REST RESPONSE ",response.toString());
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error){
                    Log.e("ERROR REST RESPONSE ",error.toString() );
                }
            }
            );

        requestQueue.add(jsonObjectRequest);
        */
        //endregion
        String tags = "Cat",src="associated-press",newsurl= "https://newsapi.org/v2/top-headlines?sources="+src+"&apiKey=20691eacad374052a07ee662dd9bd63a",
         url = " https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=8856b3f057a21cb464b3a34f65e0cee8&text="+tags+"&format=json&nojsoncallback=1";//"http://www.google.com";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //Log.e("onResponse",response.substring(0,500));
                        //tvHelloWorld.setText(response);
                        if(false) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }

                        //todo: notify main thread when branch has image
                        //thread = new IVThread(ivPicture,);
                        thread.start();


                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse ","That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);

    }
    public void CatPicture(View v){
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getDrawable(R.drawable.ic_launcher_background);
        }else
            return;

        //TODO: Implement Flickr API Here

        ivPicture.setImageDrawable(drawable);
    }

    public void NewsStory(View v){
        //TODO: Open a new page that contains the news story and the picture selected
        //Intent n = new Intent()
    }

    public void OnImageFound(Drawable drawable) {
        if(drawable != null)
        ivPicture.setImageDrawable(drawable);
    }
}
