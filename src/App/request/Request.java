package App.request;

import java.io.Serializable;

public class Request implements Serializable
{
    public Request(Action action, Object actionSpecificObject)
    {
        action_ = action;
        actionSpecificObject_ = actionSpecificObject;
    }
    public Action getAction()
    {
        return action_;
    }

    public Object getActionSpecificObject()
    {
        return actionSpecificObject_;
    }

    private Action action_;
    private Object actionSpecificObject_;
}