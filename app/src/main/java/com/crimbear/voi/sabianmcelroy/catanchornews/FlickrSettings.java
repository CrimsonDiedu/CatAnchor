package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class FlickrSettings extends AppCompatActivity {
    ListView lvTags;
    SharedPreferences preferences;
    EditText etEnterTag;
    String[] tags;
    RequestQueue requestQueue;
    ImageView[] ivs;
    int numPhotos = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preferences = getSharedPreferences("SharedProperties",MODE_PRIVATE);

        lvTags = findViewById(R.id.lvTags);
        etEnterTag = findViewById(R.id.etEnterTag);

        InitList();
        lvTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (String)(lvTags.getItemAtPosition(position));
                etEnterTag.setText(selectedFromList);
            }
        });
        ivs = new ImageView[numPhotos];
        for(int i = 0; i < numPhotos; i++)
            ivs[i] = findViewById(R.id.ivFr1+i);

        random = new Random();
    }

    public void AddTag(View v) {
        String tag = etEnterTag.getText().toString(), fullTagStr = "";
        tags = preferences.getString("FlickrTags","Cat").split(",");
        for (int i = 0; i < tags.length; i++) {
            if(tags[i].equals(tag) && tag != "") {
                Toast.makeText(this,"You already have the tag"+tag+" In your list",Toast.LENGTH_SHORT);
                return;
            }
            fullTagStr += tags[i] + ',' ;
        }
        fullTagStr+= tag;
        preferences.edit().putString("FlickrTags",fullTagStr).apply();
        InitList();
    }
    public void RemoveTag(View v){
        String tag = etEnterTag.getText().toString(), fullTagStr = "";
        tags = preferences.getString("FlickrTags","Cat").split(",");
        for(int i = 0; i < tags.length;i++){
            if(tags[i].equals(tag)) {
                tags[i] = "";
            }
            else {
                fullTagStr += tags[i] + ',';
            }

        }
        preferences.edit().putString("FlickrTags",fullTagStr).apply();
        InitList();

    }
    public void ClearText(View v){
        ((TextView)v).setText("");
    }
    public void InitList(){
        tags = preferences.getString("FlickrTags","Cat").split(",");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                tags);
        lvTags.setAdapter(adapter);
    }
    Random random;
    public void SeeExamples(View v){
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=2f0b4021021997cc3a82a0aeed6a700d&tags=" + preferences.getString("FlickrTags","Cat") + "&per_page=10&format=json&nojsoncallback=1";//"http://www.google.com";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.e("onResponse", response.toString());
                        //tvHelloWorld.setText(response);
                        int rint = Math.abs(random.nextInt());
                        Gson g = new Gson();

                        FlickrResults results = g.fromJson(response,FlickrResults.class);
                        //tvHelloWorld.setText(response);
                        for(int j = 0; j < numPhotos; j++) {
                            int i = j%results.photos.photo.length;
                            FlickrResults.Photos.Photo photo = results.getPhoto((j+rint)%results.photos.photo.length);
                            //Log.e("Image Index",i+"");
                            FlickrPhoto flickrPhoto = new FlickrPhoto(photo.secret, 'm', photo.farm, photo.server, photo.id);
                            if(i < ivs.length)
                                Picasso.with(getBaseContext()).load(flickrPhoto.url).into(ivs[i]);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse ", "That didn't work!");
            }
        });
        requestQueue.add(stringRequest);
    }

}
