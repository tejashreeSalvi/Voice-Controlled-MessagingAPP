package com.example.smsbroadcast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    Animation animation;
    MainAdapter mainAdapter;
    EditText txtPhone, txtbody;
    ImageButton btn, mic;
    ImageView contacts,more;
    String number;
    ListView list;
    ArrayAdapter<String> adapter;
    RelativeLayout relative;
    ArrayList<String> arrayList = new ArrayList<String>();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relative = (RelativeLayout)findViewById(R.id.relative_alert);
        txtPhone = (EditText) findViewById(R.id.edittext1);
        txtbody = (EditText) findViewById(R.id.editmessage);
        list = (ListView) findViewById(R.id.list_item);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.animator.slide_left);
        relative.startAnimation(animation);


        contacts = (ImageView) findViewById(R.id.contacts);
        mic = (ImageButton) findViewById(R.id.micrecord);
        more = (ImageView)findViewById(R.id.more);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(MainActivity.this,more);
                popupMenu.getMenuInflater().inflate(R.menu.pop_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Call") && !txtPhone.getText().toString().trim().equals(""))
                        {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("tel:"+txtPhone.getText().toString()));
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Plz select Contacts.", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });


        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 20);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 10);
                } else {
                    Toast.makeText(getBaseContext(), "Featured not supported...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContactsApp.class);
                startActivity(intent);
            }
        });

        String number = getIntent().getStringExtra("number");
        txtPhone.setText(number);
        mainAdapter = new MainAdapter(getApplicationContext(),arrayList);

        list.setAdapter(mainAdapter);

        btn = (ImageButton) findViewById(R.id.sendbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsend(v);

            }
        });

        txtbody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    btn.setVisibility(View.INVISIBLE);
                    mic.setVisibility(View.VISIBLE);

                } else {
                    btn.setVisibility(View.VISIBLE);
                    mic.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void buttonsend(View v) {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "in before if statement", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS)) {
                Toast.makeText(getApplicationContext(), "No Permission..", Toast.LENGTH_SHORT).show();
            }
        } else {

            SmsManager sendsms = SmsManager.getDefault();
            String phone = txtPhone.getText().toString();
            String msg = txtbody.getText().toString();
            if (phone.equals("")) {
                Toast.makeText(this, "Add recipient/phonenumber", Toast.LENGTH_SHORT).show();
            } else {
                txtbody.setText("");
                sendsms.sendTextMessage(phone, null, msg, null, null);
                arrayList.add(arrayList.size(), msg);
                mainAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK || data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtbody.setText(result.get(0));
                }
        }
    }
}