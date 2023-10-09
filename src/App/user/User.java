package App.user;

import java.io.IOException;
import java.io.Serializable;

public class User implements Serializable
{
    public User(final String username, final String password) throws IOException
    {
        username_ = username;
        password_ = password;
    }
    public User(final int id, final String username, final String password) throws IOException
    {
        id_ = id;
        username_ = username;
        password_ = password;
    }
    public int getId()
    {
        return id_;
    }
    public void setId(final int id)
    {
        id_ = id;
    }
    public String getUsername()
    {
        return username_;
    }
    public String getPassword()
    {
        return password_;
    }
    private int id_;
    private String username_;
    private String password_;
}