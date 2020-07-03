package com.example.smsbroadcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Tejashree on 02-04-2018.
 */
public class MainAdapter extends BaseAdapter {


    ArrayList<String> arrayList;
    Context context;

    LayoutInflater inflater;
    public MainAdapter(Context applicationContext, ArrayList<String> arrayList) {

        this.context = applicationContext;
        this.arrayList = arrayList;

        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        convertView = inflater.inflate(R.layout.activity_messagalert,null);

        TextView ta = (TextView)convertView.findViewById(R.id.textMessage1);
        ta.setText(arrayList.get(position));
        return convertView;
    }
}
