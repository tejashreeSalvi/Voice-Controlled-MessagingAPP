package com.example.smsbroadcast;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;

public class InboxApp extends AppCompatActivity {

    TextToSpeech tts;
    ListView listView;
    Cursor cr;
    int result;
    String number;
    String message,time,date;
    MessageInboxAdapter inboxAdapter;
    ArrayList<String> messagetext,messagenumber,messagetime,messagedate;
    ImageButton contacts;
    FloatingActionButton send;

    AdapterView.AdapterContextMenuInfo info;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_app);


       listView = (ListView)findViewById(R.id.listInbox);
       messagetext = new ArrayList<String>();
       messagenumber = new ArrayList<String>();
       messagetime = new ArrayList<String>();
       messagedate = new ArrayList<String>();


    //custom Adapater

       send = (FloatingActionButton)findViewById(R.id.openMainActivity);
       contacts = (ImageButton)findViewById(R.id.Contacts);

       contacts.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             startActivity(new Intent(getApplicationContext(),ContactsApp.class));
             }
        });
        send.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             Intent intent = new Intent(getApplicationContext(),MainActivity.class);
             startActivity(intent);
             }
         });

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS)
                {
                    result = tts.setLanguage(Locale.US);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "This Feature is not Supported.... ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //calling database getInformation() method...
         DatabaseOperation db = new DatabaseOperation(getApplicationContext());
         cr = db.getInformation(db);

        while(cr.moveToNext())
         {
          number = cr.getString(0);
          message = cr.getString(1);
          time = cr.getString(3);
          date = cr.getString(2);

          messagetext.add(message);
          messagenumber.add(number);
          messagetime.add(time);
          messagedate.add(date);
        }

        inboxAdapter = new MessageInboxAdapter(getApplicationContext(),messagetext,messagenumber,messagetime);

        listView.setAdapter(inboxAdapter);

        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_context,menu);
        menu.setHeaderTitle("Options");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

         id = info.position;
        String message =  messagetext.get(id);
        String number = messagenumber.get(id);
        String date = messagedate.get(id);
        String time = messagetime.get(id);


        switch(item.getItemId())
        {
            case R.id.delete:

                AlertDialog.Builder alert = new AlertDialog.Builder(InboxApp.this);
                alert.setMessage("This message will be deleted.");
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseOperation dop = new DatabaseOperation(getApplicationContext());
                        dop.deleteInformation(dop,messagenumber.get(id),messagetext.get(id),messagedate.get(id),messagetime.get(id));
                        messagetext.remove(info.position);
                        messagenumber.remove(info.position);
                        messagetime.remove(info.position);
                        inboxAdapter.notifyDataSetChanged();
                        tts.speak("Message Deleted",TextToSpeech.QUEUE_FLUSH,null);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

                break;

            case R.id.read:
                tts.speak("Number : "+number+" \n"+"Message"+message, TextToSpeech.QUEUE_FLUSH, null);

            break;

            case R.id.view_details:

                AlertDialog.Builder builder = new AlertDialog.Builder(InboxApp.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_view_message,null);
                EditText etype = (EditText)view.findViewById(R.id.edittype);
                etype.setText("Text Message");

                EditText efrom = (EditText) view.findViewById(R.id.editfrom);
                efrom.setText(messagenumber.get(id));

                EditText etimereceived = (EditText)view.findViewById(R.id.edittimereceived);
                etimereceived.setText(messagetime.get(id)+" "+messagedate.get(id));

                builder.setView(view);

                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }
        return super.onContextItemSelected(item);
    }
}
