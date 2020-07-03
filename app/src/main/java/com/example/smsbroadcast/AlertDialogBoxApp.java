package com.example.smsbroadcast;

import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.skyfishjy.library.RippleBackground;
import com.varunest.sparkbutton.SparkButton;
import java.util.ArrayList;
import java.util.Locale;

public class AlertDialogBoxApp extends AppCompatActivity {

    SparkButton spark ;
    TextToSpeech tts;
    ImageButton imagespeak;
    TextView tamic;
    Intent intent;
    int result;
    RelativeLayout rl;
    AlertDialog alertDialog;
    String message,number;
    String numb;
    RippleBackground rippleBackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog_box_app);

        //calling alert method
        alert();


        rippleBackground=(RippleBackground)findViewById(R.id.content);
        spark = (SparkButton)findViewById(R.id.hello);
        //When mic is tabbed then perform this action

        spark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spark.setInactiveImage(R.drawable.microphoneicon);
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,30);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,11);

                //If supported than call OnActivity..
                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 10);
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Featured not supported...", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getBaseContext(),"Featured Exhibited",Toast.LENGTH_SHORT).show();
                }
                rippleBackground.startRippleAnimation();

            }
        });
        spark.setActiveImage(R.drawable.searchone);

        tamic = (TextView)findViewById(R.id.mic);

        rl = (RelativeLayout)findViewById(R.id.Relative);

        //Object of TextToSpeech and initialization
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
    }

    //alert Method :  Shows an alert dialog box...
    public void alert()
    {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);

        //Add message and button to dialog box...

        alert.setMessage("What you want to do ?")
                .setCancelable(true)
                .setPositiveButton("CLOSE", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }})

                .setNegativeButton("DELETE",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        intent = getIntent();
                        message = intent.getStringExtra("Message");
                        number = intent.getStringExtra("Number");
                        String date = intent.getStringExtra("Date");
                        String time = intent.getStringExtra("Time");
                        DatabaseOperation dop = new DatabaseOperation(getBaseContext());
                        dop.deleteInformation(dop,number,message,date,time);
                        Toast.makeText(getApplicationContext(), "Message Deleted..", Toast.LENGTH_SHORT).show();


                    }});

        alert.setNeutralButton("READ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(AlertDialogBoxApp.this,InboxApp.class));
            }
        });

        //create a alert
        alertDialog = alert.create();
        alertDialog.setTitle("Please speak your choice");
        alertDialog.show();
    }

    //onActivity method : when user speak read , delete ,or close then perform the following task...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        rippleBackground.stopRippleAnimation();
        switch(requestCode) {
            case 10:
                if (resultCode == RESULT_OK || data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tamic.setText(result.get(0));
                    switch (result.get(0)) {

                        //Read
                        case "read":
                            //Taking input from IncomingSms : ie Message and the Number...
                            intent =  getIntent();
                            message = intent.getStringExtra("Message");
                            number = intent.getStringExtra("Number");
                            tts.speak("Number : "+number+" \n"+"Message "+message, TextToSpeech.QUEUE_FLUSH, null);
                        break;

                        //Close
                        case "close":
                            finish();
                            tts.speak("Window is closed..", TextToSpeech.QUEUE_FLUSH, null);

                            break;

                        //Delete
                        case "delete":
                            intent = getIntent();
                            message = intent.getStringExtra("Message");
                            number = intent.getStringExtra("Number");
                            String date = intent.getStringExtra("Date");
                            String time = intent.getStringExtra("Time");
                            DatabaseOperation dop = new DatabaseOperation(getBaseContext());
                            dop.deleteInformation(dop,number,message,date,time);
                            Toast.makeText(this, "Message Deleted..", Toast.LENGTH_SHORT).show();
                            tts.speak("Message Deleted...",TextToSpeech.QUEUE_FLUSH,null);
                        break;
                        //For Reply
                        case "reply":
                            intent = getIntent();
                            number = intent.getStringExtra("Number");
                            Intent intent = new Intent(AlertDialogBoxApp.this,MainActivity.class);
                            startActivity(intent);
                        break;
                        case "stop":
                            tts.stop();
                         break;

                         //For Another Choice...
                            default:tts.speak("Invalid Choice",TextToSpeech.QUEUE_FLUSH,null);
                    }
                }
            }
        }
}

