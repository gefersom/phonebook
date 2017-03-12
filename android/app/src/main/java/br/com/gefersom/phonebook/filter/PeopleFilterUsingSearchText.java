package br.com.gefersom.phonebook.filter;

import java.util.ArrayList;
import java.util.List;

import br.com.gefersom.phonebook.model.Person;


/**
 * Created by me on 02/10/2016.
 */


public class PeopleFilterUsingSearchText {

    private String constraint;

    public PeopleFilterUsingSearchText(){
        this.constraint = "";
    }

    public List<Person> filter(List<Person> peopleToFilter) {

        if (this.constraint == null || this.constraint.toString().isEmpty()) {
            return peopleToFilter;
        } else {
            String lowercaseContrainst = this.constraint.toString().toLowerCase();

            List<Person> filteredList = new ArrayList<Person>();
            for (int i = 0; i < peopleToFilter.size(); ++i) {
                Person person = peopleToFilter.get(i);
                boolean passed = person.getName().toLowerCase().contains(lowercaseContrainst);

                if (passed) {
                    filteredList.add(person);
                }
            }

            return filteredList;
        }
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }
}
