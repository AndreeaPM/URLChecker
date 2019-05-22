package com.example.urlchecker;

import android.app.AlertDialog;
import android.content.Context;
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
            private void ShowError(Context context)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Invalid url");
                builder.setCancelable(true);
                builder.setNeutralButton("Ok",null);
            }

            @Override
            public void onClick(View v) {

                TextView url = findViewById(R.id.urlInput);
                TextView name = findViewById(R.id.nameInput);
                TextView interval = findViewById(R.id.intervalInput);
                //TODO validate interval min, etc
                URI uri = null;
                try {
                    String inputUrl = url.getText().toString();
                    if(!inputUrl.startsWith("https://") && !inputUrl.startsWith("http://"))
                        inputUrl = "https://" + inputUrl;

                    uri = new URI(inputUrl);
                } catch (URISyntaxException e) {
                    ShowError(v.getContext());
                    return;
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
