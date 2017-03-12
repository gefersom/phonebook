package br.com.gefersom.phonebook.importation;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.gefersom.phonebook.R;
import br.com.gefersom.phonebook.model.PeopleManager;
import br.com.gefersom.phonebook.model.Person;
import br.com.gefersom.phonebook.util.ImageUtil;


/**
 * Created by me on 18/9/2016.
 */
public class ImportPersonAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    private static String TAG = ImportPersonAdapter.class.getSimpleName();

    private List<Person> people;
    private ImportPersonActivity importPersonActivity;
    private LayoutInflater inflater;
    private SparseBooleanArray checkStates;
    private int selectedPeopleCount;

    public ImportPersonAdapter(ImportPersonActivity importPersonActivity, List<Person> people) {
        this.importPersonActivity = importPersonActivity;
        this.inflater = (LayoutInflater) importPersonActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.checkStates = new SparseBooleanArray(people.size());
        this.selectedPeopleCount = 0;
        this.people = people;
    }

    @Override
    public int getCount() {
        Log.i(TAG, "ImportPersonAdapter: " + people.size());
        return people.size();
    }

    @Override
    public Object getItem(int position) {
        return getLookupKey(position);
    }

    private String getLookupKey(int position) {
        return people.get(position).getLookupKey();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.import_contacts_list_item, parent, false);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        Person person = people.get(position);
        holder.nameView.setText( person.getName());
        String imageUri = person.getPhotoUri();
        String lookupKey = person.getLookupKey();

        Person existingPerson = PeopleManager.getInstance(this.importPersonActivity).getPersonByLookupKey(lookupKey);
        boolean isEnabled = existingPerson == null;

        if (isEnabled) {
            holder.checkboxButton.setVisibility(View.VISIBLE);
        } else {
            holder.checkboxButton.setVisibility(View.INVISIBLE);
        }

        int colorId = R.color.grey_500;
        if (isEnabled) {
            colorId = android.R.color.black;
        }
        holder.nameView.setTextColor(holder.nameView.getResources().getColor(colorId));

        holder.checkboxButton.setTag(position);
        holder.checkboxButton.setOnCheckedChangeListener(null);
        holder.checkboxButton.setChecked(checkStates.get(position, false));
        holder.checkboxButton.setOnCheckedChangeListener(this);

        ImageUtil.loadBitmapOrUseAFakeBitmap(imageUri, holder.imageView, String.valueOf(position));

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        checkStates.put((Integer) compoundButton.getTag(), isChecked);

        if (isChecked) {
            ++this.selectedPeopleCount;
        } else {
            --this.selectedPeopleCount;
        }

        this.importPersonActivity.setSelectedNumberOfPeople(this.selectedPeopleCount);
    }


    public ArrayList<Person> getSelectedItems() {

        ArrayList<Person> arrayList = new ArrayList<Person>();
        for (int i = 0; i < checkStates.size(); ++i) {
            int key = checkStates.keyAt(i);
            boolean value = checkStates.get(key, false);
            if (value) {
                Person person = people.get(key);
                arrayList.add(person);
            }
        }

        for (Person p:arrayList) {
            this.people.remove(p);
        }

        return arrayList;
    }


    class Holder {

        public Holder(View root) {
            nameView = (TextView) root.findViewById(R.id.name);
            imageView = (ImageView) root.findViewById(R.id.image);
            checkboxButton = (CheckBox) root.findViewById(R.id.checkbox_button);
            originalBackground = root.getBackground();
        }

        final Drawable originalBackground;
        TextView nameView;
        ImageView imageView;
        CheckBox checkboxButton;
    }
}