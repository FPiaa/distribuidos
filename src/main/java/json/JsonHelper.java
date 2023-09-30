package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json.adapter.CheckOptionalTypeAdapterFactory;

public interface JsonHelper {
    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(CheckOptionalTypeAdapterFactory.FACTORY)
            .create();
}
