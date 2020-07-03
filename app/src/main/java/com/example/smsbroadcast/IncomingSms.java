package com.example.smsbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.widget.Toast;
import java.lang.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.SimpleTimeZone;

public class IncomingSms extends BroadcastReceiver {

    String smsMessage = "";
    String address,dateText;
    String smsbody;
    int result;
    String ContactName = null;
    String m;
    String[] dateString;
    long timeMillis;
    public static final String SMS_BUNDLE = "pdus";
    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if(intentExtras != null)
        {
                Object[] sms = ((Object[])intentExtras.get(SMS_BUNDLE)) ;



                for(int i = 0 ; i <sms.length ; i++)
                {
                    SmsMessage smsMessage1 = SmsMessage.createFromPdu((byte[])sms[i]);
                    smsbody = smsMessage1.getMessageBody().toString();
                    m = smsMessage1.getOriginatingAddress();

                    Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(m));
                    Cursor c = context.getContentResolver().query(lookupUri, new String[]{ContactsContract.Data.DISPLAY_NAME},null,null,null);
                    try {
                        c.moveToNext();
                        String  displayName = c.getString(0);
                         ContactName = displayName;

                    } catch (Exception e) {
                        // TODO: handle exception
                    }finally{
                        c.close();
                    }

                    timeMillis = smsMessage1.getTimestampMillis();
                    Date date = new Date(timeMillis);
                    SimpleDateFormat sampleFormat = new SimpleDateFormat("dd/MM/yy hh:mm");

                    dateText = sampleFormat.format(date);
                    dateString = dateText.split(" ");
                    smsMessage +=address +" at"+"\t"+dateText+"\n";
                    smsMessage += smsbody+"\n";

                    if(ContactName == null)
                    {
                        address = m;
                    }
                    else {
                        address = ContactName;
                    }
                }

                DatabaseOperation dop = new DatabaseOperation(context);
                dop.putInformation(dop,address,smsbody,dateString[0],dateString[1]);

            Toast.makeText(context, "Successfull....", Toast.LENGTH_SHORT).show();

            //Go to AlertDialogBox
            Intent in = new Intent(context,AlertDialogBoxApp.class);

            //passing parameters like Message and Number using putExtra() ie Content Provider
            in.putExtra("Message",smsbody);
            in.putExtra("Number",address);
            in.putExtra("Date",dateString[0]);
            in.putExtra("Time",dateString[1]);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        }
    }
}


