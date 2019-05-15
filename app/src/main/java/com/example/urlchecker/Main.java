package com.example.urlchecker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Main extends AppCompatActivity {

    private final ArrayList<UrlChecker> urlList = new ArrayList<>();
    private static UrlAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (adapter == null) {
            ListView listView = (ListView) findViewById(R.id.listView);
            adapter = new UrlAdapter(this, urlList);
            listView.setAdapter(adapter);
            UrlChecker.adapter = adapter;
        }

        //TESTING
        try {
            UrlChecker test = new UrlChecker(5000, new URI("https://google.com"), "Description test");
            urlList.add(test);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //TODO change this
        for (UrlChecker urlChecker : urlList) {
            urlChecker.run();
        }
    }
}
