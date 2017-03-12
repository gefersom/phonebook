package br.com.gefersom.phonebook.person;

import android.os.AsyncTask;

import java.util.List;

import br.com.gefersom.phonebook.MainActivity;
import br.com.gefersom.phonebook.model.PeopleManager;
import br.com.gefersom.phonebook.model.Person;

/**
 * Created by e8fr on 9/19/16.
 */

public class PeopleLoadTask<L> extends AsyncTask<Void, Void, List<Person>> {

    private MainActivity mainActivity;
    public PeopleLoadTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected List<Person> doInBackground(Void... params) {
        return PeopleManager.getInstance(mainActivity).getPeople();
    }

    @Override
    protected void onPostExecute(List<Person> people) {
        super.onPostExecute(people);

        mainActivity.setAdapter( people );
    }
}
