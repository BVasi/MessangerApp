package App.user;

import App.client.Client;

import java.io.IOException;

public class User extends Client
{
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
    private int id_;
    private String username_;
    private String password_;
}