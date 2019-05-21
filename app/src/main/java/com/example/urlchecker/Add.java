package com.example.urlchecker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Add extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView url = findViewById(R.id.urlInput);
                TextView name = findViewById(R.id.nameInput);
                TextView interval = findViewById(R.id.intervalInput);
                //TODO validate interval min, etc
                URI uri = null;
                try {
                    uri = new URI(url.getText().toString());
                } catch (URISyntaxException e) {
                    e.printStackTrace();//TODO error here
                }
                UrlChecker newUrl = new UrlChecker(Integer.parseInt(interval.getText().toString()) * 1000, uri, name.getText().toString());

                try {
                    DataPersistence.Add(newUrl);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
