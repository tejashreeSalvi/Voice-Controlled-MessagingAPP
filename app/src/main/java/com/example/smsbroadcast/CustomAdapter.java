package com.example.smsbroadcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tejashree on 01-04-2018.
 */

public class CustomAdapter extends BaseAdapter {


    Context context;
    ArrayList<String> storeContacts;
    LayoutInflater inflter;
    public CustomAdapter(Context context, ArrayList<String> storeContacts) {

        this.context = context;
        this.storeContacts = storeContacts;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return storeContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return getItemId(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflter.inflate(R.layout.activity_contacts_list,null);
        TextView ta = (TextView)convertView.findViewById(R.id.contactstext);
        ta.setText(storeContacts.get(position));
        return convertView;
    }
}






