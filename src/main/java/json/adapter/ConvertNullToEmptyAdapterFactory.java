package json.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.emptyList;

public final class ConvertNullToEmptyAdapterFactory
        implements TypeAdapterFactory {

    public static final TypeAdapterFactory FACTORY = new ConvertNullToEmptyAdapterFactory();

    private ConvertNullToEmptyAdapterFactory() {
    }

    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        @SuppressWarnings("unchecked") final Class<T> rawType = (Class<T>) typeToken.getRawType();
        if (getOptionalFields(rawType).isEmpty()) {
            return null;
        }
        final TypeAdapter<T> delegateTypeAdapter = gson.getDelegateAdapter(this, typeToken);
        final TypeAdapter<JsonElement> jsonElementTypeAdapter = gson.getAdapter(JsonElement.class);
        return PopulateOptionalWithEmpty.of(delegateTypeAdapter, jsonElementTypeAdapter);
    }

    private Collection<Field> getOptionalFields(Class<?> clazz) {
        if (clazz.isPrimitive() || clazz == Object.class) {
            return emptyList();
        }
        final Collection<Field> optionalFields = new ArrayList<>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            for (final Field f : c.getDeclaredFields()) {
                if (f.trySetAccessible() && !Modifier.isStatic(f.getModifiers()) && f.getType() == Optional.class) {
                    f.setAccessible(true);
                    optionalFields.add(f);
                }
            }
        }
        return optionalFields;
    }

    private static final class PopulateOptionalWithEmpty<T>
            extends TypeAdapter<T> {

        // This is for the cache below
        private final TypeAdapter<T> delegateTypeAdapter;
        private final TypeAdapter<JsonElement> jsonElementTypeAdapter;

        private PopulateOptionalWithEmpty(final TypeAdapter<T> delegateTypeAdapter,
                                          final TypeAdapter<JsonElement> jsonElementTypeAdapter) {
            this.delegateTypeAdapter = delegateTypeAdapter;
            this.jsonElementTypeAdapter = jsonElementTypeAdapter;
        }

        private static <T> TypeAdapter<T> of(final TypeAdapter<T> delegateTypeAdapter,
                                             final TypeAdapter<JsonElement> jsonElementTypeAdapter) {
            return new PopulateOptionalWithEmpty<>(delegateTypeAdapter, jsonElementTypeAdapter);
        }

        @Override
        public void write(final JsonWriter jsonWriter, final T t)
                throws IOException {
            delegateTypeAdapter.write(jsonWriter, t);
        }

        @Override
        public T read(final JsonReader jsonReader)
                throws IOException {
            // First, convert it to a tree to obtain its keys
            final JsonElement jsonElement = jsonElementTypeAdapter.read(jsonReader);
            // Then validate
            // And if the validation passes, then just convert the tree to the object

            T element = delegateTypeAdapter.read(new JsonTreeReader(jsonElement));
            validate(element);
            return element;
        }

        private void validate(final T element) {
            for (Field f :
                    element.getClass().getDeclaredFields()) {
                try {
                    if (f.trySetAccessible() && f.getType() == Optional.class && f.get(element) == null) {
                        f.setAccessible(true);
                        f.set(element, Optional.empty());
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}


