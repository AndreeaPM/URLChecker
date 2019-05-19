package com.example.urlchecker;

import android.os.Handler;
import android.util.Log;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UrlChecker implements Runnable {

    private final Handler handler;

    private long interval = 60 * 1000; //1 min
    private URI uri;
    private String name;
    private String lastChecked;
    public static UrlAdapter adapter;

    public UrlChecker()
    {
        this.handler = new Handler();
    }

    public UrlChecker(long interval, URI uri) {
        this.uri = uri;
        this.handler = new Handler();
        this.interval = interval;
    }

    public UrlChecker(long interval, URI uri, String name) {
        this.uri = uri;
        this.handler = new Handler();
        this.interval = interval;
        this.name = name;
    }

    private void updateLastChecked()
    {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss", Locale.UK);
        lastChecked = df.format(Calendar.getInstance().getTime());
    }

    private void check()
    {
        Log.d("url check", uri.toString());
        updateLastChecked();
        adapter.notifyDataSetChanged();
        //TODO
    }

    @Override
    public void run() {
        check();
        handler.postDelayed(this,interval);
    }

    public void stop()
    {
        handler.removeCallbacks(this);
    }

    public Handler getHandler() {
        return handler;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastChecked() {
        return lastChecked;
    }
}
