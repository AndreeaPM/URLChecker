package com.example.urlchecker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = this.getApplicationContext();
        Intent i= new Intent(context, TestService.class);
        i.putExtra("KEY1", "Value to be used by the service");
        context.startService(i);

        setContentView(R.layout.activity_main);
    }
}
