package com.example.smsbroadcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tejashree on 01-04-2018.
 */
public class MessageInboxAdapter extends BaseAdapter {
    Context context;
    int size;
    ArrayList<String> messagetext, messagenumber, messagetime;
    public MessageInboxAdapter(Context applicationContext, ArrayList<String> messagetext, ArrayList<String> messagenumber, ArrayList<String> messagetime) {

        this.context = applicationContext;
        this.messagetext = messagetext;
        this.messagenumber = messagenumber;
        this.messagetime = messagetime;
    }

    @Override
    public int getCount() {
        return messagetext.size();
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

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_message_inbox,parent,false);

        TextView message = (TextView)row.findViewById(R.id.textmessage);
        TextView number = (TextView)row.findViewById(R.id.textNumber);
        TextView time = (TextView)row.findViewById(R.id.textTime);
        ImageView image = (ImageView)row.findViewById(R.id.image);

        number.setText(messagenumber.get(position));

        message.setText(messagetext.get(position));

        time.setText(messagetime.get(position));

        image.setImageResource(R.drawable.iconperson);

        return row;
    }
}
