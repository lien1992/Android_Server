package com.crystal.arc.contentprovider;

import android.net.Uri;

public class UserContract {
 
        public static final String DBNAME = "userDB"; 
        public static final String TNAME = "users";
        public static final int VERSION = 1;
         
        public static String TID = "tid";
        public static final String NAME = "name";
        public static final String PASSWORD = "password";
        public static final String REGISTER_DATE = "register_date";
        public static final String DELETE_FLAG = "delete_flag";
         
         
        public static final String AUTOHORITY = "com.crystal.arc";
        public static final int ITEM = 1;
        public static final int ITEM_ID = 2;
         
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.crystal.arc";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.crystal.arc";
         
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTOHORITY + "/users");
}