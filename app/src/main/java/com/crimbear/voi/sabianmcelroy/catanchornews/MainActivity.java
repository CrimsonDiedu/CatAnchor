package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    TextView tvHelloWorld,tvSiteDescription,tvSiteTitle;
    EditText etSearchTag;
    ImageView ivPicture;
    IVThread thread;
    RequestQueue requestQueue;
    int pageIndex,
            imageIndex = pageIndex = 0,srcIndex = 0;
    String[] srcs = ("abc-news,mashable,bbc-news,buzzfeed,cbs-news,crypto-coins-news").split(",");

    void InitIVThread(){
        thread = new IVThread( MainActivity.this);
        thread.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPicture           = findViewById(R.id.ivPicture);
        tvHelloWorld        = findViewById(R.id.tvSiteDescription);
        tvSiteTitle         = findViewById(R.id.tvSiteTitle);
        tvSiteDescription   = findViewById(R.id.tvSiteDescription);
        etSearchTag         = findViewById(R.id.etSearchTag);
        final Context context = this;
        String flickrURL = "https://farm1.staticflickr.com/2/1418878_1e92283336_m.jpg";//"https://api.flickr.com/services/rest/?method=flickr.test.echo&name=value";
        requestQueue = Volley.newRequestQueue(this);
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
        String tags = "Cat", src = "associated-press", newsurl = "https://newsapi.org/v2/top-headlines?sources=" + src + "&apiKey=20691eacad374052a07ee662dd9bd63a",
                url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=2f0b4021021997cc3a82a0aeed6a700d&text=" + tags + "&per_page=10&format=json&nojsoncallback=1";//"http://www.google.com";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //Log.e("onResponse", response.toString());
                        //tvHelloWorld.setText(response);

                        //todo: notify main thread when branch has image
                        InitIVThread();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse ", "That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
        NewsStory();

    }

    public void CatPicture(View v) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getDrawable(R.drawable.ic_launcher_background);
        } else
            return;

        //TODO: Implement Flickr API Here
        final String  tags = etSearchTag.getText().toString(),
                flickrUrl = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=2f0b4021021997cc3a82a0aeed6a700d&text=" + tags + "&per_page=10&format=json&nojsoncallback=1";
        imageIndex++;
        {

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
// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, flickrUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            // Log.e("onResponse", response.toString());
                            Gson g = new Gson();
                            FlickrResults results = g.fromJson(response,FlickrResults.class);
                            //tvHelloWorld.setText(response);
                            //
                            int i = imageIndex%results.photos.photo.length;
                            FlickrResults.Photos.Photo photo = results.getPhoto(i);
                            //Log.e("Image Index",i+"");
                            FlickrPhoto flickrPhoto = new FlickrPhoto(photo.secret,'m',photo.farm,photo.server, photo.id);
                                    /*
    public FlickrPhoto(String secret, char size,
                       int farm_id, int server_id, int photo_id)
                                     */

                            Log.e("Image URL",flickrPhoto.url);
                            InitIVThread();
                            thread.path = flickrPhoto.url;
                            thread.photoRef = flickrPhoto;
                            thread.start();
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

        ivPicture.setImageDrawable(drawable);
    }
    public void NewsStory(View v){
        pageIndex++;
        NewsStory();
    }
    public void ChangeSource(View v){
        pageIndex = 0;
        srcIndex++;
        NewsStory();
    }
    public void UpdateStream(InputStream is){
        Drawable d = Drawable.createFromStream(is,null);
        ivPicture.setImageDrawable(d);
    }

    public void NewsStory() {
        final Context context = this;
        //TODO: Open a new page that contains the news story and the picture selected
        //Intent n = new Intent()
        int srcList = srcIndex % srcs.length;
        String src = srcs[srcList],
                newsurl = "https://newsapi.org/v2/top-headlines?sources=" + src + "&totalResults=3&apiKey=20691eacad374052a07ee662dd9bd63a";
//region request
        {

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
// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, newsurl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            // Log.e("onResponse", response.toString());
                            Gson g = new Gson();
                            NewsResults results = g.fromJson(response,NewsResults.class);
                            JSONArray  jsonArray= new JSONArray();
                            int i = pageIndex%results.articles.length;
                            //tvHelloWorld.setText(response);
                            tvSiteTitle.setText("Title: "+ results.articles[i].title+"\nSource: "+results.articles[i].source.name);
                            tvSiteDescription.setText(results.articles[i].description);
                            ((MainActivity)context).UpdateStream(FlickrPhoto.LoadStaticImageFromWebOperations(results.articles[i].urlToImage));
                            Log.e("Original Image",results.articles[i].urlToImage+ " ");
                            Log.e("Article Page",results.articles[i].url + " ");

                            //tvHelloWorld.setText("Source\n\n"+results.articles[0].source.name);

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

//endregion
    }



    public void OnImageFound() throws InterruptedException {
        Log.e("Merging threads","....");
        InputStream is = thread.is;


        if(is == null)return;
        //Log.e("ImageSetStr",is.toString());
        Drawable drawable = Drawable.createFromStream(is,null);
        if(thread.path != null)
        return;
        ivPicture.setImageDrawable(drawable);
        if(drawable != null&& thread.path==null) {

            BitmapDrawable bmpD = (BitmapDrawable)drawable;


            int pixWidth=this.getWindowManager().getDefaultDisplay().getWidth(),pixHeight=this.getWindowManager().getDefaultDisplay().getWidth();
            int dpw =pixWidth/(int)getResources().getDisplayMetrics().density , dph=pixHeight/(int)getResources().getDisplayMetrics().density ;

            Bitmap resized = Bitmap.createScaledBitmap(bmpD.getBitmap(), pixWidth, pixHeight, true);


            //ivPicture.setImageDrawable(bmpD);
            ivPicture.setImageBitmap(resized);

            Log.e("ImageSet", "T");
        }
    }
}
