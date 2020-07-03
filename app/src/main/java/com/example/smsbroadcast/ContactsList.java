package com.example.smsbroadcast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class
ContactsList extends AppCompatActivity {


    TextView ta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        ta = (TextView)findViewById(R.id.contactstext);
    }

}
