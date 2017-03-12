package br.com.gefersom.phonebook.model;

import android.provider.BaseColumns;

/**
 * Created by me on 18/9/2016.
 */
public class PersonContract implements BaseColumns {

    public static final String TABLE_NAME           = "person";
    public static final String DISPLAY_NAME         = "_name";
    public static final String PHOTO_URI            = "_photo_uri";
    public static final String TELEPHONE            = "_telephone";
    public static final String EMAIL                = "_email";
    public static final String JOB                  = "_job";
    public static final String GROUP                = "_group";
    public static final String NOTES                = "_notes";
    public static final String ADDRESS              = "_address";
    public static final String LOOKUP_KEY           = "_lookup_key";
    public static final String CHURCH_PHOTO_URI     = "_church_photo_uri";
    public static final String CHURCH               = "_church";
    public static final String BLOOD_TYPE           = "_blood_type";
    public static final String COMPANY              = "_company";
    public static final String BIRTHDAY             = "_birthday";
}
