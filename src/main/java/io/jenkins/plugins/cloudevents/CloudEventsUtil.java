package io.jenkins.plugins.cloudevents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts POJO to JSON-String.
 */

public class CloudEventsUtil {

    public static String convertToJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String convertedJson = "";
        try {
            convertedJson = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return convertedJson;
    }

}
