package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import json.adapter.CheckOptionalTypeAdapterFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

public class JsonHelper {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(CheckOptionalTypeAdapterFactory.FACTORY)
            .create();

    public static <T> T fromJson(@NonNull final String json, Class<T> returnType) throws JsonSyntaxException {
        return gson.fromJson(json, returnType);
    }

    public static <T> String toJson(T object) {
        return gson.toJson(object);
    }


}
