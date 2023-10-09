package App.UiHandler;

import App.actionhandler.ActionHandler;
import App.client.Client;
import App.request.Action;
import App.request.Request;
import App.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class UiHandler
{
    public static UiHandler getInstance()
    {
        if (instance_ == null)
        {
            instance_ = new UiHandler();
        }
        return instance_;
    }
    public void initialize()
    {
        frame_ = new JFrame(APPLICATION_TITLE);
        frame_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_.setSize(CURRENT_WIDTH, CURRENT_HEIGHT);
        frame_.setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));

        mainPanel_ = new JPanel(new CardLayout());

        JPanel loginPanel = createInitialPanel();
        mainPanel_.add(loginPanel, "login");

        frame_.add(mainPanel_);
        frame_.setLocationRelativeTo(null);
        frame_.setVisible(true);
    }

    private JPanel createInitialPanel()
    {
        JPanel loginPanel = new JPanel(new GridBagLayout());

        JButton loginButton = new JButton(LOGIN);
        JButton registerButton = new JButton(REGISTER);

        Dimension buttonSize = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
        loginButton.setPreferredSize(buttonSize);
        registerButton.setPreferredSize(buttonSize);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(TOP_MARGIN, LEFT_MARGIN, BOTTOM_MARGIN, RIGHT_MARGIN);

        loginPanel.add(registerButton, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        loginPanel.add(loginButton, gridBagConstraints);

        registerButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                showRegistrationForm();
            }
        });

        loginButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                showLoginForm();
            }
        });

        return loginPanel;
    }

    private void showRegistrationForm()
    {
        JPanel registrationPanel = new JPanel(new GridBagLayout());
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField_ = new JTextField(20);
        passwordField_ = new JPasswordField(20);
        JButton registerButton = new JButton(REGISTER);

        Dimension textFieldSize = new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        usernameField_.setPreferredSize(textFieldSize);
        passwordField_.setPreferredSize(textFieldSize);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(TOP_MARGIN, LEFT_MARGIN, BOTTOM_MARGIN, RIGHT_MARGIN);

        registrationPanel.add(usernameLabel, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        registrationPanel.add(usernameField_, gridBagConstraints);

        gridBagConstraints.gridy = 2;
        registrationPanel.add(passwordLabel, gridBagConstraints);

        gridBagConstraints.gridy = 3;
        registrationPanel.add(passwordField_, gridBagConstraints);

        gridBagConstraints.gridy = 4;
        registrationPanel.add(registerButton, gridBagConstraints);

        registerButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    Request request = new Request(Action.REGISTER, new User(usernameField_.getText(), new String(passwordField_.getPassword())));
                    Client client = Client.getInstance();
                    if (client.sendRequestToServer(request))
                    {
                        JOptionPane.showMessageDialog(frame_, "Registered sucessfully!");
                        showLoginForm();
                    }
                } catch (IOException ioException)
                {
                    ioException.printStackTrace();
                }
            }
        });

        mainPanel_.remove(0);
        mainPanel_.add(registrationPanel, "registration");
        mainPanel_.revalidate();
        mainPanel_.repaint();
    }

    private void showLoginForm()
    {
        JPanel registrationPanel = new JPanel(new GridBagLayout());
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField_ = new JTextField(20);
        passwordField_ = new JPasswordField(20);
        JButton loginButton = new JButton(LOGIN);

        Dimension textFieldSize = new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        usernameField_.setPreferredSize(textFieldSize);
        passwordField_.setPreferredSize(textFieldSize);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(TOP_MARGIN, LEFT_MARGIN, BOTTOM_MARGIN, RIGHT_MARGIN);

        registrationPanel.add(usernameLabel, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        registrationPanel.add(usernameField_, gridBagConstraints);

        gridBagConstraints.gridy = 2;
        registrationPanel.add(passwordLabel, gridBagConstraints);

        gridBagConstraints.gridy = 3;
        registrationPanel.add(passwordField_, gridBagConstraints);

        gridBagConstraints.gridy = 4;
        registrationPanel.add(loginButton, gridBagConstraints);

        loginButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    Request request = new Request(Action.LOGIN, new User(usernameField_.getText(), new String(passwordField_.getPassword())));
                    Client client = Client.getInstance();
                    if (client.sendRequestToServer(request)) //to do: proper check if its ok to login
                    {
                        JOptionPane.showMessageDialog(frame_, "Logged in sucessfully!");
                        //to do main panel
                    }
                }catch (IOException ioException)
                {
                    ioException.printStackTrace();
                }
            }
        });

        mainPanel_.remove(0);
        mainPanel_.add(registrationPanel, "login");
        mainPanel_.revalidate();
        mainPanel_.repaint();
    }
    private UiHandler() {}
    private static UiHandler instance_;
    private JFrame frame_;
    private JPanel mainPanel_;
    private JTextField usernameField_;
    private JPasswordField passwordField_;
    private static final String APPLICATION_TITLE = "Messenger";
    private static final String LOGIN = "Login";
    private static final String REGISTER = "Register";
    private static int CURRENT_WIDTH = 600;
    private static int CURRENT_HEIGHT = 800;
    private static final int MINIMUM_WIDTH = 400;
    private static final int MINIMUM_HEIGHT = 400;
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 40;
    private static final int TOP_MARGIN = 10;
    private static final int LEFT_MARGIN = 0;
    private static final int BOTTOM_MARGIN = 10;
    private static final int RIGHT_MARGIN = 0;
    private static final int TEXT_FIELD_WIDTH = 200;
    private static final int TEXT_FIELD_HEIGHT = 30;
}