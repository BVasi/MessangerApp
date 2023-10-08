package App.actionhandler;

import App.database.DataBase;
import App.message.Message;
import App.user.User;

import java.io.IOException;
import java.sql.SQLException;

public class ActionHandler
{
    public static boolean handleRegistration(final String username, final String password) throws SQLException, IOException
    {
        DataBase dataBase = DataBase.getInstance();
        user_ = dataBase.writeUser(username, password);
        dataBase = null;
        if (user_ == null)
        {
            System.out.println("Error at registering!");
            return false;
        }
        return true;
    }
    public static boolean handleLogin(final String username, final String password) throws SQLException, IOException
    {
        DataBase dataBase = DataBase.getInstance();
        user_ = dataBase.getUser(username, password);
        dataBase = null;
        if (user_ == null)
        {
            System.out.println("Error at loging in!");
            return false;
        }
        return true;
    }
    public static void handleSendMessage(final Message message)
    {
        if (user_ == null || message == null)
        {
            System.out.println("Error at sending message!");
            return;
        }
        user_.sendMessageToServer(message);
    }
    private static User user_;
}
