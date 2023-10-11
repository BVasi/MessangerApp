package App.serverresponse;

import java.io.Serializable;

public class ServerResponse implements Serializable
{
    public ServerResponse(final Response response)
    {
        response_ = response;
    }
    public Response getResponse()
    {
        return response_;
    }
    private Response response_;
}