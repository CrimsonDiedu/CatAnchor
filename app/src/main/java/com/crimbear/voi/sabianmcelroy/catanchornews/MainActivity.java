package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
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
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    int numFragments = 100;
    TextView tvSourceName;
    RequestQueue requestQueue;
    Spinner spnrSearchThrough;
    Article curFrag;
    FragmentArticle[] fragments;
    SharedPreferences preferences;
    Button btnNextPage,btnLastPage;
    SourceList sourceList;
    SearchView sbArticles;
    int pages=0, imageIndex =0, pageIndex = 1,srcIndex = 0;
    String[] srcs,srcUrls;
    String source,query = "";

    RecyclerView rvArticles;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragments = new FragmentArticle[numFragments];
        for (int i = 0; i < numFragments; i++) {
            //fragments[i] = (FragmentArticle) getSupportFragmentManager().findFragmentById(R.id.fgmt + i);
        }
        tvSourceName = findViewById(R.id.tvSourceName);
        preferences = getSharedPreferences("SharedProperties",MODE_PRIVATE);

        btnNextPage = findViewById(R.id.btnNextPage);
        btnLastPage = findViewById(R.id.btnLastPage);
        rvArticles = findViewById(R.id.rvArticles);
        sbArticles = findViewById(R.id.sbArticles);
        spnrSearchThrough = findViewById(R.id.spnrSearchThrough);

        sbArticles.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                spnrSearchThrough.setEnabled(true);
                spnrSearchThrough.setVisibility(View.VISIBLE);
                return false;
            }
        });
        sbArticles.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spnrSearchThrough.setEnabled(false);
                spnrSearchThrough.setVisibility(View.INVISIBLE);
            }
        });
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
        mLayoutManager = new LinearLayoutManager(this);
        rvArticles.setLayoutManager(mLayoutManager);

        mAdapter = new ArticleRecycleAdapter(new ArrayList<Article>()
                ,this);
        rvArticles.setAdapter(mAdapter);
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
        Log.e("Info","Started retrieving sources");
        String url = "https://newsapi.org/v2/sources?"+(query==""?"":"q="+query)+"&apiKey=20691eacad374052a07ee662dd9bd63a";
        final Context context = this;
        ///todo
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                sourceList = gson.fromJson(response,SourceList.class);
                String language = preferences.getString("Language",""),country = preferences.getString("Country","");
                sourceList.GetSourcesLanguageCountry(language,country);

                int sourclen=sourceList.sources.length,len = (sourclen>numFragments)?numFragments:sourclen;
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
                        Log.e("Info","Started retrieving sources");
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
                Log.e("RetrieveSourcesError", "Failure in RetrieveSources");
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

        final Context context = this;
        //TODO: Open a new page that contains the news story and the picture selected
        //Intent n = new Intent()
        int srcList = srcIndex % srcUrls.length;
            String src = (source==""?srcUrls[srcList]:source),
                    newsurl = "https://newsapi.org/v2/everything?sources=" + src + "&totalResults="+numFragments+"&pageSize=100&page="+pageIndex+"&apiKey=20691eacad374052a07ee662dd9bd63a";

//region request
            {

// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, newsurl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            rvArticles.setAdapter(mAdapter);
                            ArrayList<Article> articleList = new ArrayList<>();

                            // Display the first 500 characters of the response string.
                            // Log.e("onResponse", response.toString());
                            Gson g = new Gson();
                            SetRes(g.fromJson(response, NewsResults.class));

                            for(int ind = 0; ind < numFragments; ind++) {
                                if(results.articles.length==0){
                                    return;
                                }
                                int i = (ind) % results.articles.length;
                                articleList.add(results.articles[i]);

                                /*
                                tvSourceName.setText("Source: "+results.articles[i].source.name);
                                LoadImageFromURL(results.articles[i].urlToImage, curFrag.ivFragImage);

                                //Log.e("Original Image", results.articles[i].urlToImage + " ");
                                Log.e("Article Page", results.articles[i].url + "\n");
                                */




                            }
                            mLayoutManager = new LinearLayoutManager(context);
                            mAdapter = new ArticleRecycleAdapter(articleList
                                    ,context);
                            rvArticles.setAdapter(mAdapter);
                            rvArticles.setLayoutManager(mLayoutManager);
                        }
                    }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RetrieveNewsStoryError ", "That didn't work!");
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
