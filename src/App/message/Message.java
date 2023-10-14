package App.message;

import java.io.Serializable;

public class Message implements Serializable
{
    public Message(final String senderUsername, final String receiverUsername, final String content)
    {
        senderUsername_ = senderUsername;
        receiverUsername_ = receiverUsername;
        content_ = content;
    }
    public String getSenderUsername()
    {
        return senderUsername_;
    }
    public String getReceiverUsername()
    {
        return receiverUsername_;
    }
    public String getContent()
    {
        return content_;
    }
    private String senderUsername_;
    private String receiverUsername_;
    private String content_;
}