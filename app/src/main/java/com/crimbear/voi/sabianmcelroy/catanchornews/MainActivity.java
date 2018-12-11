package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;


public class MainActivity extends AppCompatActivity {
    int numFragments = 10;
    TextView tvSourceName;
    ImageView ivPicture;
    RequestQueue requestQueue;
    Spinner spnrSearchThrough;
    FragmentArticle curFrag;
    FragmentArticle[] fragments;
    SharedPreferences preferences;
    Button btnNextPage,btnLastPage;
    SourceList sourceList;
    int pages=0, imageIndex =0, pageIndex = 1,srcIndex = 0;
    String[] srcs,srcUrls;
    String source,query = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragments = new FragmentArticle[numFragments];
        for (int i = 0; i < numFragments; i++) {
            fragments[i] = (FragmentArticle) getSupportFragmentManager().findFragmentById(R.id.fgmt + i);
        }
        tvSourceName = findViewById(R.id.tvSourceName);
        preferences = getSharedPreferences("SharedProperties",MODE_PRIVATE);

        btnNextPage = findViewById(R.id.btnNextPage);
        btnLastPage = findViewById(R.id.btnLastPage);

        spnrSearchThrough = findViewById(R.id.spnrSearchThrough);

        source = preferences.getString("LastUsedSource","abc-news");


        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        if(connected)
        {
            final Context context = this;
            String flickrURL = "https://farm1.staticflickr.com/2/1418878_1e92283336_m.jpg";//"https://api.flickr.com/services/rest/?method=flickr.test.echo&name=value";
            requestQueue = Volley.newRequestQueue(this);


            RetrieveSources();

        }
        else{

            finish();
            System.exit(0);
        }
    }


    public void NextArticle(View v){
        NextArticle();
    }
    public void LastArticle(View v){
        LastArticle();
    }
    public void NextArticle(){
        pageIndex++;
        if(pageIndex==1){
            //on the first page, so cannot move further back, hid the previous page button
            btnLastPage.setEnabled(false);
            btnLastPage.setVisibility(View.INVISIBLE);
        }
        else
        {
            btnLastPage.setEnabled(true);
            btnLastPage.setVisibility(View.VISIBLE);
        }
        RetrieveNewsStory();
    }
    public void LastArticle(){
        pageIndex-=numFragments;
        pageIndex = pageIndex>1?pageIndex:1;
        if(pageIndex==1)
            btnLastPage.setVisibility(View.INVISIBLE);
        else
            btnLastPage.setVisibility(View.VISIBLE);
        RetrieveNewsStory();
    }

    public void NextSource(View v){
        pageIndex = 1;
        srcIndex++;
        source = "";
        RetrieveNewsStory();

    }
    void RetrieveSources(){
        String url = "https://newsapi.org/v2/sources?"+(query==""?"":"q="+query)+"&apiKey=20691eacad374052a07ee662dd9bd63a";
        final Context context = this;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                sourceList = gson.fromJson(response,SourceList.class);

                String language = preferences.getString("Language",""),country = preferences.getString("Country","");
                sourceList.GetSourcesLanguageCountry(language,country);

                int len = sourceList.sources.length;
                srcs = new String[len];
                srcUrls = new String[len];

                for(int i = 0; i < len;i++) {
                    srcs[i] = sourceList.GetSourceAt(i).name;
                    srcUrls[i] = sourceList.GetSourceAt(i).id;
                }

                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,srcs);

                spnrSearchThrough.setAdapter(stringArrayAdapter);
                spnrSearchThrough.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        source = srcUrls[position];
                        preferences.edit().putString("LastUsedSource",srcUrls[position]).apply();
                        RetrieveNewsStory();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });

                RetrieveNewsStory();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse ", "That didn't work!");
            }
        }
        );
        requestQueue.add(stringRequest);
    }


    NewsResults results;
    public void SetRes(NewsResults r){
        results = r;
    }
    public void RetrieveNewsStory() {
        if(false)
            return;
        final Context context = this;
        //TODO: Open a new page that contains the news story and the picture selected
        //Intent n = new Intent()
        int srcList = srcIndex % srcUrls.length;
            String src = (source==""?srcUrls[srcList]:source),
                    newsurl = "https://newsapi.org/v2/everything?sources=" + src + "&totalResults="+numFragments+"&pageSize=10&page="+pageIndex+"&apiKey=20691eacad374052a07ee662dd9bd63a";

//region request
            {
            Log.i("Request ",newsurl);
// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, newsurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                // Log.e("onResponse", response.toString());
                                Gson g = new Gson();
                                SetRes(g.fromJson(response, NewsResults.class));
                                JSONArray jsonArray = new JSONArray();
                                    for(int ind = 0; ind < numFragments; ind++) {
                                        curFrag = fragments[ind];
                                        if(results.articles.length==0){
                                            Log.e("FUCCCC","HI");
                                            return;
                                        }
                                        int i = (ind) % results.articles.length;

                                        curFrag.tvFragTitle.setText("Title: " + results.articles[i].title );
                                        curFrag.tvFragDescription.setText(results.articles[i].description);
                                        curFrag.article = results.articles[i];
                                        tvSourceName.setText("Source: "+results.articles[i].source.name);
                                        LoadImageFromURL(results.articles[i].urlToImage, curFrag.ivFragImage);
                                        //Log.e("Original Image", results.articles[i].urlToImage + " ");
                                        Log.e("Article Page", results.articles[i].url + "\n");

                                }
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

    public void LoadImageFromURL(String url, ImageView imageView){
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.rounded_button)
                .error(R.drawable.rounded_button)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }


    public void ToArticle(View v){
        Intent n = new Intent(this,ViewArticleActivity.class);
        startActivity(n);
    }
    public void ToSettings(View v){
        Intent n = new Intent(this,SettingsActivity.class);
        startActivity(n);
    }
    public void ToFlickrSettings(View v){
        Intent n = new Intent(this,FlickrSettings.class);
        startActivity(n);
    }
}
