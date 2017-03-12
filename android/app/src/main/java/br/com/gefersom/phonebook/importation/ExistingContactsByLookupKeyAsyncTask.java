package br.com.gefersom.phonebook.importation;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.gefersom.phonebook.model.Person;

/**
 * Created by me on 01/10/2016.
 */

public class ExistingContactsByLookupKeyAsyncTask extends AsyncTask<List<Person>, Void, Map<String, Person>> {

    @Override
    protected Map<String, Person> doInBackground(List<Person>... params) {

        if (params.length == 0){
            return null;
        }

        List<Person> people = params[0];
        if ( people == null ){
            return null;
        }

        Map<String, Person> map = new HashMap<>(people.size());

        for (int i=0; i < people.size(); ++i){
            Person person = people.get(i);
            map.put(person.getLookupKey(), person);
        }

        return map;
    }
}
