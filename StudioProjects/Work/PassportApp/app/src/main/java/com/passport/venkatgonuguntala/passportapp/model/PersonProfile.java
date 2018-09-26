package com.passport.venkatgonuguntala.passportapp.model;

/**
 * Created by venkatgonuguntala on 9/19/18.
 */

public class PersonProfile {

    private String id;
    private String name;
    private String age;
    private String hobie;
    private String gender;
    private String image;

    public PersonProfile() {

    }

    public PersonProfile(String id, String name, String age, String hobie, String gender, String image) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.hobie = hobie;
        this.gender = gender;
        this.image = image;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getHobie() {
        return hobie;
    }

    public String getGender() {
        return gender;
    }

    public String getImage() {
        return image;
    }
}
