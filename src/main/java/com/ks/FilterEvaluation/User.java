package com.ks.FilterEvaluation;

import java.util.Hashtable;

public class User
{
    private static final Hashtable<String, User> USER_HASHTABLE = new Hashtable<>();

    private String idUsername;
    private String email;
    private String userName;
    private String name;
    private String updatedAt;

    public User()
    {
        idUsername = "";
        email = "";
        userName = "";
        name = "";
        updatedAt = "";
    }

    public static Hashtable<String, User> getUserHashtable()
    {
        return USER_HASHTABLE;
    }

    public void save()
    {
        USER_HASHTABLE.put(getIdUsername(), this);
    }

    public String getIdUsername()
    {
        return idUsername;
    }

    public void setIdUsername(String idUsername)
    {
        this.idUsername = idUsername;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt)
    {
        this.updatedAt = updatedAt;
    }
}
