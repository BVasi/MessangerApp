package App.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

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
            processMessageFromClient();
        }catch (SocketException socketException)
        {
            System.out.println("Connection reset by client.");
        }catch (IOException ioException)
        {
            ioException.printStackTrace();
        }finally
        {
            closeConnection();
        }
    }
    private void setupStreams() throws IOException
    {
        streamToClient_ = new PrintWriter(clientSocket_.getOutputStream(), AUTO_FLUSH);
        streamFromClient_ = new BufferedReader(new InputStreamReader(clientSocket_.getInputStream()));
    }
    private void processMessageFromClient() throws IOException
    {
        String receivedMessage;
        while ((receivedMessage = streamFromClient_.readLine()) != null)
        {
            System.out.println("Received message: " + receivedMessage);
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
    private PrintWriter streamToClient_;
    private BufferedReader streamFromClient_;
    private boolean AUTO_FLUSH = true;
}