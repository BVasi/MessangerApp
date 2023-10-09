package App;

import App.UiHandler.UiHandler;

import javax.swing.*;

public class Application
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                UiHandler uiHandler = UiHandler.getInstance();
                uiHandler.initialize();
            }
        });
    }
}