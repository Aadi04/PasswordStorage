package javaFiles.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserData
{
    private final StringProperty website;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty notes;

    public UserData (String website, String username, String password, String notes)
    {
        this.website = new SimpleStringProperty(website);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.notes = new SimpleStringProperty(notes);
    }

    public String getWebsite()
    {
        return website.get();
    }

    public StringProperty websiteProperty()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website.set(website);
    }

    public String getUsername()
    {
        return username.get();
    }

    public StringProperty usernameProperty()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username.set(username);
    }

    public String getPassword()
    {
        return password.get();
    }

    public StringProperty passwordProperty()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password.set(password);
    }

    public String getNotes()
    {
        return notes.get();
    }

    public StringProperty notesProperty()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes.set(notes);
    }

}
