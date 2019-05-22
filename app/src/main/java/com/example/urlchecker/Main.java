package com.example.urlchecker;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {

    private static UrlAdapter adapter = null;
    private List<UrlChecker> urlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            DataPersistence.Init(getFilesDir());
        } catch (IOException e) {
            e.printStackTrace();
        }


        ListView listView = (ListView) findViewById(R.id.listView);
        UrlChecker.appContext = getApplicationContext();

        if (adapter == null) {
            adapter = new UrlAdapter(this, urlList);
            listView.setAdapter(adapter);
            UrlChecker.adapter = adapter;
        }

        final Intent addIntent = new Intent(this, Add.class);
        final Intent detailsIntent = new Intent(this, Details.class);
        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(addIntent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                detailsIntent.putExtra("position", position);
                startActivity(detailsIntent);
            }
        });

        //DataPersistence.Debug();

        //TESTING
//        try {
//            UrlChecker test = new UrlChecker(5000, new URI("https://google.com"), "Description test");
//            urlList.add(test);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }


        UpdateList();

    }

    protected void UpdateList() {
        for (UrlChecker urlChecker : urlList)
            urlChecker.stop();

        try {
            urlList = DataPersistence.Read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new UrlAdapter(this, urlList);
        listView.setAdapter(adapter);
        UrlChecker.adapter = adapter;

        for (UrlChecker urlChecker : urlList) {
            urlChecker.run();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("resume", "@@@@@@@@@@@@@@@@@@ App resumed");


        UpdateList();
    }
}
