package com.crystal.arc.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class UserProvider extends ContentProvider{
 
        UserDBHelper dBlite;
        SQLiteDatabase db;
         
        private static final UriMatcher sMatcher;
        static{
                sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
                sMatcher.addURI(UserContract.AUTOHORITY,UserContract.TNAME, UserContract.ITEM);
                sMatcher.addURI(UserContract.AUTOHORITY, UserContract.TNAME+"/#", UserContract.ITEM_ID);
 
        }
        
        @Override
        public boolean onCreate() {
            this.dBlite = new UserDBHelper(this.getContext());
            return true;
        }
        
        @Override
        public String getType(Uri uri) {
            switch (sMatcher.match(uri)) {
                case UserContract.ITEM:
                    return UserContract.CONTENT_TYPE;
                case UserContract.ITEM_ID:
                    return UserContract.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalArgumentException("Unknown URI"+uri);
            }
        }
        
        @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {
                db = dBlite.getWritableDatabase();
                int count = 0;
                switch (sMatcher.match(uri)) {
                    case UserContract.ITEM:
                            count = db.delete(UserContract.TNAME,selection, selectionArgs);
                            break;
                    case UserContract.ITEM_ID:
                            String id = uri.getPathSegments().get(1);
                            count = db.delete(UserContract.TNAME, UserContract.TID+"="+id+(!TextUtils.isEmpty(UserContract.TID="?")?"AND("+selection+')':""), selectionArgs);
                        break;
                    default:
                            throw new IllegalArgumentException("Unknown URI"+uri);
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
        }
 
 
        @Override
        public Uri insert(Uri uri, ContentValues values) {               
                db = dBlite.getWritableDatabase();
                long rowId;
                if(sMatcher.match(uri)!=UserContract.ITEM){
                        throw new IllegalArgumentException("Unknown URI"+uri);
                }
                rowId = db.insert(UserContract.TNAME,UserContract.TID,values);
                if(rowId>0){
                        Uri noteUri=ContentUris.withAppendedId(UserContract.CONTENT_URI, rowId);
                        getContext().getContentResolver().notifyChange(noteUri, null);
                        return noteUri;
                }
                throw new IllegalArgumentException("Unknown URI"+uri);
        }
 
 
        @Override
        public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
                db = dBlite.getWritableDatabase();                
                Cursor c;
                Log.d("-------", String.valueOf(sMatcher.match(uri)));
                switch (sMatcher.match(uri)) {
                case UserContract.ITEM:
                        c = db.query(UserContract.TNAME, projection, selection, selectionArgs, null, null, null);
                 
                        break;
                case UserContract.ITEM_ID:
                        String id = uri.getPathSegments().get(1);
                        c = db.query(UserContract.TNAME, projection, UserContract.TID+"="+id+(!TextUtils.isEmpty(selection)?"AND("+selection+')':""),selectionArgs, null, null, sortOrder);
                    break;
                default:
                        Log.d("!!!!!!", "Unknown URI"+uri);
                        throw new IllegalArgumentException("Unknown URI"+uri);
                }
                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
        }
        @Override
        public int update(Uri uri, ContentValues values, String selection,
                        String[] selectionArgs) {
                return 0;
        }
}