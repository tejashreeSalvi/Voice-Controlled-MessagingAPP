package com.example.smsbroadcast;

import android.provider.BaseColumns;

/**
 * Created by Tejashree on 30-03-2018.
 */

public class TableData {

    public TableData() {

    }

    public static abstract class TableInfo implements BaseColumns{

        public static final String PHONE_NUMBER = "phone_number";
        public static final String MESSAGE_INFO = "message_info";
        public static final String DATE_OF_MESSAGE = "date_of_message";
        public static final String TIME_OF_MESSAGE = "time_of_message";
        public static final String DATABASE_NAME = "user_info";
        public static final String TABLE_NAME = "sms_info";
        public static final String MESSAGE_QUIT = "message_input";

    }
}
