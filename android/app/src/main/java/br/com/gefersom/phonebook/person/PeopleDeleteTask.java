package br.com.gefersom.phonebook.person;

import android.os.AsyncTask;

import java.util.List;

import br.com.gefersom.phonebook.MainActivity;
import br.com.gefersom.phonebook.model.PeopleManager;
import br.com.gefersom.phonebook.model.Person;

/**
 * Created by me on 23/9/2016.
 */
public class PeopleDeleteTask<L> extends AsyncTask<Void, Void, Boolean> {

    private final List<Person> people;
    private MainActivity mainActivity;

    public PeopleDeleteTask(MainActivity context, List<Person> people) {
        this.mainActivity = context;
        this.people = people;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return PeopleManager.getInstance(mainActivity).removePeople(people);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        mainActivity.startLoaderPersonInfo();
    }
}