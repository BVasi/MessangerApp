package App.message;

import java.util.Map;

public class Message
{
    public Message(final int senderId,final int receiverId, final String content)
    {
        senderId_ = senderId;
        receiverId_ = receiverId;
        content_ = content;
    }
    @Override
    public String toString()
    {
        return JsonConverter.convertMessageToJson(this);
    }
    public static Message parseMessage(String message)
    {
        Map<String, String> jsonMapOfMessage = JsonConverter.convertJsonToMap(message);
        return new Message(Integer.parseInt(jsonMapOfMessage.get(SENDER)),
                Integer.parseInt(jsonMapOfMessage.get(RECEIVER)), jsonMapOfMessage.get(CONTENT));
    }
    public int getSenderId()
    {
        return senderId_;
    }
    public int getReceiverId()
    {
        return receiverId_;
    }
    public String getContent()
    {
        return content_;
    }
    private int senderId_;
    private int receiverId_;
    private String content_;
    static String SENDER = "sender";
    static String RECEIVER = "receiver";
    static String CONTENT = "content";
}