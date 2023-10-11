package App.actionhandler;

import App.database.DataBase;
import App.message.Message;
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
    public static boolean handleMessage(final Message message) throws SQLException
    {
        DataBase dataBase = DataBase.getInstance();
        return dataBase.writeMessage(message);
    }
    public static boolean handleSearchUser(final String username) throws SQLException
    {
        DataBase dataBase = DataBase.getInstance();
        return dataBase.getUserIdByUsername(username) != NOT_FOUND;
    }
    private static int NOT_FOUND = -1;
}