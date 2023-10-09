package App.actionhandler;

import App.database.DataBase;
import App.user.User;

import java.io.IOException;
import java.sql.SQLException;

public class ActionHandler
{
    public static boolean handleRegistration(final User user) throws SQLException, IOException
    {
        DataBase dataBase = DataBase.getInstance();
        return dataBase.writeUser(user) != null;
    }
    public static boolean handleLogin(final User user) throws SQLException, IOException
    {
        DataBase dataBase = DataBase.getInstance();
        return dataBase.getUser(user.getUsername(), user.getPassword()) != null;
    }
    //to do: write message sending logic
}