package br.com.gefersom.phonebook.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by me on 18/9/2016.
 */
class PersonDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "ibji.db";
    private final String TAG = PersonDbHelper.class.getSimpleName();

    public PersonDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.d("Create Table", DATABASE_VERSION + "");

        final String SQL_CREATE_PERSON_TABLE = "CREATE TABLE " + PersonContract.TABLE_NAME
                + " ("
                + PersonContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PersonContract.DISPLAY_NAME + " TEXT NOT NULL, "
                + PersonContract.PHOTO_URI + " TEXT, "
                + PersonContract.TELEPHONE + " TEXT, "
                + PersonContract.EMAIL + " TEXT, "
                + PersonContract.JOB + " TEXT, "
                + PersonContract.GROUP + " TEXT, "
                + PersonContract.NOTES + " TEXT, "
                + PersonContract.ADDRESS + " TEXT, "
                + PersonContract.LOOKUP_KEY + " TEXT, "
                + PersonContract.CHURCH_PHOTO_URI + " TEXT, "
                + PersonContract.CHURCH + " TEXT, "
                + PersonContract.BLOOD_TYPE + " TEXT, "
                + PersonContract.COMPANY + " TEXT, "
                + PersonContract.BIRTHDAY + " TEXT "
                + ")";
        try {
            sqLiteDatabase.execSQL(SQL_CREATE_PERSON_TABLE);
        }
        catch (Exception e){
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PersonContract.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    public boolean insert(Person person){
        List<Person> people = new ArrayList<>(1);
        people.add(person);

        return this.insert(people);
    }

    public boolean insert(List<Person> people){

        if ( people == null ) {
            return false;
        }

        Log.i("TAG", "PersonDbHelper.Insert");

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int numRows = people.size();

        boolean ret = true;
        try {
            for (int i = 0; i < numRows; ++i) {
                Person person = people.get(i);
                contentValues.put(PersonContract.DISPLAY_NAME, person.getName());
                contentValues.put(PersonContract.PHOTO_URI, person.getPhotoUri());
                contentValues.put(PersonContract.TELEPHONE, person.getPhone());
                contentValues.put(PersonContract.EMAIL, person.getEmail());
                contentValues.put(PersonContract.JOB, person.getJob());
                contentValues.put(PersonContract.GROUP, person.getGroup());
                contentValues.put(PersonContract.NOTES, person.getNotes());
                contentValues.put(PersonContract.ADDRESS, person.getAddress());
                contentValues.put(PersonContract.LOOKUP_KEY, person.getLookupKey());
                contentValues.put(PersonContract.CHURCH_PHOTO_URI, person.getChurchPhotoUri());
                contentValues.put(PersonContract.CHURCH, person.getChurch());
                contentValues.put(PersonContract.BLOOD_TYPE, person.getBloodType());
                contentValues.put(PersonContract.COMPANY, person.getCompany());
                contentValues.put(PersonContract.BIRTHDAY, person.getBirthday());

                database.insert(PersonContract.TABLE_NAME, null, contentValues);
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            ret = false;
        } finally {
            database.close();
        }

        return ret;
    }

    public boolean update(List<Person> people){

        if ( people == null ) {
            return false;
        }

        Log.i("TAG", "PersonDbHelper.Update");

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int numRows = people.size();

        boolean ret = true;
        try {
            for (int i = 0; i < numRows; ++i) {
                Person person = people.get(i);
                contentValues.put(PersonContract.DISPLAY_NAME, person.getName());
                contentValues.put(PersonContract.PHOTO_URI, person.getPhotoUri());
                contentValues.put(PersonContract.TELEPHONE, person.getPhone());
                contentValues.put(PersonContract.EMAIL, person.getEmail());
                contentValues.put(PersonContract.JOB, person.getJob());
                contentValues.put(PersonContract.GROUP, person.getGroup());
                contentValues.put(PersonContract.NOTES, person.getNotes());
                contentValues.put(PersonContract.ADDRESS, person.getAddress());
                contentValues.put(PersonContract.LOOKUP_KEY, person.toString());
                contentValues.put(PersonContract.CHURCH_PHOTO_URI, person.getChurchPhotoUri());
                contentValues.put(PersonContract.CHURCH, person.getChurch());
                contentValues.put(PersonContract.LOOKUP_KEY, person.getChurch());
                contentValues.put(PersonContract.BLOOD_TYPE, person.getBloodType());
                contentValues.put(PersonContract.COMPANY, person.getCompany());
                contentValues.put(PersonContract.BIRTHDAY, person.getBirthday());

                String whereClause = PersonContract._ID + " = " + person.getId();

                database.update(PersonContract.TABLE_NAME, contentValues, whereClause, null);
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            ret = false;
        } finally {
            database.close();
        }

        return ret;
    }


    public List<Person> getPeople( )
    {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(PersonContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                PersonContract.DISPLAY_NAME + " COLLATE NOCASE ASC");

        ArrayList<Person> arrayList = new ArrayList<>(cursor.getCount());

        while( cursor.moveToNext() ) {
            arrayList.add( getPersonFromCursor(cursor) );
        }

        return arrayList;
    }

    public Person getPerson( String personID )
    {
        SQLiteDatabase database = getReadableDatabase();

        String[] selectionArgs = { String.valueOf(personID) };

        Cursor cursor = database.query(PersonContract.TABLE_NAME,
                null,
                PersonContract._ID + " = ?",
                selectionArgs,
                null,
                null,
                null );

        if ( cursor == null || !cursor.moveToFirst() ) {
            return null;
        }

        return getPersonFromCursor(cursor);
    }

    public Map<String, Person> getPeopleByLookupKey(List<String> lookupKeys){

        Assert.assertNotNull(lookupKeys);

        if ( lookupKeys.isEmpty() ) {
            return null;
        }

        Map<String, Person> map = new HashMap<>();
        String[] values = new String[lookupKeys.size()];

        StringBuilder whereStringBuilder = new StringBuilder();
        whereStringBuilder.append( PersonContract.LOOKUP_KEY );
        whereStringBuilder.append(" in (" );

        int index = 0;
        String separator = "";
        for ( String key: lookupKeys ){
            whereStringBuilder.append( separator );
            whereStringBuilder.append( "?" );
            separator = ",";
            values[index] = key;
            ++index;
        }

        whereStringBuilder.append(" )");

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(PersonContract.TABLE_NAME,
                null,
                whereStringBuilder.toString(),
                values,
                null,
                null,
                null );

        if ( cursor == null || cursor.getCount() == 0 ) {
            return null;
        }

        while( cursor.moveToNext() ){
            Person person = getPersonFromCursor(cursor);
            map.put(person.getLookupKey(), person);
        }

        return map;
    }

    public Person getPersonFromCursor(Cursor cursor){

        String id = cursor.getString( cursor.getColumnIndex( PersonContract._ID ) );
        String name = cursor.getString( cursor.getColumnIndex( PersonContract.DISPLAY_NAME ) );
        String photoUri = cursor.getString( cursor.getColumnIndex( PersonContract.PHOTO_URI ) );
        String phone = cursor.getString( cursor.getColumnIndex( PersonContract.TELEPHONE ) );
        String email = cursor.getString( cursor.getColumnIndex( PersonContract.EMAIL ) );
        String job = cursor.getString( cursor.getColumnIndex( PersonContract.JOB ) );
        String group = cursor.getString( cursor.getColumnIndex( PersonContract.GROUP ) );
        String notes = cursor.getString( cursor.getColumnIndex( PersonContract.NOTES ) );
        String address = cursor.getString( cursor.getColumnIndex( PersonContract.ADDRESS) );
        String churchPhotoUri = cursor.getString( cursor.getColumnIndex( PersonContract.CHURCH_PHOTO_URI ) );
        String church = cursor.getString( cursor.getColumnIndex( PersonContract.CHURCH) );
        String bloodType = cursor.getString( cursor.getColumnIndex( PersonContract.BLOOD_TYPE) );
        String company = cursor.getString( cursor.getColumnIndex( PersonContract.COMPANY) );
        String birthday = cursor.getString( cursor.getColumnIndex( PersonContract.BIRTHDAY) );
        String lookupKey = cursor.getString( cursor.getColumnIndex( PersonContract.LOOKUP_KEY) );

        return new Person(id,
                name,
                email,
                job,
                notes,
                phone,
                address,
                group,
                photoUri,
                church,
                churchPhotoUri,
                bloodType,
                company,
                birthday,
                lookupKey);
    }

    public boolean remove(List<Person> people){

      Log.i("TAG", "PersonDbHelper.Starting remove" );

        if ( people == null ){
            return false;
        }

        if ( people.isEmpty() ) {
            Log.i("TAG", "PersonDbHelper.Remove - People Empty" );
            return true;
        }

        SQLiteDatabase database = getWritableDatabase();

        StringBuilder whereCloseStringBuilder = new StringBuilder();

        whereCloseStringBuilder.append(PersonContract._ID);
        whereCloseStringBuilder.append(" in ( ");

        int size = people.size();
        for ( int i = 0; i < size; ++i){
            Person person = people.get(i);
            if ( person != null ) {
                if ( i < size - 1 ) {
                    whereCloseStringBuilder.append(String.valueOf(person.getId()));
                    whereCloseStringBuilder.append(",");
                }
                else
                {
                    whereCloseStringBuilder.append(String.valueOf(person.getId()));
                }
            }
        }

        whereCloseStringBuilder.append(" )");

        Log.i("TAG", "PersonDbHelper.Where do Delete: " + whereCloseStringBuilder.toString());

        int deletedRows = 0;
        try {
            deletedRows = database.delete(PersonContract.TABLE_NAME, whereCloseStringBuilder.toString(), null);
        }catch (Exception ex) {
            Log.e("TAG", ex.toString());
            return false;
        }
        finally {
            database.close();
        }

        Log.i("TAG", "PersonDbHelper.DeletedRows: " + deletedRows);

        return deletedRows != 0;
    }

    public boolean remove(Person person){

        ArrayList<Person> people = new ArrayList(1);
        people.add(person);

        return this.remove(people);
    }
}

