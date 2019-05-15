package com.example.urlchecker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UrlAdapter extends ArrayAdapter<UrlChecker> {
    public UrlAdapter(Context context, List<UrlChecker> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        UrlChecker urlChecker = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.url_list_layout, parent, false);
        }

        TextView url = (TextView) convertView.findViewById(R.id.urlLine);
        TextView urlName = (TextView) convertView.findViewById(R.id.urlDescLine);
        TextView urlLastDate = convertView.findViewById(R.id.urlLastTime);
        // Populate the data into the template view using the data object
        url.setText(urlChecker.getUri().toString());
        urlName.setText(urlChecker.getName());
        urlLastDate.setText(urlChecker.getLastChecked());

        return convertView;
    }

}
