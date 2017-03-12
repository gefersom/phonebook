package br.com.gefersom.phonebook.person;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import br.com.gefersom.phonebook.MainActivity;
import br.com.gefersom.phonebook.R;
import br.com.gefersom.phonebook.model.Person;
import br.com.gefersom.phonebook.util.ImageUtil;

/**
 * Created by me on 1/9/2016.
 */
public class PeopleAdapter extends BaseAdapter
        implements CompoundButton.OnCheckedChangeListener,
        Filterable {

    private MainActivity mainActivity;
    private List<Person> people;
    private boolean enableRemoveButton;
    private SparseBooleanArray checkStates;
    private SparseBooleanArray visibleStates;
    private TextSearchFilter textSearchFilter;


    public PeopleAdapter(MainActivity context, List<Person> people) {
        this.mainActivity = context;
        this.people = people;
        this.enableRemoveButton = false;
        this.checkStates = new SparseBooleanArray(people.size());
        this.visibleStates = new SparseBooleanArray(people.size());
        this.textSearchFilter = new TextSearchFilter();
    }

    public void setEnableRemoveButton(boolean value) {
        this.enableRemoveButton = value;
    }

    public boolean isEnableRemoveButton(){
        return this.enableRemoveButton;
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Person getItem(int i) {
        return people.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.person_preview_with_delete, parent, false);

            convertView.setTag(new ViewHolder(convertView));
        }

        Person person = people.get(position);

        ViewHolder holder = (ViewHolder) convertView.getTag();

        ImageUtil.loadImageInBackground(holder.photoView, person.getPhotoUri(), person.getId());

        holder.nameView.setText(person.getName());

        holder.removeButton.setTag(position);
        holder.removeButton.setChecked(checkStates.get(position, false));
        holder.removeButton.setOnCheckedChangeListener(this);
        holder.removeButton.setVisibility(this.enableRemoveButton ? View.VISIBLE : View.INVISIBLE);

        return convertView;
    }

    public List<Person> getSelectPeople() {
        ArrayList<Person> arrayList = new ArrayList<Person>();
        for (int i = 0; i < this.checkStates.size(); ++i) {
            int key = this.checkStates.keyAt(i);
            boolean value = this.checkStates.get(key, false);
            if (value) {
                arrayList.add(this.people.get(key));
            }
        }

        return arrayList;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        this.checkStates.put((Integer) compoundButton.getTag(), isChecked);
    }

    public void setVisible(int itemIndex, boolean isVisible) {
        this.visibleStates.put(itemIndex, isVisible);
    }

    public void restoreVisibility() {
        this.visibleStates.clear();
    }

    public List<Person> getPeople() {
        return this.people;
    }

    @Override
    public Filter getFilter() {
        return PeopleAdapter.this.textSearchFilter;
    }

    private class TextSearchFilter extends Filter {

        List<Person> listBeforeFiltering = new ArrayList<>();

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();

            if (constraint == null || constraint.toString().isEmpty()) {
                result.values = listBeforeFiltering;
                result.count = 0;
            } else {

                listBeforeFiltering = PeopleAdapter.this.people;

                String lowercaseContrainst = constraint.toString().toLowerCase();

                for( int i =0; i < visibleStates.size(); ++i ){

                    Integer key = visibleStates.keyAt(i);
                    Person person = people.get(key);
                    //Log.i("TAG", "PeopleAdapter.perfomFiltering - " + person.getName() + ": " + visibleStates.get(i) );
                }

                List<Person> toFilter = PeopleAdapter.this.people;

                List<Person> filteredList = new ArrayList<Person>();
                for (int i = 0; i < toFilter.size(); ++i) {
                    Person person = toFilter.get(i);
                    boolean visible = person.getName().toLowerCase().contains(lowercaseContrainst);

                    visible = visible && visibleStates.get(i, true);

                    if (visible) {
                        filteredList.add(person);
                    }
                }
                result.values = filteredList;
                result.count = filteredList.size();
            }

            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            List<Person> list = (ArrayList<Person>) results.values;

            people = list;

            Assert.assertNotNull((ArrayList<Person>) results.values);

            notifyDataSetChanged();
        }
    }

    public void setPeopleList(List<Person> list){
        this.people = list;
        this.notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView photoView;
        TextView nameView;
        CheckBox removeButton;

        ViewHolder(View view) {
            photoView = (ImageView) view.findViewById(R.id.image);
            nameView = (TextView) view.findViewById(R.id.name);
            removeButton = (CheckBox) view.findViewById(R.id.remove_button);
        }
    }

    ;
}
