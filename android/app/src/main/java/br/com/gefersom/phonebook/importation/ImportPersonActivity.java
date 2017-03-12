package br.com.gefersom.phonebook.importation;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.List;

import br.com.gefersom.phonebook.R;
import br.com.gefersom.phonebook.model.PeopleManager;
import br.com.gefersom.phonebook.model.Person;
import br.com.gefersom.phonebook.util.PermissionUtil;


public class ImportPersonActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {

    private final int CONTACT_PREVIEW_LOADER_ID = 1;
    private final int CONTACTS_WRITER_LOADER_ID = 2;
    private ListView listView;
    private ImportPersonAdapter adapter;
    public final String TAG = ImportPersonActivity.class.getName();
    private View mLayout;
    private TextView numberOfPeopleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_contact);
        listView = (ListView) findViewById(R.id.list);
        mLayout = findViewById(R.id.main_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        numberOfPeopleTextView = (TextView) findViewById(R.id.number_of_people_toolbar);
        assert numberOfPeopleTextView != null;

        final ImportPersonActivity activity = this;
        Button importContactButton = (Button) findViewById(R.id.import_contact);

        importContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportLoaderManager().initLoader(CONTACTS_WRITER_LOADER_ID,
                        null,
                        activity);
            }
        });

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Importar contatos");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestContactsPermissions();
    }

    private void requestContactsPermissions() {

        if ( ImportAndroidContactUtil.requestReadAdnroidContactPermision(this,
                ImportAndroidContactUtil.PERMISSIONS_CONTACT,
                CONTACT_PREVIEW_LOADER_ID) )
        {
            getSupportLoaderManager().initLoader(CONTACT_PREVIEW_LOADER_ID,
                    null,
                    this );
        }
    }


    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case CONTACT_PREVIEW_LOADER_ID: {

                if (PermissionUtil.verifyPermissions(grantResults)) {
                    getSupportLoaderManager().initLoader(CONTACT_PREVIEW_LOADER_ID,
                            null,
                            this);
                } else {
                    Log.i(TAG, "Contacts permissions were NOT granted.");
                    Snackbar.make(mLayout, R.string.permissions_not_granted,
                            Snackbar.LENGTH_SHORT)
                            .show();
                }

            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    public void setDataImported(boolean isImported){
        setResult(isImported? AppCompatActivity.RESULT_OK: AppCompatActivity.RESULT_CANCELED);
        this.finish();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        switch ( id ) {
            case CONTACT_PREVIEW_LOADER_ID:{
                return ImportAndroidContactUtil.createPersonCursorLoader(this);
            }
            case CONTACTS_WRITER_LOADER_ID: {
                return ImportAndroidContactUtil.createWritePersonContactAsyncTask(this, this.adapter.getSelectedItems());
            }
        }


        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        switch ( loader.getId() ) {
            case CONTACT_PREVIEW_LOADER_ID: {
                Cursor cursor = (Cursor) data;
                assert cursor != null;
                cursor.moveToFirst();

                List<Person> people = new ArrayList<>();

                PeopleManager peopleManager = PeopleManager.getInstance(this);
                while( cursor.moveToNext() ) {
                    Person person = ImportAndroidContactUtil.getPersonFrom(cursor);
                    Person importedPerson = peopleManager.getPersonByLookupKey(person.getLookupKey());
                    if (importedPerson == null){
                        people.add(person);
                    }
                }

                this.adapter = new ImportPersonAdapter(this, people);

                listView.setAdapter( this.adapter );

                assert numberOfPeopleTextView != null;

                setSelectedNumberOfPeople(0);
            }
            break;
            case CONTACTS_WRITER_LOADER_ID:{
                Boolean bool = (Boolean) data;
                assert bool != null;
                setDataImported(bool.booleanValue());
            }break;
        }
    }

    public void setSelectedNumberOfPeople(int numberOfSelectedPeople){
        numberOfPeopleTextView.setText( adapter.getCount() + " contato(s) - " + numberOfSelectedPeople + " selecionados(s)" );
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        getSupportLoaderManager().destroyLoader(CONTACT_PREVIEW_LOADER_ID);
        getSupportLoaderManager().destroyLoader(CONTACTS_WRITER_LOADER_ID);
    }

    /********************************************************************************************/




}

