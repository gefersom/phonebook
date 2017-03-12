package br.com.gefersom.phonebook.person;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;


import br.com.gefersom.phonebook.R;
import br.com.gefersom.phonebook.model.Person;
import br.com.gefersom.phonebook.model.PersonContract;
import br.com.gefersom.phonebook.util.ImageUtil;
import butterknife.ButterKnife;

public class PersonEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Person>{

    private String TAG = PersonEditorActivity.class.getSimpleName();

    private int PERSON_LOADER_ID = 0;

    private ImageView photo;
    private EditText personName;
    private EditText phoneNumber;
    private EditText email;
    private EditText address;
    private EditText job;
    private EditText birthday;
    private EditText company;
    private EditText group;
    private EditText church;
    private EditText bloodType;

    private boolean editingMode;
    private Person person;
    private String personID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_detail_editable);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        this.personID = intent.getStringExtra(PersonContract._ID);

        this.photo = (ImageView) findViewById(R.id.photo);
        this.personName = (EditText) findViewById(R.id.person_name);
        this.phoneNumber = (EditText) findViewById(R.id.phone_number);
        this.email = (EditText) findViewById(R.id.email);
        this.address  = (EditText) findViewById(R.id.address);
        this.job = (EditText) findViewById(R.id.job);
        this.birthday = (EditText) findViewById(R.id.birthday);
        this.company = (EditText) findViewById(R.id.company);
        this.group = (EditText) findViewById(R.id.group);
        this.church = (EditText) findViewById(R.id.church);
        this.bloodType = (EditText) findViewById(R.id.blood_type);

        this.editingMode = (personID != null && !personID.isEmpty());

        if ( this.editingMode ){
            getSupportLoaderManager().restartLoader(PERSON_LOADER_ID, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_person_editor, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_save ) {

            if ( this.personName.getText().toString().isEmpty() ){
                this.personName.setError("Preencha o nome da pessoa");
                this.personName.setFocusableInTouchMode(true);
                this.personName.requestFocus();
                return true;
            }

            if ( this.editingMode == false ) {

                Person person = new Person("",
                        this.personName.getText().toString(),
                        this.email.getText().toString(),
                        this.job.getText().toString(),
                        "",
                        this.phoneNumber.getText().toString(),
                        this.address.getText().toString(),
                        this.group.getText().toString(),
                        "",
                        this.church.getText().toString(),
                        "",
                        this.bloodType.getText().toString(),
                        this.company.getText().toString(),
                        this.birthday.getText().toString(),
                        Person.NO_LOOKUP_KEY
                );

                List<Person> list = new ArrayList<Person>();
                list.add(person);

                new WritePeopleAsyncTask(this).execute(list);
                return true;
            }
            else {
                Assert.assertNotNull(this.person);

                this.person.setName(this.personName.getText().toString());
                this.person.setPhone(this.phoneNumber.getText().toString());
                this.person.setEmail(this.email.getText().toString());
                this.person.setAddress(this.address.getText().toString());
                this.person.setJob(this.job.getText().toString());
                this.person.setBirthday(this.birthday.getText().toString());
                this.person.setCompany(this.company.getText().toString());
                this.person.setGroup(this.group.getText().toString());
                this.person.setChurch(this.church.getText().toString());
                this.person.setBloodType(this.bloodType.getText().toString());

                List<Person> list = new ArrayList<>();
                list.add(person);

                new PeopleUpdateAsyncTask(this).execute(list);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void setPersonSaved(boolean isSaved) {
        setResult(isSaved ? AppCompatActivity.RESULT_OK : AppCompatActivity.RESULT_CANCELED);
        this.finish();
    }

    @Override
    public Loader<Person> onCreateLoader(int id, Bundle args) {
        return new PersonLoadTaskLoader<Person>(this, this.personID);
    }

    @Override
    public void onLoadFinished(Loader<Person> loader, Person person) {

        if ( person != null ){
            this.person = person;
            this.personName.setText(person.getName());
            this.phoneNumber.setText(person.getPhone());
            this.email.setText(person.getEmail());
            this.address.setText(person.getAddress());
            this.job.setText(person.getJob());
            this.birthday.setText(person.getBirthday());
            this.company.setText(person.getCompany());
            this.group.setText(person.getGroup());
            this.church.setText(person.getGroup());
            this.bloodType.setText(person.getBloodType());

            ImageUtil.loadBitmapOrUseAFakeBitmap(person.getPhotoUri(), this.photo, personID);
        }
    }

    @Override
    public void onLoaderReset(Loader<Person> loader) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        getSupportLoaderManager().destroyLoader(PERSON_LOADER_ID);
    }
}
