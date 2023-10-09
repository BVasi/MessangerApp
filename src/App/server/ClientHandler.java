package App.server;

import App.actionhandler.ActionHandler;
import App.message.Message;
import App.request.Request;
import App.user.User;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

public class ClientHandler implements Runnable
{
    public ClientHandler(Socket clientSocket)
    {
        clientSocket_ = clientSocket;
    }
    @Override
    public void run()
    {
        try
        {
            setupStreams();
            getClientRequests();
        } catch (SocketException socketException)
        {
            System.out.println("Connection reset by client.");
            //disconnected
        } catch(SQLException | ClassNotFoundException | IOException exception)
        {
            exception.printStackTrace();
        } finally
        {
            closeConnection();
        }
    }
    private void setupStreams() throws IOException
    {
        streamToClient_ = new ObjectOutputStream(clientSocket_.getOutputStream());
        streamFromClient_ = new ObjectInputStream(clientSocket_.getInputStream());
    }
    private void processRequest(Request request) throws SQLException, IOException //to do: send an ack signal back
    {
        switch (request.getAction())
        {
            case REGISTER:
            {
                ActionHandler.handleRegistration((User)request.getActionSpecificObject());
            }
            case LOGIN:
            {
                ActionHandler.handleLogin((User)request.getActionSpecificObject());
            }
            case SEND_MESSAGE:
            {
                //to do the logic here
            }
        }
    }
    private void getClientRequests() throws IOException, SQLException, ClassNotFoundException
    {
        while (true)
        {
            try
            {
                Request request = (Request)streamFromClient_.readObject();
                processRequest(request);
            }catch (IOException ioException)
            {
                System.out.println("User disconnected!");
                return;
            }
        }
    }
    private void closeConnection()
    {
        try
        {
            if (streamFromClient_ != null)
            {
                streamFromClient_.close();
            }
            if (streamToClient_ != null)
            {
                streamToClient_.close();
            }
            if (clientSocket_ != null)
            {
                clientSocket_.close();
            }
        }catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
    private Socket clientSocket_;
    private ObjectOutputStream streamToClient_;
    private ObjectInputStream streamFromClient_;
}