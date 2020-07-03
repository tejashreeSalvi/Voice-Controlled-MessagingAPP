package com.example.smsbroadcast;


import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsApp extends AppCompatActivity {

    ListView listView;
    Intent i;
    ArrayList<String> StoreContacts;
    Cursor cursor;
    CustomAdapter customAdapter;
    String[] str1;
    String name, phonenumber;
    public static final int RequestPermissionCode = 1;


    public ContactsApp()
    {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_app);


        StoreContacts = new ArrayList<String>();

        listView = (ListView) findViewById(R.id.contactslist);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               String str = StoreContacts.get(position);

                String[] str1 = str.split(":");

                Toast.makeText(ContactsApp.this, "Name : "+str1[0], Toast.LENGTH_SHORT).show();
                if(str1[1].charAt(1) == '+')
                {
                    str1[1] = str1[1].substring(4);
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("number", str1[1]);
                startActivity(intent);
            }
        });

        // EnableRuntimePermission();

        ContactsIntoArrayList();

        listView.setAdapter(customAdapter);
    }

    public void ContactsIntoArrayList() {

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            StoreContacts.add(name + " " + ":" + " " +phonenumber);
        }
        customAdapter = new CustomAdapter(getApplicationContext(),StoreContacts);

        cursor.close();
    }

}
