package json.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import json.annotation.JsonOptional;

import java.io.IOException;
import java.lang.reflect.Field;

public class CheckOptionalTypeAdapterFactory implements TypeAdapterFactory {
    public static final TypeAdapterFactory FACTORY = new CheckOptionalTypeAdapterFactory();

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        final TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(this, typeToken);

        return new CheckOptionalTypeAdapter<>(delegateAdapter, typeToken.getRawType());
    }

    private static class CheckOptionalTypeAdapter<T> extends TypeAdapter<T> {
        final TypeAdapter<T> delegate;
        final Class<?> clazz;

        public CheckOptionalTypeAdapter(TypeAdapter<T> delegate, Class<?> clazz) {
            this.delegate = delegate;
            this.clazz = clazz;
        }

        @Override
        public void write(JsonWriter jsonWriter, T t) throws IOException {
            delegate.write(jsonWriter, t);
        }

        @Override
        public T read(JsonReader jsonReader) throws IOException {
            final T value = delegate.read(jsonReader);
            for (Class<?> c = clazz; c != null && c != Object.class; c = c.getSuperclass()) {
                for (Field field : c.getDeclaredFields()) {
                    if (field.trySetAccessible() && !field.isAnnotationPresent(JsonOptional.class)) {
                        field.setAccessible(true);
                        try {
                            if (field.get(value) == null) {
                                throw new MalformedJsonException("Obligatory field '" + field + "' is missing or is null.");
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            return value;
        }
    }
}
