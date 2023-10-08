package App.user;

import App.client.Client;

import java.io.IOException;

public class User extends Client
{
    public User(final String username, final String password) throws IOException
    {
        super();
        username_ = username;
        password_ = password;
    }
    public User(final int id, final String username, final String password) throws IOException
    {
        super();
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