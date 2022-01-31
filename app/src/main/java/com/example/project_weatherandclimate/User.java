package com.example.project_weatherandclimate;

import java.util.Optional;

public final class User {

    private Optional<String> firstName = Optional.ofNullable(null);
    private Optional<String> lastName = Optional.ofNullable(null);
    private Optional<String> email = Optional.ofNullable(null);
    private Optional<String> city = Optional.ofNullable(null);

    public User(){}

    public User(String firstName, String lastName, String city){
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
        this.city = Optional.ofNullable(city);
    }
}
