package br.com.gefersom.phonebook;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.com.gefersom.phonebook.filter.FilterActivity;
import br.com.gefersom.phonebook.filter.PeopleFilterExecutor;
import br.com.gefersom.phonebook.filter.PeopleFilterUsingFilterInfo;
import br.com.gefersom.phonebook.filter.PeopleFilterUsingSearchText;
import br.com.gefersom.phonebook.importation.ImportPersonActivity;
import br.com.gefersom.phonebook.model.Person;
import br.com.gefersom.phonebook.model.PersonContract;
import br.com.gefersom.phonebook.person.PeopleAdapter;
import br.com.gefersom.phonebook.person.PeopleDeleteTask;
import br.com.gefersom.phonebook.person.PeopleLoadTask;
import br.com.gefersom.phonebook.person.PersonDetailActivity;
import br.com.gefersom.phonebook.person.PersonEditorActivity;


public class MainActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener {

    private final int IMPORT_CONTACTS_ACTIVITY_RESULT = 0;
    private final int FILTER_CONTACTS_ACTIVITY_RESULT = 1;
    private final int LOAD_DATABASE_CONTACTS_LOADER_ID = 2;
    private final int PERSON_EDITOR_ACTIVITY_RESULT = 3;

    private final String TAG = MainActivity.class.getSimpleName();
    private PeopleAdapter peopleAdapter;
    private TextView numberOfPeopleTextView;
    private ListView listView;
    private Menu menu;
    private View filterLayout;
    private DataSetObserver dataSetObserver;
    private List<Person> filteredPeopleUsingFilterActivity;
    private List<Person> peopleListWithoutFiltering;
    private String currentFilteringText;


    private PeopleFilterUsingFilterInfo peopleFilterUsingFilterInfo;
    private PeopleFilterUsingSearchText peopleFilterUsingSearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(this);

        View actionEditFilterView = findViewById(R.id.ac_edit_filter);
        actionEditFilterView.setOnClickListener(buildOnClickListener());

        this.listView = (ListView) findViewById(R.id.list);
        this.listView.setOnItemClickListener(this.buildOnItemClickListener());

        this.numberOfPeopleTextView = (TextView) findViewById(R.id.number_of_people_toolbar);

        ImageButton removeFilterButton = (ImageButton) findViewById(R.id.ac_remove_filter);
        removeFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFilterView();
            }
        });

        this.filterLayout = findViewById(R.id.filter_layout);
        this.filterLayout.setVisibility(View.GONE);

        startLoaderPersonInfo();
        this.dataSetObserver = buildDataSetObserver();

        this.currentFilteringText = "";

        this.peopleFilterUsingFilterInfo = new PeopleFilterUsingFilterInfo();
        this.peopleFilterUsingSearchText = new PeopleFilterUsingSearchText();
    }


    private DataSetObserver buildDataSetObserver() {

        return new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                MainActivity.this.setNumberOfPeopleMessage(MainActivity.this.peopleAdapter.getCount());
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
            }
        };
    }

    public void startLoaderPersonInfo() {
        new PeopleLoadTask<List<Person>>(this).execute();
    }

    private void hideFilterView() {
        this.filterLayout.setVisibility(View.GONE);

        if (!this.isFilterBySearchActive()) {
            this.peopleAdapter.setPeopleList(peopleListWithoutFiltering);
            setNumberOfPeopleMessage(peopleAdapter.getCount());
        } else {
            this.peopleFilterUsingSearchText.setConstraint(this.currentFilteringText);

            PeopleFilterExecutor peopleFilterExecutor = new PeopleFilterExecutor(this,
                    this.peopleListWithoutFiltering,
                    null,
                    this.peopleFilterUsingSearchText
            );

            peopleFilterExecutor.execute();
        }
    }

    private boolean isFilterActive() {
        return filterLayout.getVisibility() == View.VISIBLE;
    }

    private boolean isFilterBySearchActive() {
        return !this.currentFilteringText.isEmpty();
    }

    private AdapterView.OnItemClickListener buildOnItemClickListener() {
        final Context context = this;
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                assert peopleAdapter != null;

                Intent intent = new Intent(context, PersonDetailActivity.class);
                intent.putExtra(PersonContract._ID, peopleAdapter.getItem(i).getId());

                startActivityForResult(intent, PERSON_EDITOR_ACTIVITY_RESULT);
            }
        };
    }

    View.OnClickListener buildOnClickListener() {
        final Context context = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, FilterActivity.class));
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        getSupportLoaderManager().destroyLoader(LOAD_DATABASE_CONTACTS_LOADER_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        this.menu = menu;

        verifyMenuVisibility();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_filter) {
            startActivityForResult(new Intent(this, FilterActivity.class), FILTER_CONTACTS_ACTIVITY_RESULT);
            return true;
        }

        if (id == R.id.action_remove) {
            this.peopleAdapter.setEnableRemoveButton(true);
            this.peopleAdapter.notifyDataSetChanged();
            this.menu.findItem(R.id.action_done).setVisible(true);
            this.menu.findItem(R.id.action_filter).setVisible(false);
            this.menu.findItem(R.id.action_add_person).setVisible(false);
            this.menu.findItem(R.id.action_import).setVisible(false);
            item.setVisible(false);
            return true;
        }

        if (id == R.id.action_done) {
            this.peopleAdapter.setEnableRemoveButton(false);
            this.peopleAdapter.notifyDataSetChanged();
            resetMenuItems();
            item.setVisible(false);

            List<Person> list = this.peopleAdapter.getSelectPeople();
            if (!list.isEmpty()) {
                new PeopleDeleteTask<Boolean>(this, list).execute();
            }

            return true;
        }

        if (id == R.id.action_import) {
            startActivityForResult(new Intent(this, ImportPersonActivity.class), IMPORT_CONTACTS_ACTIVITY_RESULT);
            return true;
        }

        if (id == R.id.action_remove_database) {
            new PeopleDeleteTask<Boolean>(this, peopleAdapter.getPeople()).execute();
        }

        if (id == R.id.action_add_person) {
            Intent intent = new Intent(this, PersonEditorActivity.class);
            intent.putExtra(PersonContract._ID, "");
            this.startActivityForResult(intent, PERSON_EDITOR_ACTIVITY_RESULT);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == PERSON_EDITOR_ACTIVITY_RESULT || requestCode == IMPORT_CONTACTS_ACTIVITY_RESULT)
                && resultCode == AppCompatActivity.RESULT_OK) {
            new PeopleLoadTask<List<Person>>(this).execute();
        }

        if (requestCode == FILTER_CONTACTS_ACTIVITY_RESULT && resultCode == AppCompatActivity.RESULT_OK) {
            executeFiltering();
        }
    }

    private void executeFiltering() {

        this.peopleFilterUsingSearchText.setConstraint(this.currentFilteringText);
        PeopleFilterExecutor peopleFilterExecutor = new PeopleFilterExecutor(this,
                this.peopleListWithoutFiltering,
                this.peopleFilterUsingFilterInfo,
                this.peopleFilterUsingSearchText
        );

        peopleFilterExecutor.execute();

        this.filterLayout.setVisibility(View.VISIBLE);
    }


    private void verifyMenuVisibility() {
        if (this.menu != null && this.peopleAdapter != null) {
            boolean enable = peopleAdapter.getCount() != 0;

            MenuItem filter = this.menu.findItem(R.id.action_filter);
            MenuItem remmove = this.menu.findItem(R.id.action_remove);

            if (filter != null && remmove != null) {
                filter.setVisible(enable);
                remmove.setVisible(enable);
            }
        }
    }

    public void setAdapter(List<Person> people) {
        this.peopleListWithoutFiltering = people;
        this.peopleAdapter = new PeopleAdapter(this, people);
        this.peopleAdapter.registerDataSetObserver(dataSetObserver);
        this.listView.setAdapter(peopleAdapter);
        setNumberOfPeopleMessage(peopleAdapter.getCount());

        verifyMenuVisibility();
    }

    public void setNumberOfPeopleMessage(int numberOfPeople) {
        this.numberOfPeopleTextView.setText(String.valueOf(numberOfPeople) + " pessoas(s)");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {

        MainActivity.this.currentFilteringText = newText;

        PeopleFilterUsingFilterInfo peopleFilterUsingFilterInfo = null;

        if (isFilterActive()) {
            peopleFilterUsingFilterInfo = this.peopleFilterUsingFilterInfo;
        }

        this.peopleFilterUsingSearchText.setConstraint(newText);

        PeopleFilterExecutor peopleFilterExecutor = new PeopleFilterExecutor(this,
                this.peopleListWithoutFiltering,
                peopleFilterUsingFilterInfo,
                this.peopleFilterUsingSearchText
        );

        peopleFilterExecutor.execute();

        return true;
    }

    public void setFilteredPeople(List<Person> filteredPeople) {
        this.filteredPeopleUsingFilterActivity = filteredPeople;
        this.peopleAdapter.setPeopleList(this.filteredPeopleUsingFilterActivity);
        this.setNumberOfPeopleMessage(filteredPeople.size());
        this.peopleAdapter.notifyDataSetChanged();
    }

    private void resetMenuItems(){
        this.menu.findItem(R.id.action_remove).setVisible(true);
        this.menu.findItem(R.id.action_filter).setVisible(true);
        this.menu.findItem(R.id.action_add_person).setVisible(true);
        this.menu.findItem(R.id.action_import).setVisible(true);
        this.menu.findItem(R.id.action_done).setVisible(false);
    }

    @Override
    public void onBackPressed() {
        if (this.peopleAdapter.isEnableRemoveButton()) {
            resetMenuItems();
            this.peopleAdapter.setEnableRemoveButton(false);
            this.peopleAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }
}
