package br.com.gefersom.phonebook.model;

import junit.framework.Assert;

/**
 * Created by me on 1/9/2016.
 */
public class Person {
    private String lookupKey;
    private String id;
    private String email;
    private String job;
    private String name;
    private String notes;
    private String phone;
    private String address;
    private String group;
    private String photoUri;
    private String churchPhotoUri;
    private String church;
    private String bloodType;
    private String company;
    private String birthday;

    public static final String NO_LOOKUP_KEY = "";

    public Person(String id,
                  String name,
                  String email,
                  String job,
                  String notes,
                  String phone,
                  String address,
                  String group,
                  String photoUri,
                  String church,
                  String churchPhotoUri,
                  String bloodType,
                  String company,
                  String birthday,
                  String lookupKey) {
        this.id = id;
        this.email = email;
        this.job = job;
        this.name = name;
        this.notes = notes;
        this.phone = phone;
        this.address = address;
        this.group = group;
        this.photoUri = photoUri;
        this.churchPhotoUri = churchPhotoUri;
        this.church = church;
        this.bloodType = bloodType;
        this.company = company;
        this.birthday = birthday;
        this.lookupKey = lookupKey;

        Assert.assertNotNull(id);
        Assert.assertNotNull(email);
        Assert.assertNotNull(job);
        Assert.assertNotNull(name);
        Assert.assertNotNull(notes);
        Assert.assertNotNull(phone);
        Assert.assertNotNull(address);
        Assert.assertNotNull(group);
        Assert.assertNotNull(photoUri);
        Assert.assertNotNull(churchPhotoUri);
        Assert.assertNotNull(church);
        Assert.assertNotNull(bloodType);
        Assert.assertNotNull(company);
        Assert.assertNotNull(birthday);
        Assert.assertNotNull(lookupKey);
    }

    public Person(String name,
                  String email,
                  String phone,
                  String group,
                  String photoUri,
                  String church,
                  String churchPhotoUri,
                  String bloodType) {
        this("", name, email, "", "", phone, "", group, photoUri, church, churchPhotoUri,bloodType, "", "", "");
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getJob() {
        return job;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getGroup() {
        return group;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public String getLookupKey() {
        return lookupKey;
    }

    public String getChurchPhotoUri() {
        return churchPhotoUri;
    }

    public String getChurch() {
        return church;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getCompany() {
        return company;
    }

    public String getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return "Person{" +
                "email='" + email + '\'' +
                ", job='" + job + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", group='" + group + '\'' +
                ", church='" + church + '\'' +
                '}';
    }

    public void setEmail(String email) {
        this.email = email;
        Assert.assertNotNull(this.email);
    }

    public void setJob(String job) {
        this.job = job;
        Assert.assertNotNull(this.job);
    }

    public void setName(String name) {
        this.name = name;
        Assert.assertNotNull(this.name);
    }

    public void setNotes(String notes) {
        this.notes = notes;
        Assert.assertNotNull(this.notes);
    }

    public void setPhone(String phone) {
        this.phone = phone;
        Assert.assertNotNull(this.phone);
    }

    public void setAddress(String address) {
        this.address = address;
        Assert.assertNotNull(this.address);
    }

    public void setGroup(String group) {
        this.group = group;
        Assert.assertNotNull(this.group);
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
        Assert.assertNotNull(this.photoUri);
    }

    public void setChurchPhotoUri(String churchPhotoUri) {
        this.churchPhotoUri = churchPhotoUri;
        Assert.assertNotNull(this.churchPhotoUri);
    }

    public void setChurch(String church) {
        this.church = church;
        Assert.assertNotNull(this.church);
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
        Assert.assertNotNull(this.bloodType);
    }

    public void setCompany(String company) {
        this.company = company;
        Assert.assertNotNull(this.company);
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
        Assert.assertNotNull(this.birthday);
    }
}
