package App;

import App.UiHandler.UiHandler;

import javax.swing.*;

public class Application
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            UiHandler uiHandler = UiHandler.getInstance();
            uiHandler.initialize();
        });
    }
} //I organized the whole app like this because it's just an example, normally I would have split the client/server functionalities