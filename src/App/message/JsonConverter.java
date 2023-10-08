package App.message;

import java.util.HashMap;
import java.util.Map;

import static App.message.Message.*;

public class JsonConverter
{
    public static String convertMessageToJson(Message message)
    {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put(SENDER, Integer.toString(message.getSenderId()));
        jsonMap.put(RECEIVER, Integer.toString(message.getReceiverId()));
        jsonMap.put(CONTENT, message.getContent());
        return convertMapToString(jsonMap);
    }
    public static Map<String, String> convertJsonToMap(String jsonString)
    {
        Map<String, String> jsonMap = new HashMap<>();
        String[] pairs = jsonString.substring(ONE, jsonString.length() - ONE).split(SEPARATOR);
        for (String pair : pairs)
        {
            String[] keyValue = pair.split(KEY_VALUE_DELIMITER);
            String key = keyValue[KEY].replaceAll(KEY_DELIMITER, EMPTY_STRING).trim();
            String value = keyValue[VALUE].replaceAll(VALUE_DELIMITER, EMPTY_STRING).trim();
            jsonMap.put(key, value);
        }
        return jsonMap;
    }
    private static String convertMapToString(Map<String, String> jsonMap)
    {
        boolean isFirst = true;
        StringBuilder jsonString = new StringBuilder();
        jsonString.append(JSON_OBJECT_START);
        for (Map.Entry<String, String> entry : jsonMap.entrySet())
        {
            if (!isFirst)
            {
                jsonString.append(SEPARATOR);
            }
            jsonString.append(KEY_DELIMITER).append(entry.getKey()).append(KEY_DELIMITER).append(KEY_VALUE_DELIMITER)
                    .append(VALUE_DELIMITER).append(entry.getValue()).append(VALUE_DELIMITER);
            isFirst = false;
        }
        jsonString.append(JSON_OBJECT_END);
        return jsonString.toString();
    }
    private static final String JSON_OBJECT_START = "{";
    private static final String JSON_OBJECT_END = "}";
    private static final String SEPARATOR = ",";
    private static final String KEY_DELIMITER = "\"";
    private static final String KEY_VALUE_DELIMITER = ":";
    private static final String VALUE_DELIMITER = "\"";
    private static final String EMPTY_STRING = "";
    private static final int KEY = 0;
    private static final int VALUE = 1;
    private static final int ONE = 1;
}