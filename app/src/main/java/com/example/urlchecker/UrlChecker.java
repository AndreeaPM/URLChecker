package com.example.urlchecker;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UrlChecker implements Runnable {

    private final transient Handler handler;

    private long interval = 60 * 1000; //1 min
    private URI uri;
    private String name;
    private String lastChecked;
    public static UrlAdapter adapter;

    public UrlChecker() {
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

    private void updateLastChecked() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss", Locale.UK);
        lastChecked = df.format(Calendar.getInstance().getTime());

        try {
            DataPersistence.Replace(this,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void check() {
        Log.d("url check", uri.toString());
        //TODO here check
        updateLastChecked();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void run() {
        check(); //TODO maybe call only if needed
        handler.postDelayed(this, interval);
    }

    public void stop() {
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

    public void setLastChecked(String lastChecked) {
        this.lastChecked = lastChecked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrlChecker that = (UrlChecker) o;

        if (getInterval() != that.getInterval()) return false;
        if (!getUri().equals(that.getUri())) return false;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        int result = (int) (getInterval() ^ (getInterval() >>> 32));
        result = 31 * result + getUri().hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }
}
