package App.actionhandler;

import App.database.DataBase;
import App.message.Message;
import App.user.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
    public static boolean handleUpdateLastOnline(final String username) throws SQLException
    {
        DataBase dataBase = DataBase.getInstance();
        return dataBase.updateLastOnline(username);
    }
    public static List<Message> handleReceivedMessagesWhenWasOffline(final String username) throws SQLException
    {
        DataBase dataBase = DataBase.getInstance();
        return dataBase.getMessagesFromWhenWasOffline(username);
    }
    private static final int NOT_FOUND = -1;
}