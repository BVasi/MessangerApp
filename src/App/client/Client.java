package App.client;

import App.UiHandler.UiHandler;
import App.message.Message;
import App.request.Request;
import App.serverresponse.Response;
import App.serverresponse.ServerResponse;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

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
            serverSocket_ = new Socket(InetAddress.getByName("192.168.0.102"), PORT_NUMBER); //if somebody wants to use this app locally too, just change the ip with your local ip of the server
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
            ServerResponse serverResponse;
            while ((serverResponse = serverResponses_.poll()) == null); //wait for server response
            return (serverResponse.getResponse() == Response.OK);
        }catch (IOException exception)
        {
            exception.printStackTrace();
        }
        return false;
    }
    private void startMessageListener()
    {
        Thread messageListener = new Thread(() -> {
            try
            {
                while (true)
                {
                    Object serverMessage = streamFromServer_.readObject();
                    if (serverMessage != null)
                    {
                        Thread messageDispatcher = new Thread(() -> {
                            try
                            {
                                dispatchMessage(serverMessage);
                            } catch (IOException | ClassNotFoundException e)
                            {
                                e.printStackTrace();
                            }
                        });
                        messageDispatcher.start();
                    }
                }
            }catch (IOException | ClassNotFoundException exception)
            {
                exception.printStackTrace();
            }
        });
        messageListener.start();
    }
    private void dispatchMessage(Object serverMessage) throws IOException, ClassNotFoundException
    {
        if (serverMessage instanceof ServerResponse)
        {
            serverResponses_.offer((ServerResponse)serverMessage);
        }
        if (serverMessage instanceof Message)
        {
            UiHandler.updateUiMessages((Message)serverMessage);
        }
        if (serverMessage instanceof List<?>)
        {
            assert serverMessage instanceof ArrayList<?>;
            ArrayList<Message> messageList = (ArrayList<Message>)serverMessage;
            for (Message message : messageList)
            {
                UiHandler.updateUiMessages(message);
            }
        }
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
        startMessageListener();
    }
    private static Client instance_;
    private Socket serverSocket_;
    private ObjectOutputStream streamToServer_;
    private ObjectInputStream streamFromServer_;
    private ConcurrentLinkedQueue<ServerResponse> serverResponses_ = new ConcurrentLinkedQueue<>();
    private static final int PORT_NUMBER = 3389;
}