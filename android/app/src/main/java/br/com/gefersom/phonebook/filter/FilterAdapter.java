package br.com.gefersom.phonebook.filter;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatImageView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.com.gefersom.phonebook.R;
import br.com.gefersom.phonebook.model.FilterInfo;
import br.com.gefersom.phonebook.model.FilterInfoDictionary;

/**
 * Created by me on 24/9/2016.
 */

public class FilterAdapter extends BaseAdapter {
    private FilterActivity filterActivity;
    private TextView currentViewWithFocus;

    private Map<Integer, String> map;

    public FilterAdapter(FilterActivity filterActivity) {
        this.filterActivity = filterActivity;
        this.map = new HashMap<>();
    }

    @Override
    public int getCount() {
        return FilterInfoDictionary.getInstance().getSize();
    }

    @Override
    public FilterInfo getItem(int i) {
        return FilterInfoDictionary.getInstance().getFilterInfo(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.filterActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.filter_item, parent, false);

            convertView.setTag(new Holder(convertView));
        }

        FilterInfo filterInfo = this.getItem(position);

        Holder holder = (Holder) convertView.getTag();
        holder.imageView.setImageResource(filterInfo.getImageId());
        holder.textInputLayout.setHint(filterInfo.getTitle());
        holder.textView.setText(filterInfo.getValue());
        holder.textView.setTag(position);
        holder.textView.setOnFocusChangeListener( buildOnFocusChangeListener() );


        return convertView;
    }

    View.OnFocusChangeListener buildOnFocusChangeListener(){

        return new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                TextView textView = (TextView) v;
                String value = textView.getText().toString();
                int position = (Integer) textView.getTag();

                if ( hasFocus ) {
                    map.put(position, value);
                    currentViewWithFocus = textView;
                }
                else{
                    currentViewWithFocus = textView;
                    if ( !value.isEmpty() ){
                        map.put(position, value);
                    }
                }
            }
        };
    }

    public void updateFilterInfoDictionaty(){

        Iterator entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            Object key = thisEntry.getKey();
            Object value = thisEntry.getValue();

            changeListFilterItem((Integer)key, (String) value);
        }

        //If the usre press home button the textview doesn't lose focus and the value insn't saved
        if ( currentViewWithFocus != null ) {
                int position = (Integer) currentViewWithFocus.getTag();
                changeListFilterItem(position, currentViewWithFocus.getText().toString());
        }

    }


    private void changeListFilterItem(int index, String newValue) {

        FilterInfo filterInfo = this.getItem(index);
        filterInfo.setValue(newValue);
    }

    class Holder {
        public AppCompatImageView imageView;
        public EditText textView;
        public TextInputLayout textInputLayout;

        public Holder(View rootView) {
            imageView = (AppCompatImageView) rootView.findViewById(R.id.image);
            textView = (EditText) rootView.findViewById(R.id.text);
            textInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout);

            textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    int position = (Integer)v.getTag();
                    String value = v.getText().toString();

                    changeListFilterItem(position, value);
                    return false;
                }
            });
        }
    }


}
