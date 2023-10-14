package App.database;

import App.message.Message;
import App.user.User;
import oracle.jdbc.pool.OracleDataSource;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataBase
{
    public static DataBase getInstance()
    {
        if (instance_ == null)
        {
            instance_ = new DataBase();
        }
        return instance_;
    }
    public User writeUser(final User user) throws SQLException, IOException
    {
        OracleDataSource oracleDataSource = new OracleDataSource();
        oracleDataSource.setURL(URL_STRING);
        oracleDataSource.setUser(USERNAME_STRING);
        oracleDataSource.setPassword(PASSWORD_STRING);
        if (getUserIdByUsername(user.getUsername()) != NOT_FOUND)
        {
            return null;
        }
        try (Connection connection = oracleDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER))
        {
            preparedStatement.setString(FIRST_PARAMETER_INDEX, user.getUsername());
            preparedStatement.setString(SECOND_PARAMETER_INDEX, user.getPassword());
            preparedStatement.execute();
        }catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return getUser(user.getUsername(), user.getPassword());
    }
    public boolean writeMessage(final Message message) throws SQLException
    {
        OracleDataSource oracleDataSource = new OracleDataSource();
        oracleDataSource.setURL(URL_STRING);
        oracleDataSource.setUser(USERNAME_STRING);
        oracleDataSource.setPassword(PASSWORD_STRING);
        try (Connection connection = oracleDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MESSAGE))
        {
            preparedStatement.setTimestamp(FIRST_PARAMETER_INDEX, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setInt(SECOND_PARAMETER_INDEX, getUserIdByUsername(message.getSenderUsername()));
            preparedStatement.setInt(THIRD_PARAMETER_INDEX, getUserIdByUsername(message.getReceiverUsername()));
            preparedStatement.setString(FOURTH_PARAMETER_INDEX, message.getContent());
            preparedStatement.execute();
            return true;
        }catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return false;
    }
    public User getUser(final String username, final String password) throws SQLException, IOException
    {
        OracleDataSource oracleDataSource = new OracleDataSource();
        oracleDataSource.setURL(URL_STRING);
        oracleDataSource.setUser(USERNAME_STRING);
        oracleDataSource.setPassword(PASSWORD_STRING);
        try (Connection connection = oracleDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER))
        {
            preparedStatement.setString(FIRST_PARAMETER_INDEX, username);
            preparedStatement.setString(SECOND_PARAMETER_INDEX, password);
            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    return new User(resultSet.getString(USERNAME), resultSet.getString(PASSWORD));
                }
            }
        }catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return null;
    }
    public int getUserIdByUsername(final String username) throws SQLException
    {
        OracleDataSource oracleDataSource = new OracleDataSource();
        oracleDataSource.setURL(URL_STRING);
        oracleDataSource.setUser(USERNAME_STRING);
        oracleDataSource.setPassword(PASSWORD_STRING);
        try (Connection connection = oracleDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID))
        {
            preparedStatement.setString(FIRST_PARAMETER_INDEX, username);
            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(ID);
                }
                return NOT_FOUND;
            }
        }
    }
    public String getUserUsernameById(final int id) throws SQLException
    {
        OracleDataSource oracleDataSource = new OracleDataSource();
        oracleDataSource.setURL(URL_STRING);
        oracleDataSource.setUser(USERNAME_STRING);
        oracleDataSource.setPassword(PASSWORD_STRING);
        try (Connection connection = oracleDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERNAME))
        {
            preparedStatement.setInt(FIRST_PARAMETER_INDEX, id);
            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString(USERNAME);
                }
                return null;
            }
        }
    }
    public boolean updateLastOnline(final String username) throws SQLException
    {
        OracleDataSource oracleDataSource = new OracleDataSource();
        oracleDataSource.setURL(URL_STRING);
        oracleDataSource.setUser(USERNAME_STRING);
        oracleDataSource.setPassword(PASSWORD_STRING);
        try (Connection connection = oracleDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LAST_ONLINE))
        {
            preparedStatement.setTimestamp(FIRST_PARAMETER_INDEX, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(SECOND_PARAMETER_INDEX, username);
            preparedStatement.execute();
            return true;
        }catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return false;
    }
    public List<Message> getMessagesFromWhenWasOffline(final String username) throws SQLException
    {
        OracleDataSource oracleDataSource = new OracleDataSource();
        oracleDataSource.setURL(URL_STRING);
        oracleDataSource.setUser(USERNAME_STRING);
        oracleDataSource.setPassword(PASSWORD_STRING);
        List<Message> messageList = new ArrayList<>();
        try (Connection connection = oracleDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RECEIVED_MESSAGES_WHEN_WAS_OFFLINE))
        {
            preparedStatement.setInt(FIRST_PARAMETER_INDEX, getUserIdByUsername(username));
            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                while (resultSet.next())
                {
                    messageList.add(new Message(getUserUsernameById(resultSet.getInt(SENDER_ID)), username, resultSet.getString(CONTENT)));
                }
            }
        }
        return messageList;
    }
    private DataBase() {}
    private static DataBase instance_ = null;
    private final static String DB_URL = "DB_URL";
    private final  static String DB_USERNAME = "DB_USERNAME";
    private final static String DB_PASSWORD = "DB_PASSWORD";
    private final static String URL_STRING = System.getenv(DB_URL);
    private final static String USERNAME_STRING = System.getenv(DB_USERNAME);
    private final static String PASSWORD_STRING = System.getenv(DB_PASSWORD);
    private static final int NOT_FOUND = -1;
    private final static int FIRST_PARAMETER_INDEX = 1;
    private final static int SECOND_PARAMETER_INDEX = 2;
    private final static int THIRD_PARAMETER_INDEX = 3;
    private final static int FOURTH_PARAMETER_INDEX = 4;
    private final static String ID = "id";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String SENDER_ID = "senderId";
    private final static String CONTENT = "content";
    private final String INSERT_USER = "INSERT INTO Users (username, password) VALUES (?, ?)";
    private final String INSERT_MESSAGE = "INSERT INTO Messages (sendingDate, senderId, receiverId, content) VALUES (?, ?, ?, ?)";
    private final String SELECT_ID = "SELECT id from Users WHERE username = ?";
    private final String SELECT_USER = "SELECT id, username, password from Users WHERE username = ? AND password = ?";
    private final String SELECT_USERNAME = "SELECT username from Users WHERE id = ?";
    private final String UPDATE_LAST_ONLINE = "UPDATE Users SET lastOnline = ? WHERE username = ?";
    private final String SELECT_RECEIVED_MESSAGES_WHEN_WAS_OFFLINE = "SELECT m.senderId, m.content FROM Messages m WHERE m.receiverId = (SELECT u.id FROM Users u WHERE m.sendingDate > u.lastOnline AND u.id = ?)";
} //to do: refactor the class and make it more efficient by not converting the types using the database