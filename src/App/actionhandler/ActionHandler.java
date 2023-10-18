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
        return DataBase.writeUser(user) != null;
    }
    public static boolean handleLogin(final User user) throws SQLException, IOException
    {
        return DataBase.getUser(user.getUsername(), user.getPassword()) != null;
    }
    public static boolean handleMessage(final Message message) throws SQLException
    {
        return DataBase.writeMessage(message);
    }
    public static boolean handleSearchUser(final String username) throws SQLException
    {
        return DataBase.getUserIdByUsername(username) != DataBase.NOT_FOUND;
    }
    public static boolean handleUpdateLastOnline(final String username) throws SQLException
    {
        return DataBase.updateLastOnline(username);
    }
    public static List<Message> handleReceivedMessagesWhenWasOffline(final String username) throws SQLException
    {
        return DataBase.getMessagesFromWhenWasOffline(username);
    }
}