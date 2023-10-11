package App.server;

import App.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class Server
{
    public Server()
    {
        try
        {
            serverSocket_ = new ServerSocket(PORT_NUMBER);
            acceptConnections();
        }catch(IOException exception)
        {
            exception.printStackTrace();
        }
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
    public void routeMessage(final Message message) throws IOException //to do: make a map for client/user instead of iterating through this and delete user object from ClientHandler
    {
        for (Map.Entry<ClientHandler, Thread> client : threadClientMap_.entrySet())
        {
            if (client.getKey().getLoggedUser().getUsername().equals(message.getReceiverUsername()))
            {
                client.getKey().sendMessageToClient(message);
            }
        }
    }
    public void disconnect(final ClientHandler clientHandler)
    {
        threadClientMap_.get(clientHandler).interrupt();
        threadClientMap_.remove(clientHandler);
    }
    public static void main(String[] args)
    {
        new Server();
    }
    private ServerSocket serverSocket_;
    private Map<ClientHandler, Thread> threadClientMap_ = new HashMap<>();
    private final int PORT_NUMBER = 8888;
}