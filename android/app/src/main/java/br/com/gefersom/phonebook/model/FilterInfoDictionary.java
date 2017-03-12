package br.com.gefersom.phonebook.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.gefersom.phonebook.R;


/**
 * Created by me on 24/9/2016.
 */

public class FilterInfoDictionary {

    private FilterInfo bloodTypeFilter;
    private FilterInfo addressFilter;
    private FilterInfo groupFilter;
    private FilterInfo jobFilter;
    private List<FilterInfo> filterInfoList;
    private Map<String, FilterInfo> map;
    private static FilterInfoDictionary instance = null;


    static public FilterInfoDictionary getInstance(){
        if ( instance == null ){
            instance = new FilterInfoDictionary();
        }

        return instance;
    }

    private FilterInfoDictionary(){
        this.filterInfoList = new ArrayList<>();

        this.addressFilter  = new FilterInfo(R.drawable.ic_place_black_24dp,  "Endereço", "");
        this.jobFilter      = new FilterInfo(R.drawable.ic_job_black_24dp,    "Profissão", "");
        this.groupFilter    = new FilterInfo(R.drawable.ic_group_black_24dp,  "Grupo", "");
        this.bloodTypeFilter= new FilterInfo(R.drawable.ic_blood_bag_24dp,    "Tipo sanguíneo", "");

        this.map = new HashMap<>(3);

        map.put(this.addressFilter.getTitle(), this.addressFilter);
        map.put(this.jobFilter.getTitle(), this.jobFilter);
        map.put(this.groupFilter.getTitle(), this.groupFilter);
        map.put(this.bloodTypeFilter.getTitle(), this.bloodTypeFilter);

        filterInfoList.addAll( map.values() );
    }

    public FilterInfo getAddressFilter() {
        return addressFilter;
    }

    public FilterInfo getGroupFilter() {
        return groupFilter;
    }

    public FilterInfo getJobFilter() {
        return jobFilter;
    }

    public FilterInfo getBloodTypeFilter() {
        return bloodTypeFilter;
    }

    public FilterInfo getFilterInfo(int index){
        return filterInfoList.get(index);
    }

    public FilterInfo getFilterInfo(String title){
        return map.get(title);
    }

    public void changeFilterValue(String filterTitle, String newValue){
        FilterInfo filterInfo = map.get(filterTitle);

        assert filterInfo != null;
        filterInfo.setValue(newValue);
    }

    public int getSize(){
        return map.size();
    }






}
