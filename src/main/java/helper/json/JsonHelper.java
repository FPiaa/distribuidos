package helper.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import helper.json.deserializer.CoercionLessStringDeserializer;

public class JsonHelper {
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new SimpleModule().addDeserializer(String.class, new CoercionLessStringDeserializer()))
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static <T> String toJson(T object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(json, clazz);
    }
}
