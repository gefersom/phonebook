package br.com.gefersom.phonebook.filter;

import android.os.AsyncTask;

import java.util.List;

import br.com.gefersom.phonebook.MainActivity;
import br.com.gefersom.phonebook.model.Person;

/**
 * Created by me on 24/9/2016.
 */

public class PeopleFilterExecutor extends AsyncTask<Void, Void, List<Person>> {

    private final List<Person> peopleToFilter;
    private final MainActivity mainActivity;
    private final PeopleFilterUsingFilterInfo peopleFilterUsingFilterInfo;
    private final PeopleFilterUsingSearchText peopleFilterUsingSearchText;

    public PeopleFilterExecutor(MainActivity mainActivity,
                                List<Person> peopleToFilter,
                                PeopleFilterUsingFilterInfo peopleFilterUsingFilterInfo,
                                PeopleFilterUsingSearchText peopleFilterUsingSearchText) {
        this.peopleToFilter = peopleToFilter;
        this.mainActivity = mainActivity;
        this.peopleFilterUsingFilterInfo = peopleFilterUsingFilterInfo;
        this.peopleFilterUsingSearchText = peopleFilterUsingSearchText;
    }

    @Override
    protected List<Person> doInBackground(Void... params) {

        List<Person> filteredPeople = peopleToFilter;

        if ( peopleFilterUsingSearchText != null ) {
            filteredPeople = peopleFilterUsingSearchText.filter(filteredPeople);
        }

        if ( peopleFilterUsingFilterInfo != null ){
            filteredPeople = peopleFilterUsingFilterInfo.filter(filteredPeople);
        }

        return filteredPeople;
    }

    @Override
    protected void onPostExecute(List<Person> people) {
        super.onPostExecute(people);

        mainActivity.setFilteredPeople(people);
    }
}
