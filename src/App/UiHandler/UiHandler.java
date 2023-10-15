package App.UiHandler;

import App.client.Client;
import App.message.Message;
import App.request.Action;
import App.request.Request;
import App.user.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

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
    public static void updateUiMessages(Message messageToUpdate)
    {
        if (!interactedWithUsers_.contains(messageToUpdate.getSenderUsername()))
        {
            interactedWithUsers_.addElement(messageToUpdate.getSenderUsername());
        }
        if (conversations_.getSelectedValue() != null && conversations_.getSelectedValue().equals(messageToUpdate.getSenderUsername()))
        {
            chatTextArea_.append(messageToUpdate.getSenderUsername() + ": " + messageToUpdate.getContent() + "\n");
        }
        List<String> messagesList = conversationsMap_.computeIfAbsent(messageToUpdate.getSenderUsername(), k -> new ArrayList<>());
        messagesList.add(messageToUpdate.getSenderUsername() + ": " + messageToUpdate.getContent() + "\n");
    }
    public void initialize()
    {
        initializeFrame();
        initializeMainPanelAndAttachItToFrame();
    }
    private void switchToPanel(final JPanel panel, final String panelName)
    {
        mainPanel_.remove(LAST_PANEL);
        mainPanel_.add(panel, panelName);
        mainPanel_.revalidate();
        mainPanel_.repaint();
    }
    private JPanel createInitialPanel()
    {
        JPanel initialPanel = new JPanel(new GridBagLayout());

        JButton loginButton = new JButton(LOGIN);
        JButton registerButton = new JButton(REGISTER);

        Dimension buttonSize = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
        loginButton.setPreferredSize(buttonSize);
        registerButton.setPreferredSize(buttonSize);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        initializeGridBagConstraints(gridBagConstraints);

        initialPanel.add(registerButton, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        initialPanel.add(loginButton, gridBagConstraints);

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

        return initialPanel;
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
        initializeGridBagConstraints(gridBagConstraints);

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
                register();
            }
        });
        switchToPanel(registrationPanel, "registration");
    }

    private void showLoginForm()
    {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField_ = new JTextField(20);
        passwordField_ = new JPasswordField(20);
        JButton loginButton = new JButton(LOGIN);

        Dimension textFieldSize = new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        usernameField_.setPreferredSize(textFieldSize);
        passwordField_.setPreferredSize(textFieldSize);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        initializeGridBagConstraints(gridBagConstraints);

        loginPanel.add(usernameLabel, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        loginPanel.add(usernameField_, gridBagConstraints);

        gridBagConstraints.gridy = 2;
        loginPanel.add(passwordLabel, gridBagConstraints);

        gridBagConstraints.gridy = 3;
        loginPanel.add(passwordField_, gridBagConstraints);

        gridBagConstraints.gridy = 4;
        loginPanel.add(loginButton, gridBagConstraints);

        loginButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                login();
            }
        });
        switchToPanel(loginPanel, "login");
    }
    private void showMainForm()
    {
        JPanel mainFormPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new BorderLayout());

        JScrollPane conversationScrollPane = new JScrollPane(conversations_);
        leftPanel.add(conversationScrollPane, BorderLayout.CENTER);

        JPanel searchFieldPanel = new JPanel();
        searchFieldPanel.setLayout(new BoxLayout(searchFieldPanel, BoxLayout.Y_AXIS));

        JTextField searchField = new JTextField(20);
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        searchFieldPanel.add(searchField);

        leftPanel.add(searchFieldPanel, BorderLayout.NORTH);

        JPanel rightPanel = new JPanel(new BorderLayout());

        JLabel messageLabel = new JLabel();

        chatTextArea_.setEditable(false);

        JScrollPane toRename = new JScrollPane(chatTextArea_);
        rightPanel.add(toRename, BorderLayout.CENTER);

        JPanel messageInputPanel = new JPanel(new BorderLayout());

        JTextField messageField = new JTextField();
        messageInputPanel.add(messageField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        messageInputPanel.add(sendButton, BorderLayout.EAST);

        rightPanel.add(messageInputPanel, BorderLayout.SOUTH);
        rightPanel.add(messageLabel, BorderLayout.EAST);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(200);

        mainFormPanel.add(splitPane, BorderLayout.CENTER);
        conversations_.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                updateUiForConversation();
            }
        });
        searchField.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                searchUser(searchField.getText());
            }
        });
        messageField.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(messageField);
            }
        });
        sendButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                sendMessage(messageField);
            }
        });
        switchToPanel(mainFormPanel, "mainForm");
    }
    private void saveMessagesLocally()
    {
        String fileName = "conversations_" + usernameField_.getText() + ".bin";
        try
        {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName, false));
            outputStream.writeObject(conversationsMap_);
            outputStream.close();
        }catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
    private void getSavedMessages()
    {
        String fileName = "conversations_" + usernameField_.getText() + ".bin";
        File file = new File(fileName);
        if (!file.exists())
        {
            return;
        }
        try
        {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName));
            conversationsMap_ = (Map<String, List<String>>)inputStream.readObject();
            inputStream.close();
            for (Map.Entry entry : conversationsMap_.entrySet())
            {
                interactedWithUsers_.addElement(entry.getKey().toString());
            }
        }catch (IOException | ClassNotFoundException exception)
        {
            exception.printStackTrace();
        }
    }
    private void initializeFrame()
    {
        frame_ = new JFrame(APPLICATION_TITLE);
        frame_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                if (usernameField_ != null)
                {
                    saveMessagesLocally();
                }
            }
        });
        frame_.setSize(CURRENT_WIDTH, CURRENT_HEIGHT);
        frame_.setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
    }
    private void initializeMainPanelAndAttachItToFrame()
    {
        mainPanel_ = new JPanel(new CardLayout());
        JPanel loginPanel = createInitialPanel();
        mainPanel_.add(loginPanel, "login");
        frame_.add(mainPanel_);
        frame_.setLocationRelativeTo(null);
        frame_.setVisible(true);
    }
    private void initializeGridBagConstraints(GridBagConstraints gridBagConstraints)
    {
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(TOP_MARGIN, LEFT_MARGIN, BOTTOM_MARGIN, RIGHT_MARGIN);
    }
    private void register()
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
        }catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
    private void login()
    {
        try
        {
            Request request = new Request(Action.LOGIN, new User(usernameField_.getText(), new String(passwordField_.getPassword())));
            Client client = Client.getInstance();
            if (client.sendRequestToServer(request))
            {
                getSavedMessages();
                showMainForm();
            }
        }catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
    private void updateUiForConversation()
    {
        List<String> conversationMessages = conversationsMap_.get(conversations_.getSelectedValue());
        chatTextArea_.setText("");
        if (conversationMessages == null)
        {
            return;
        }
        for (String message : conversationMessages)
        {
            chatTextArea_.append(message);
        }
    }
    private void searchUser(final String username)
    {
        try
        {
            Request request = new Request(Action.SEARCH_USER, username);
            Client client = Client.getInstance();
            if (client.sendRequestToServer(request))
            {
                if (interactedWithUsers_.contains(username) || username.equals(usernameField_.getText()))
                {
                    return;
                }
                conversationsMap_.put(username, new ArrayList<>());
                interactedWithUsers_.addElement(username);
            }
            else
            {
                JOptionPane.showMessageDialog(frame_, "User " + username + " doesn't exist!");
            }
        }catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
    private void sendMessage(JTextField messageField)
    {
        try
        {
            if (conversations_.getSelectedValue() == null)
            {
                JOptionPane.showMessageDialog(frame_, "Select the user you want to send the message to!");
                return;
            }
            Request request = new Request(Action.SEND_MESSAGE, new Message(usernameField_.getText(),
                    conversations_.getSelectedValue(), messageField.getText()));
            Client client = Client.getInstance();
            if (!client.sendRequestToServer(request))
            {
                JOptionPane.showMessageDialog(frame_, "Error at sending message!");
            }
            else
            {
                List<String> messagesList = conversationsMap_.computeIfAbsent(conversations_.getSelectedValue(), k -> new ArrayList<>());
                messagesList.add("You" + ": " + messageField.getText() + "\n");
                chatTextArea_.append("You: " + messageField.getText() + "\n");
                messageField.setText("");
            }
        }catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
    private UiHandler() {}
    private static UiHandler instance_;
    private JFrame frame_;
    private JPanel mainPanel_;
    private JTextField usernameField_;
    private JPasswordField passwordField_;
    private static DefaultListModel<String> interactedWithUsers_ = new DefaultListModel<>();
    private static JList<String> conversations_ = new JList<>(interactedWithUsers_);
    private static JTextArea chatTextArea_ = new JTextArea();
    private static Map<String, List<String>> conversationsMap_ = new HashMap<>();
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
    private static final int LAST_PANEL = 0;
} //to do: refactor this spaghetti code