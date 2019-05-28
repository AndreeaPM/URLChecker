package com.example.urlchecker;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);
        Log.d("DESCRIPTION", "FOUND POS " + position);

        if (position == -1)
            finish();

        TextView url = findViewById(R.id.urlDetails);
        TextView name = findViewById(R.id.nameDetails);
        TextView lastDate = findViewById(R.id.dateDetails);
        TextView interval = findViewById(R.id.intervalDetails);
        TextView minDif = findViewById(R.id.minDetails);



        try {
            final UrlChecker selected = DataPersistence.Read().get(position);
            url.setText(selected.getUri().toString());
            name.setText(selected.getName());
            lastDate.setText(selected.getLastChecked());
            interval.setText(String.valueOf(selected.getInterval() / 1000));
            minDif.setText(String.valueOf((int)(selected.getMinDif() * 100)));

            findViewById(R.id.accessButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(selected.getUri().toString()));
                    startActivity(intent);
                }
            });

            findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                try {
                                    DataPersistence.Delete(selected);
                                    finish();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
