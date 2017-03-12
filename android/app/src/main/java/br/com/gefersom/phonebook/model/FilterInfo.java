package br.com.gefersom.phonebook.model;

/**
 * Created by e8fr on 9/8/16.
 */
public class FilterInfo {

    private String title;
    private String value;
    private int imageId;

    public FilterInfo(int imageId, String title, String value) {
        this.title = title;
        this.value = value;
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public int getImageId() {
        return imageId;
    }

    public void setValue(String value){
        this.value = value;
    }
}
