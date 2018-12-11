package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

public class SettingsActivity extends AppCompatActivity {

    Spinner spnrLanguages, spnrCountry;
    SourceList sourceList;
    SharedPreferences preferences;
    String[] srcs,srcLan,srcCountry;
    RequestQueue requestQueue;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        RetrieveSources();
    }

    void RetrieveSources(){
        String url = "https://newsapi.org/v2/sources?apiKey=20691eacad374052a07ee662dd9bd63a";
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
                srcCountry = new String[len];
                srcLan = new String[len];

                boolean found = false;
                for(int i = 0; i < len;i++) {
                    srcs[i] = sourceList.GetSourceAt(i).name;

                    for(int j = 0; j < i; j++)
                    {
                        if(srcCountry[j] == srcCountry[i])
                            found = true;
                    }
                    if(!found) {
                        srcCountry[i] =sourceList.GetSourceAt(i).country;
                    }found = false;

                }

                ArrayAdapter<String> Country = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,srcCountry),
                Language = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,srcLan);

                spnrCountry.setAdapter(Country);
                spnrCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String country = srcCountry[position];
                        preferences.edit().putString("LastCountry",country).apply();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });
                spnrLanguages.setAdapter(Language);
                spnrLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String language = srcLan[position];
                        preferences.edit().putString("LastLanguages",language).apply();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });


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
}

