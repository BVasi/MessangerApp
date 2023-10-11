package App.client;

import App.request.Request;
import App.serverresponse.Response;
import App.serverresponse.ServerResponse;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements AutoCloseable, Serializable
{
    public static Client getInstance() throws IOException
    {
        if (instance_ == null)
        {
            instance_ = new Client();
        }
        return instance_;
    }
    @Override
    public void close()
    {
        disconnectClientFromServer();
    }
    private void connectToServer() throws IOException
    {
        try
        {
            serverSocket_ = new Socket(InetAddress.getLocalHost(), PORT_NUMBER);
            streamToServer_ = new ObjectOutputStream(serverSocket_.getOutputStream());
            streamFromServer_ = new ObjectInputStream(serverSocket_.getInputStream());
        } catch (IOException ioException)
        {
            ioException.printStackTrace();
            throw ioException;
        }
    }
    public boolean sendRequestToServer(Request request) throws IOException
    {
        if (serverSocket_ == null || serverSocket_.isClosed())
        {
            System.out.println("Error: Connection is closed!");
            return false;
        }
        try
        {
            streamToServer_.writeObject(request);
            streamToServer_.flush();
            return getAndProcessServerResponse();
        }catch (IOException | ClassNotFoundException exception)
        {
            exception.printStackTrace();
        }
        return false;
    }
    private boolean getAndProcessServerResponse() throws IOException, ClassNotFoundException //to do: message dispatcher
    {
        ServerResponse serverResponse = (ServerResponse)streamFromServer_.readObject();
        return serverResponse.getResponse() == Response.OK;
    }
    /*
    dispatcher skeleton:
    start a new thread to listen for messages, have 2 queues, one for messages for UI and one for suggesting if the operation had or not success
    have 2 threads, one for each queue that gets the message and processes it (might not work, but it's a starting idea)
    */
    private void disconnectClientFromServer()
    {
        try
        {
            if (streamToServer_ != null)
            {
                streamToServer_.close();
            }
            if (streamFromServer_ != null)
            {
                streamFromServer_.close();
            }
            if (serverSocket_ != null)
            {
                serverSocket_.close();
            }
        } catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
    private Client() throws IOException
    {
        connectToServer();
    }
    private static Client instance_;
    private Socket serverSocket_;
    private ObjectOutputStream streamToServer_;
    private ObjectInputStream streamFromServer_;
    private final int PORT_NUMBER = 8888;
}