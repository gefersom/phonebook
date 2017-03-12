package br.com.gefersom.phonebook.filter;

import java.util.ArrayList;
import java.util.List;

import br.com.gefersom.phonebook.model.FilterInfo;
import br.com.gefersom.phonebook.model.FilterInfoDictionary;
import br.com.gefersom.phonebook.model.Person;

/**
 * Created by me on 24/9/2016.
 */

public class PeopleFilterUsingFilterInfo {

    public PeopleFilterUsingFilterInfo(){
    }

    private boolean contains(String value, FilterInfo filterInfo) {
        String filterInfoValue = filterInfo.getValue().toLowerCase();
        return value.toLowerCase().contains(filterInfoValue);
    }

    public List<Person> filter(List<Person> peopleToFilter) {

        FilterInfoDictionary filterInfoDictionary = FilterInfoDictionary.getInstance();

        List<Person> list = new ArrayList<>();
        for (int i = 0; i < peopleToFilter.size(); ++i) {
            Person person = peopleToFilter.get(i);

            boolean passed = true;

            //Job
            String value = filterInfoDictionary.getJobFilter().getValue().toLowerCase();
            passed = passed && person.getJob().toLowerCase().contains(value);

            //Adress
            value = filterInfoDictionary.getAddressFilter().getValue().toLowerCase();
            passed = passed && person.getAddress().toLowerCase().contains(value);

            //group
            value = filterInfoDictionary.getGroupFilter().getValue().toLowerCase();
            passed = passed && person.getGroup().toLowerCase().contains(value);

            // Blood Type
            passed = passed && contains(person.getBloodType(), filterInfoDictionary.getBloodTypeFilter());

            if (passed) {
                list.add(person);
            }
        }
        return list;
    }
}
