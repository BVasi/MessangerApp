package App.server;

import App.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

public class Server
{
    public Server()
    {
        try
        {
            serverSocket_ = new ServerSocket(PORT_NUMBER);
            acceptConnections();
        }catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
    public void mapUserToClient(final String username, final ClientHandler clientHandler)
    {
        usernameClientMap_.put(username, clientHandler);
    }
    private void acceptConnections() throws IOException
    {
        System.out.println("Waiting for connections...");
        while (true)
        {
            try
            {
                ClientHandler clientHandler = new ClientHandler(serverSocket_.accept(), this);
                System.out.println("Connecting!");
                Thread clientThread = new Thread(clientHandler);
                threadClientMap_.put(clientHandler, clientThread);
                clientThread.start();
                System.out.println("Connection established!");
            }catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }
    }
    public void routeMessage(final Message message) throws IOException
    {
        ClientHandler clientHandler;
        if ((clientHandler = usernameClientMap_.get(message.getReceiverUsername())) != null)
        {
            clientHandler.sendMessageToClient(message);
        }
    }
    public void disconnect(final ClientHandler clientHandler, final String username)
    {
        threadClientMap_.get(clientHandler).interrupt();
        threadClientMap_.remove(clientHandler);
        usernameClientMap_.remove(username);
    }
    public static void main(String[] args)
    {
        new Server();
    }
    private ServerSocket serverSocket_;
    private ConcurrentHashMap<ClientHandler, Thread> threadClientMap_ = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ClientHandler> usernameClientMap_ = new ConcurrentHashMap<>();
    private final int PORT_NUMBER = 3389;
}