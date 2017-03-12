package br.com.gefersom.phonebook.filter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.view.Menu;
import android.view.MenuItem;

import br.com.gefersom.phonebook.R;

public class FilterActivity extends AppCompatActivity {

    private FilterAdapter filterAdapter;
    private final String TAG = FilterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ListViewCompat listViewCompat = (ListViewCompat) findViewById(R.id.list);

        filterAdapter = new FilterAdapter(this);
        listViewCompat.setAdapter(filterAdapter);
    }

    private void applyFilter(){
        this.setResult(AppCompatActivity.RESULT_OK);
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        filterAdapter.updateFilterInfoDictionaty();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_filter, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ( id == R.id.action_apply ) {
            applyFilter();
            return true;
        }

        return false;
    }
}