package br.com.gefersom.phonebook.model;

import android.content.Context;
import android.util.Log;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by me on 01/10/2016.
 */

public class PeopleManager {
    private Map<String, Person> androidLookupKeyAndPersonMap;
    private Map<String, Person> idAndPersonMap;
    private List<Person> people;
    private final String TAG = PeopleManager.class.getSimpleName();

    private static PeopleManager instance;
    private Context context;

    static public PeopleManager getInstance(Context context) {
        if (instance == null) {
            instance = new PeopleManager();
        }

        instance.context = context;
        return instance;
    }

    private PeopleManager() {
        this.context = null;
        this.idAndPersonMap = new HashMap<String, Person>();
        this.androidLookupKeyAndPersonMap = new HashMap<String, Person>();
    }

    synchronized private void startIndexing(){

        this.androidLookupKeyAndPersonMap.clear();

        for ( Person person: this.idAndPersonMap.values() ){
            String lookupKey = person.getLookupKey();
            if ( !lookupKey.isEmpty() ) {
                this.androidLookupKeyAndPersonMap.put(lookupKey, person);
            }
        }
    }

    synchronized public List<Person> getPeople() {
        if ( this.isEmpty() ) {
            PersonDbHelper helper = new PersonDbHelper(PeopleManager.this.context);

            this.people = helper.getPeople();

            if ( people == null ){
                return null;
            }

            for( Person person: people ){
                this.idAndPersonMap.put(person.getId(), person);
            }

            this.startIndexing();
        }

        return this.people;
    }

    synchronized  public List<Person> reloadPeople() {
        clear();
        return getPeople();
    }

    synchronized  public void clear() {
        Log.i("TAG", "PeopleManager.clear");
        this.androidLookupKeyAndPersonMap.clear();
        this.idAndPersonMap.clear();
        this.people = null;
    }

    synchronized private boolean isEmpty(){
        return this.idAndPersonMap.isEmpty();
    }

    synchronized public Person getPersonByLookupKey(String lookupKey) {
        Assert.assertNotNull(lookupKey);
        return this.androidLookupKeyAndPersonMap.get(lookupKey);
    }

    synchronized public Person getPerson(String personId) {
        Assert.assertNotNull(personId);

        if ( getPeople() == null ) {
            return null;
        }

        return idAndPersonMap.get(personId);
    }


    synchronized public boolean removePeople(List<Person> peopleToRemove) {

        Assert.assertNotNull(peopleToRemove);

        PersonDbHelper helper = new PersonDbHelper(PeopleManager.this.context);

        boolean ret = helper.remove(peopleToRemove);

        Log.i("TAG", "PeopleManager.removePeople: " + ret );

        if (ret) {
           reloadPeople();
        }

        return ret;
    }

    synchronized public boolean removePerson(Person person) {
        Assert.assertNotNull(person);
        List<Person> list = new ArrayList<>();
        list.add(person);
        return this.removePeople(list);
    }

    synchronized public boolean updatePeople(List<Person> people) {

        Assert.assertNotNull(people);
        if ( isEmpty() ){
            return false;
        }

        PersonDbHelper helper = new PersonDbHelper(PeopleManager.this.context);
        boolean success = helper.update(people);

        if ( success ){
            startIndexing();
        }

        return success;
    }

    synchronized public boolean updatePeople(Person person) {
        Assert.assertNotNull(person);
        List<Person> list = new ArrayList<>(1);
        list.add(person);
        return this.updatePeople(list);
    }

    synchronized public boolean add(List<Person> people){
        Assert.assertNotNull(people);

        PersonDbHelper helper = new PersonDbHelper(PeopleManager.this.context);

        boolean ret = helper.insert(people);

        if (ret) {
            this.clear();
            this.getPeople();
            this.startIndexing();
        }

        return ret;
    }

    synchronized public boolean add(Person person){
        Assert.assertNotNull(person);

        List<Person> list = new ArrayList<Person>(1);
        return this.add(list);
    }

}
