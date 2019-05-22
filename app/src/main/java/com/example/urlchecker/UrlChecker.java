package com.example.urlchecker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.NOTIFICATION_SERVICE;

public class UrlChecker implements Runnable, AsyncResponse {

    public static UrlAdapter adapter;
    public static Context appContext;
    private static String GROUP_NAME = "com.android.example.UrlChecker";
    private final transient Handler handler;
    private long interval = 60 * 1000; //1 min
    private URI uri;
    private String name;
    private String lastChecked;
    private Hash lastHash;
    private double minDif = 0.001;

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

    private static boolean checkNotification(int id) {
        NotificationManager mNotificationManager = (NotificationManager) appContext.getSystemService(NOTIFICATION_SERVICE);
        StatusBarNotification[] notifications = mNotificationManager.getActiveNotifications();
        for (StatusBarNotification notification : notifications) {
            if (notification.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private void updateLastChecked() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss", Locale.UK);
        lastChecked = df.format(Calendar.getInstance().getTime());

        try {
            DataPersistence.Replace(this, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void check() {
        Log.d("url check", uri.toString());

        new LongOperation(this).execute(uri.toString());
    }

    @Override
    public void processFinish(String output) {
        Hash hash = new Hash(output);
        if (lastHash == null)
            lastHash = hash;
        else {
            double difference = lastHash.compareTo(hash);
            if (difference > minDif) {
                //TODO notif
                Log.d("@@@NOTIF@@@", uri.toString());
                showNotification(uri.toString());
            }
            lastHash = hash;
        }


        updateLastChecked();
        adapter.notifyDataSetChanged();
    }

    private void showNotification(String eventtext) {

        int id = hashCode();

        if (checkNotification(id))
            return;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri.toString()));

        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(appContext)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Url changed")
                .setContentText(eventtext)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup(GROUP_NAME)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(appContext);

        notificationManager.notify(id, builder.build());
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

    public Hash getLastHash() {
        return lastHash;
    }

    public void setLastHash(Hash lastHash) {
        this.lastHash = lastHash;
    }

    public double getMinDif() {
        return minDif;
    }

    public void setMinDif(double minDif) {
        this.minDif = minDif;
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


    private class LongOperation extends AsyncTask<String, Void, String> {

        private AsyncResponse delegate;//Call back interface

        LongOperation(AsyncResponse callback) {
            delegate = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(new URL(params[0]).openStream()));

                String inputLine;

                while ((inputLine = in.readLine()) != null)
                    result.append(inputLine);

                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish(result);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
