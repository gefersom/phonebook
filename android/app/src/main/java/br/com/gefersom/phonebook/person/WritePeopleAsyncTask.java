package br.com.gefersom.phonebook.person;

import android.os.AsyncTask;

import junit.framework.Assert;

import java.util.List;

import br.com.gefersom.phonebook.model.PeopleManager;
import br.com.gefersom.phonebook.model.Person;


/**
 * Created by me on 29/09/2016.
 */


public class WritePeopleAsyncTask extends AsyncTask< List<Person>, Void, Boolean> {

    private final PersonEditorActivity personEditorActivity;

    public WritePeopleAsyncTask(PersonEditorActivity personEditorActivity) {
        this.personEditorActivity = personEditorActivity;
    }

    @Override
    protected Boolean doInBackground(List<Person>... params) {

        List<Person> people = params[0];
        Assert.assertNotNull(people);

        return PeopleManager.getInstance(personEditorActivity).add(people);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        this.personEditorActivity.setPersonSaved(result.booleanValue());
    }
}