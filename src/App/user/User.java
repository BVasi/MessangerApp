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
    public String getUsername()
    {
        return username_;
    }
    public String getPassword()
    {
        return password_;
    }
    private String username_;
    private String password_;
}