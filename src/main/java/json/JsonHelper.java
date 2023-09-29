package json;

import json.adapter.ConvertNullToEmptyAdapterFactory;
import json.adapter.NonOptionalTypeAdapterFactory;
import json.adapter.OptionalTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface JsonHelper {
    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(NonOptionalTypeAdapterFactory.FACTORY)
            .registerTypeAdapterFactory(OptionalTypeAdapterFactory.FACTORY)
            .registerTypeAdapterFactory(ConvertNullToEmptyAdapterFactory.FACTORY)
            .create();
}
