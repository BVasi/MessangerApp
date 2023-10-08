package App.server;

import java.io.IOException;
import java.net.ServerSocket;

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
                ClientHandler clientHandler = new ClientHandler(serverSocket_.accept());
                System.out.println("Connecting!");
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
                System.out.println("Connection established!");
            }catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }
    }
    public static void main(String[] args)
    {
        new Server();
    }
    private ServerSocket serverSocket_;
    private final int PORT_NUMBER = 8888;
}