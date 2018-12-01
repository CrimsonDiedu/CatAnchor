package com.crimbear.voi.sabianmcelroy.catanchornews;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class SettingsActivity extends AppCompatActivity {

    ListView lvTags;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preferences = getSharedPreferences("SharedProperties",MODE_PRIVATE);
    }
}
