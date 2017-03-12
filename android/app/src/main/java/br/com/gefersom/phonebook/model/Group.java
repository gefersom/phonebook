package br.com.gefersom.phonebook.model;

import java.util.ArrayList;
import java.util.List;

import br.com.gefersom.phonebook.R;


/**
 * Created by me on 3/9/2016.
 */
public class Group {
    private String name;
    private List<Person> personList;
    private int imageId;

    public Group(String name) {
        this.name = name;
        personList = new ArrayList<>();
        imageId = R.drawable.ic_group_black_48dp;
    }

    public Group(String name, List<Person> personList, int imageId) {
        this.personList = personList;
        this.name = name;
        this.imageId = imageId;
    }

    public void addContact(Person person){
        this.personList.add(person);
    }

    public int size(){
        return this.personList.size();
    }

    public Person getContact(int index){
        return this.personList.get(index);
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
