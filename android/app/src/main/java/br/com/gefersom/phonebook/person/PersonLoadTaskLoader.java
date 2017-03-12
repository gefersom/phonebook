package br.com.gefersom.phonebook.person;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import br.com.gefersom.phonebook.model.PeopleManager;
import br.com.gefersom.phonebook.model.Person;


/**
 * Created by me on 20/9/2016.
 */

public class PersonLoadTaskLoader<L> extends AsyncTaskLoader<Person> {

    private String personID;

    public PersonLoadTaskLoader(Context context, String personID) {
        super(context);
        this.personID = personID;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Person loadInBackground() {
        return PeopleManager.getInstance(getContext()).getPerson(personID);
    }
}
