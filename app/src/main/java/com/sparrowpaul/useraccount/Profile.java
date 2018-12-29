package com.sparrowpaul.useraccount;

public class Profile {
    private String personName, email, telNumber;

    public Profile(String personName, String email, String telNumber) {
        this.personName = personName;
        this.email = email;
        this.telNumber = telNumber;
    }

    public Profile() {
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }
}
