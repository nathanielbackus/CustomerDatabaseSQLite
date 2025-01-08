package model;

public class User {
    public int userID;
    public String username, password;

    public User(int userID, String password, String username) {
        this.userID = userID;
        this.password = password;
        this.username = username;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public String toString() {
        return username;
    }
    //user needs to be prompted to enter first time, then successful log in
    //user cannot delete themselves
}
