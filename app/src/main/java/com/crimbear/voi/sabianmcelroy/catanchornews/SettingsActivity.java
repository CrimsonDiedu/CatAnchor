package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    ListView lvTags;
    SharedPreferences preferences;
    EditText etEnterTag;
    String[] tags;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }
    public void AddTag(View v) {
        String tag = etEnterTag.getText().toString(), fullTagStr = "";
        tags = preferences.getString("FlickrTags","Cat").split(",");
        for (int i = 0; i < tags.length; i++) {
            if(tags[i].equals(tag)) {
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
}

