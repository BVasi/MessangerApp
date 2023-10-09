package App.client;

import App.request.Request;

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
            streamFromServer_ = new BufferedReader(new InputStreamReader(serverSocket_.getInputStream()));
        } catch (IOException ioException)
        {
            ioException.printStackTrace();
            throw ioException;
        }
    }
    public boolean sendRequestToServer(Request request) throws IOException //to do: wait for a response from the server if the action got completed (registration/login) and return that
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
            return true;
        }catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
        return false;
    }
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
    private BufferedReader streamFromServer_;
    private final int PORT_NUMBER = 8888;
}