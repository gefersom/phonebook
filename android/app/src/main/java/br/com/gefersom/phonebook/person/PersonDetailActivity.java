package br.com.gefersom.phonebook.person;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import br.com.gefersom.phonebook.R;
import br.com.gefersom.phonebook.model.Person;
import br.com.gefersom.phonebook.model.PersonContract;
import br.com.gefersom.phonebook.util.ImageUtil;
import br.com.gefersom.phonebook.util.PrivateSharedPreferences;


public class PersonDetailActivity extends AppCompatActivity implements LoaderCallbacks<Person> {
    private final int PERSON_LOADER_TASK_ID = 0;
    private final int PERSON_EDITOR_ACTIVITY = 1;
    private String personID;
    private Person person;
    private String TAG = PersonDetailActivity.class.getSimpleName();
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_detail_preview);

        Intent intent = getIntent();

        if ( intent.hasExtra(PersonContract._ID) ) {
            personID = intent.getStringExtra(PersonContract._ID);
        }
        else
        {
            SharedPreferences sharedPreferences = getPrivatePreferences();
            personID = sharedPreferences.getString(PersonContract._ID, "");
        }

        assert personID != "";

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        this.imageLoader= ImageLoader.getInstance();
        this.imageLoader.init(config);

        getSupportLoaderManager().initLoader(PERSON_LOADER_TASK_ID, null, this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    protected  void onPause(){
        super.onPause();

        SharedPreferences sharedPreferences = getPrivatePreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PersonContract._ID, personID);
        editor.commit();
        editor.apply();
    }

    private SharedPreferences getPrivatePreferences(){
        return PrivateSharedPreferences.getPrivatePreferences(this, TAG);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_person_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_edit) {

            Intent intent = new Intent(this, PersonEditorActivity.class);
            intent.putExtra(PersonContract._ID, personID);

            this.startActivityForResult(intent, PERSON_EDITOR_ACTIVITY );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Person> onCreateLoader(int id, Bundle args) {

        if ( id == PERSON_LOADER_TASK_ID ) {
            return new PersonLoadTaskLoader(this, personID);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Person> loader, Person person) {
        if ( loader.getId() == PERSON_LOADER_TASK_ID && person != null ) {

            ImageView imageView  = (ImageView)findViewById(R.id.photo);
            ImageUtil.loadBitmapOrUseAFakeBitmap( person.getPhotoUri(), imageView, personID);
            Log.i("LoadBitmap.Detail", personID);
            imageView.setOnClickListener( buildOnClickListener() );


            TextView phoneInputEditText = (TextView)findViewById(R.id.phone_number);
            phoneInputEditText.setText(person.getPhone());

            TextView emailInputEditText = (TextView)findViewById(R.id.email);
            emailInputEditText.setText(person.getEmail());

            TextView groupText = (TextView)findViewById(R.id.group);
            groupText.setText(person.getGroup());

            if ( person.getChurchPhotoUri().isEmpty() == false ) {

                ImageView churchImage = (ImageView) findViewById(R.id.church_icon);
                ImageUtil.loadBitmap(person.getChurchPhotoUri(), churchImage, R.drawable.ic_church_black_24dp);
            }

            TextView addressText = (TextView)findViewById(R.id.address);
            addressText.setText(person.getAddress());

            TextView jobText = (TextView)findViewById(R.id.job);
            jobText.setText(person.getJob());

            TextView bloodTypeText = (TextView)findViewById(R.id.blood_type);
            bloodTypeText.setText(person.getBloodType());

            TextView birthdayTypeText = (TextView)findViewById(R.id.birthday);
            birthdayTypeText.setText(person.getBirthday());

            TextView companyTypeText = (TextView)findViewById(R.id.company);
            companyTypeText.setText(person.getCompany());

            TextView churchTypeText = (TextView)findViewById(R.id.church);
            churchTypeText.setText(person.getChurch());

            getSupportActionBar().setTitle(person.getName());

            this.person = person;
        }else{
            Toast.makeText(this.getApplicationContext(),"Erro ao carregar pessoa", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Person> loader) {
    }


    ImageView.OnClickListener buildOnClickListener(){

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonPhotoActivity.class );

                intent.putExtra(PersonPhotoActivity.PERSON_NAME, person.getName() );
                intent.putExtra(PersonPhotoActivity.PHOTO_URI, person.getPhotoUri());
                intent.putExtra(PersonPhotoActivity.PERSON_ID, personID);

                startActivity(intent);
            }
        };
    }

    private AppCompatActivity getActivity(){
        return this;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == PERSON_EDITOR_ACTIVITY && resultCode == AppCompatActivity.RESULT_OK )
        {
            setResult(AppCompatActivity.RESULT_OK);
            this.finish();
        }
    }
}
