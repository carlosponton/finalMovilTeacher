package com.clases.carlosponton.tutorteacher;

public class Student {
    private String id;
    private String email;
    private String name;
    private String sex;
    private int city;
    private String uri;
    public Student(String id,String email,String name, String sex, int city, String uri){
        this.email = email;
        this.name = name;
        this.sex = sex;
        this.city = city;
        this.id = id;
        this.uri = uri;
    }

    public Student(){

    }

    public Student(String id){
        this.setId(id);
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

