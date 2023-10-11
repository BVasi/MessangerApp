package App.server;

import App.actionhandler.ActionHandler;
import App.message.Message;
import App.request.Request;
import App.serverresponse.Response;
import App.serverresponse.ServerResponse;
import App.user.User;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

public class ClientHandler implements Runnable
{
    public ClientHandler(Socket clientSocket, Server server)
    {
        clientSocket_ = clientSocket;
        server_ = server;
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
            server_.disconnect(this);
            System.out.println("Connection reset by client.");
        } catch(SQLException | ClassNotFoundException | IOException exception)
        {
            exception.printStackTrace();
        } finally
        {
            closeConnection();
        }
    }
    public void sendMessageToClient(final Message message) throws IOException
    {
        streamToClient_.writeObject(message);
    }
    public User getLoggedUser()
    {
        return loggedUser_;
    }
    private void setupStreams() throws IOException
    {
        streamToClient_ = new ObjectOutputStream(clientSocket_.getOutputStream());
        streamFromClient_ = new ObjectInputStream(clientSocket_.getInputStream());
    }
    private void processRequest(Request request) throws SQLException, IOException
    {
        switch (request.getAction())
        {
            case REGISTER:
            {
                streamToClient_.writeObject(ActionHandler.handleRegistration((User)request.getActionSpecificObject()) ? new ServerResponse(Response.OK) :
                        new ServerResponse(Response.BAD));
                break;
            }
            case LOGIN:
            {
                User tryingUser = (User)request.getActionSpecificObject();
                if (ActionHandler.handleLogin(tryingUser))
                {
                    loggedUser_ = tryingUser;
                    streamToClient_.writeObject(new ServerResponse(Response.OK));
                }
                else
                {
                    streamToClient_.writeObject(new ServerResponse(Response.BAD));
                }
                break;
            }
            case SEND_MESSAGE:
            {
                Message messageToRoute = (Message)request.getActionSpecificObject();
                if (ActionHandler.handleMessage(messageToRoute))
                {
                    server_.routeMessage(messageToRoute);
                    streamToClient_.writeObject(new ServerResponse(Response.OK));
                }
                else
                {
                    streamToClient_.writeObject(new ServerResponse(Response.BAD));
                }
                break;
            }
            case SEARCH_USER:
            {
                streamToClient_.writeObject(ActionHandler.handleSearchUser((String)request.getActionSpecificObject()) ? new ServerResponse(Response.OK) :
                        new ServerResponse(Response.BAD));
                break;
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
                server_.disconnect(this);
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
    private final Server server_;
    private final Socket clientSocket_;
    private ObjectOutputStream streamToClient_;
    private ObjectInputStream streamFromClient_;
    private User loggedUser_; //to do: read comment in server line 42
}