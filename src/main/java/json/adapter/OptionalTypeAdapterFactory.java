package json.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public final class OptionalTypeAdapterFactory implements TypeAdapterFactory {

    public static final TypeAdapterFactory FACTORY = new OptionalTypeAdapterFactory();

    private OptionalTypeAdapterFactory() {}
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        @SuppressWarnings("unchecked")
        Class<T> rawType = (Class<T>) typeToken.getRawType();
        if(rawType != Optional.class) {
            return null;
        }

        final ParameterizedType pt = (ParameterizedType) typeToken.getType();
        final Type type = pt.getActualTypeArguments()[0];
        final TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        @SuppressWarnings("unchecked")
        final TypeAdapter<T> typeAdapter =  (TypeAdapter<T>) new OptionalTypeAdapter<>(adapter);
        return typeAdapter;
    }



    private static class OptionalTypeAdapter<T> extends TypeAdapter<Optional<T>> {
        private final TypeAdapter<T> adapter;
        public OptionalTypeAdapter(TypeAdapter<T> adapter) {
            this.adapter = adapter;
        }
        @Override
        public void write(JsonWriter jsonWriter, Optional<T> t) throws IOException {
            if(t.isEmpty()) {
                jsonWriter.nullValue();
            } else {
                adapter.write(jsonWriter, t.get());
            }
        }

        @Override
        public Optional<T> read(JsonReader jsonReader) throws IOException {
            final JsonToken peek = jsonReader.peek();
            if(peek != JsonToken.NULL) {
                return Optional.ofNullable(adapter.read(jsonReader));
            }
            jsonReader.nextNull();
            return Optional.empty();
        }
    }

}
