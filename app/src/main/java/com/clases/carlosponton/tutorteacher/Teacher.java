package com.clases.carlosponton.tutorteacher;

import java.util.ArrayList;

/**
 * Created by carlosponton on 3/06/18.
 */

public class Teacher {
    private String id;
    private String email;
    private String name;
    private String sex;
    private int city;
    private String uri;
    private ArrayList<String> topics;

    public Teacher(String id, String email, String name, String sex, int city, String uri, ArrayList<String> topics){
        this.email = email;
        this.name = name;
        this.sex = sex;
        this.city = city;
        this.topics = topics;
        this.id = id;
        this.uri = uri;

    }

    public Teacher(){

    }

    public Teacher(String id){
        this.setId(id);
    }

    public ArrayList<String> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<String> topics) {
        this.topics = topics;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void save(){

        Datos.save(this);
    }

    public void edit(){
        Datos.edit(this);
    }

    public void delete(){
        Datos.delete(this);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
