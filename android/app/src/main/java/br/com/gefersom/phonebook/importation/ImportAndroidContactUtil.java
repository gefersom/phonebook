package br.com.gefersom.phonebook.importation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;

import junit.framework.Assert;

import java.util.ArrayList;

import br.com.gefersom.phonebook.model.AndroidContact;
import br.com.gefersom.phonebook.model.Person;

/**
 * Created by me on 23/9/2016.
 */
public class ImportAndroidContactUtil {


    public static final String[] PROJECTION_ANDROID_CONTACT = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.LOOKUP_KEY};

    public static final String SELECTION_HASPHONENUMBER_STRING = ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " = 1";

    public static final String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS};

    public static CursorLoader createPersonCursorLoader(Context context){

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        return new CursorLoader(context,
                uri,
                PROJECTION_ANDROID_CONTACT,
                SELECTION_HASPHONENUMBER_STRING,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " COLLATE NOCASE ASC");
    }


    public static WriteImportedPersonAsyncTask createWritePersonContactAsyncTask(Context context, ArrayList<Person> people){
        return new WriteImportedPersonAsyncTask(context, people );

    }

    public static Person getPersonFrom(Cursor cursor){

        String id = cursor.getString( cursor.getColumnIndex(ContactsContract.Contacts._ID) );
        String name = cursor.getString( cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME) );
        String photoUri = cursor.getString( cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI) );
        String lookupKey = cursor.getString( cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY) );

        Assert.assertNotNull(id);
        Assert.assertNotNull(name);
        Assert.assertNotNull(lookupKey);

        if ( photoUri == null ) {
            photoUri = "";
        }

        String bloodType = "O+";

        return new AndroidContact(id, name, photoUri, bloodType, lookupKey);
    }

    public static boolean requestReadAdnroidContactPermision(AppCompatActivity appCompatActivity, String[] permissions, int requestCode){
        int permissionCheck = ContextCompat.checkSelfPermission(appCompatActivity, Manifest.permission.WRITE_CALENDAR);
        if ( permissionCheck != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(appCompatActivity, permissions, requestCode);
            return false;
        }

        return true;
    }

}