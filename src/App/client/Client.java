package App.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class Client implements AutoCloseable
{
    public Client() throws IOException
    {
        connectToServer();
    }
    @Override
    public void close() throws Exception
    {
        disconnectClientFromServer();
    }
    private void connectToServer() throws IOException
    {
        try
        {
            serverSocket_ = new Socket(InetAddress.getLocalHost(), PORT_NUMBER);
            streamToServer_ = new PrintWriter(serverSocket_.getOutputStream(), AUTO_FLUSH);
            streamFromServer_ = new BufferedReader(new InputStreamReader(serverSocket_.getInputStream()));
        } catch (IOException ioException)
        {
            ioException.printStackTrace();
            throw ioException;
        }
    }
    public void sendMessageToServer(String message)
    {
        if (serverSocket_ == null || serverSocket_.isClosed())
        {
            System.out.println("Error: Connection is closed!");
            return;
        }
        streamToServer_.println(message);
    }
    private void disconnectClientFromServer() throws IOException
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
    private Socket serverSocket_;
    private PrintWriter streamToServer_;
    private BufferedReader streamFromServer_;
    private final int PORT_NUMBER = 8888;
    private final boolean AUTO_FLUSH = true;
}