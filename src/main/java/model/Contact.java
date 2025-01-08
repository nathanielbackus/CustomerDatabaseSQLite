package model;

import javafx.collections.ObservableList;

public class Contact {
    private int ContactID;
    private String ContactName, Email;
    public Contact(int ContactID, String ContactName, String Email){
        this.ContactID = ContactID;
        this.ContactName = ContactName;
        this.Email = Email;
    }

    public int getContactID() {
        return ContactID;
    }

    public void setContactID(int contactID) {
        ContactID = contactID;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
    @Override
    public String toString(){
        return ContactName;
    }
}
