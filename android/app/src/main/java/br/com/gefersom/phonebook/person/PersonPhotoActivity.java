package br.com.gefersom.phonebook.person;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import junit.framework.Assert;

import br.com.gefersom.phonebook.R;
import br.com.gefersom.phonebook.util.ImageUtil;

public class PersonPhotoActivity extends AppCompatActivity
{
    public static final String PERSON_NAME = "personName";
    public static final String PHOTO_URI = "photo_uri";
    public static final String PERSON_ID = "person_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_photo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        assert intent != null;

        String name = intent.getStringExtra(PERSON_NAME);
        String photoUri = intent.getStringExtra(PHOTO_URI);
        String personID = intent.getStringExtra(PERSON_ID);

        Assert.assertNotNull(personID);
        Assert.assertNotNull(photoUri);
        Assert.assertNotNull(name);

        Assert.assertFalse(personID.isEmpty());
        Assert.assertFalse(personID.isEmpty());

        ImageView imageView = (ImageView) findViewById(R.id.photo);

        ImageUtil.loadBitmapOrUseAFakeBitmap(photoUri, imageView, personID);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(name);
    }
};