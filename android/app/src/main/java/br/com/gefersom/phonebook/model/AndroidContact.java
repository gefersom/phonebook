package br.com.gefersom.phonebook.model;

/**
 * Created by me on 19/9/2016.
 */
public class AndroidContact extends Person {

    private String TAG = AndroidContact.class.getSimpleName();
    public AndroidContact(String id,
                          String name,
                          String photoUri,
                          String bloodType,
                          String lookupKey) {
        super(id, name, "", "", "", "", "", "", photoUri, "", "", bloodType, "", "", lookupKey);
    }
}
