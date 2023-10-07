package App;

import App.user.User;

public class MainApp
{
    public static void main(String[] args) throws Exception
    {
        for (int id = 0; id < 100; id++)
        {
            User user = new User(id, "username" + id, "password" + id);
            user.sendMessageToServer("Message from user with id " + user.getId());
            user.close();
        }
    }
}