package json.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.emptyList;

public final class NonOptionalTypeAdapterFactory
        implements TypeAdapterFactory {

    // The type adapter factory holds no state, so no need to instantiate it multiple times
    public static final TypeAdapterFactory FACTORY = new NonOptionalTypeAdapterFactory();

    private NonOptionalTypeAdapterFactory() {
    }

    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        final Collection<Field> notNullFields = getNotNullFields(typeToken.getRawType());
        // If no @NotNull fields found, then just tell Gson to pick the next best type adapter
        if (notNullFields.isEmpty()) {
            return null;
        }
        // If there's at least one @NotNull field, get the original type adapter
        final TypeAdapter<T> delegateTypeAdapter = gson.getDelegateAdapter(this, typeToken);
        return new NonOptionalTypeAdapter<>(delegateTypeAdapter, notNullFields);
    }

    private static Collection<Field> getNotNullFields(final Class<?> clazz) {
        // Primitive types and java.lang.Object do not have @NotNull
        if (clazz.isPrimitive() || clazz == Object.class) {
            return emptyList();
        }
        // Scan the whole hierarchy from the bottom subclass to the top superclass (except java.lang.Object we mentioned above)
        final Collection<Field> nonOptionalFields = new ArrayList<>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            for (final Field f : c.getDeclaredFields()) {
                if (f.trySetAccessible() && !Modifier.isStatic(f.getModifiers()) && f.getType() != Optional.class) {
                    f.setAccessible(true);
                    nonOptionalFields.add(f);
                }
            }
        }
        return nonOptionalFields;
    }
    static class NonOptionalTypeAdapter<T> extends TypeAdapter<T> {
        private final Collection<Field> nonOptionalFields;
        private final TypeAdapter<T> delegateTypeAdapter;

        NonOptionalTypeAdapter(TypeAdapter<T> delegateTypeAdapter, Collection<Field> nonOptionalFields) {
            this.nonOptionalFields = nonOptionalFields;
            this.delegateTypeAdapter = delegateTypeAdapter;
        }


        @Override
        public void write(final JsonWriter out, final T value)
                throws IOException {
            delegateTypeAdapter.write(out, value);
        }

        @Override
        public T read(final JsonReader in)
                throws IOException {
            try {
                // Read the value ...
                final T value = delegateTypeAdapter.read(in);
                System.out.println(value);
                // ... and make some post-processing
                for (final Field f : nonOptionalFields) {
                    if (f.getType() != Optional.class && (value == null || f.get(value) == null)) {
                        throw new MalformedJsonException(f + " has no value");
                    }
                }
                return value;
            } catch (final IllegalAccessException ex) {
                throw new IOException(ex);
            }
        }
    }

}

