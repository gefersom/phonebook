package br.com.gefersom.phonebook.importation;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import br.com.gefersom.phonebook.model.PeopleManager;
import br.com.gefersom.phonebook.model.Person;

/**
 * Created by e8fr on 9/20/16.
 */
public class WriteImportedPersonAsyncTask extends AsyncTaskLoader {

    private final String TAG = WriteImportedPersonAsyncTask.class.getSimpleName();
    private ArrayList<Person> people;
    private Map<String, String> groups;

    public WriteImportedPersonAsyncTask(Context context,
                                        ArrayList<Person> people) {
        super(context);
        this.people = people;
        this.groups = new HashMap<>();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Object loadInBackground() {

        this.fetchGroups();

        for (int i = 0; i < people.size(); ++i) {

            Person person = people.get(i);

            person.setAddress(retrieveAddress(person));
            person.setPhone(retrievePhone(person));
            person.setEmail(retrieveEmail(person));
            person.setGroup(retrieveGroup(person));
            person.setJob(retrieveJob(person));
            person.setNotes(retrieveNotes(person));
        }

        return PeopleManager.getInstance(getContext()).add(people);
    }

    private void fetchGroups() {

        Uri uri = ContactsContract.Groups.CONTENT_URI;

        String[] selectColumns = {
                ContactsContract.Groups._ID,
                ContactsContract.Groups.TITLE};

        Cursor c = getContext().getContentResolver().query(
                uri,
                selectColumns,
                null,
                null,
                null);

        groups = new Hashtable(c.getCount());

        try {

            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndex(ContactsContract.Groups._ID));
                String title = c.getString(c.getColumnIndex(ContactsContract.Groups.TITLE));

                groups.put(id, title);
            }
        } finally {
            c.close();
        }
    }

    private String retrievePhone(Person person) {

        String phone = "";

        Cursor cursor = this.getContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{person.getId()},
                null);


        if (cursor.moveToFirst())
            phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

        return phone;
    }

    private String retrieveEmail(Person person) {
        String email = "";

        Cursor cursor = this.getContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{person.getId()},
                null);

        if (cursor != null && cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            cursor.close();
        }

        return email;
    }

    public long getGroupIdFor(String contactId) {
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String where = String.format(
                "%s = ? AND %s = ?",
                ContactsContract.Contacts.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID);

        String[] whereParams = new String[]{
                ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
                contactId,
        };

        String[] selectColumns = new String[]{
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,
        };

        Cursor cursor = getContext().getContentResolver().query(
                uri,
                selectColumns,
                where,
                whereParams,
                null);
        try {
            //O primeiro Id do grupo pertence ao titulo "My Contacts",
            // todos os contatos estão nesse grupo
            if (cursor.moveToPosition(1)) {
                return cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID));
            }
            return Long.MIN_VALUE; // Has no group ...
        } finally {
            cursor.close();
        }
    }

    private String retrieveGroup(Person person) {
        long id = getGroupIdFor(person.getId());

        if (id == Long.MIN_VALUE) {
            return "";
        }

        return groups.get(Long.toString(id));
    }

    private String retrieveJob(Person person) {
        return "";
    }

    private String retrieveNotes(Person person) {

        String notes = "";

        String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";

        String[] noteWhereParams = new String[]{String.valueOf(person.getId()),
                ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};

        ContentResolver cr = this.getContext().getContentResolver();

        Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI,
                null,
                noteWhere,
                noteWhereParams,
                null);

        if (noteCur.moveToFirst()) {
            notes = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
        }
        noteCur.close();

        return notes;
    }


    private String retrieveAddress(Person person) {

        String address = "";

        Uri uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;

        String sortOrder = ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor cr = this.getContext().getContentResolver().query(uri,
                null,
                ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + person.getId(),
                null,
                sortOrder);

        if (cr.moveToFirst()){
            address = cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
        }

        if ( address == null || address.isEmpty() ){
            return "";
        }

        return address;
    }


}