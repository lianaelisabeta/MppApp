package models;

import java.io.Serializable;

public class User implements HasId<String>,Serializable {
    private String UserId;
    private String Name;
    private String Password;

    public User(String uid,String uname,String pass){
        UserId=uid;
        Name=uname;
        Password=pass;
    }

    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }

    public String getId() {
        return UserId;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
    public void setId(String userId) {
        UserId = userId;
    }
    @Override
    public String toString(){
        return UserId+" "+Name+" "+Password;
    }
}
