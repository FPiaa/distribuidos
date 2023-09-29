package json.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NonOptionalTypeAdapterFactoryTest {
    private final Gson gson = new GsonBuilder().registerTypeAdapterFactory(NonOptionalTypeAdapterFactory.FACTORY).create();

    @ParameterizedTest
    @MethodSource("provideValidJsons")
    public void givenValidJson_whenFromJson_thenReturnObject(String json) {

        assert (gson.fromJson(json, TestClass1.class) != null);
    }
    @ParameterizedTest
    @MethodSource("provideMissingObligatoryFields")
    public void givenMissingObligatoryFieldsJson_whenFromJson_thenThrowsMalformedJsonException(String json) {
        assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, TestClass1.class));
    }
    @ParameterizedTest
    @MethodSource("provideNullInObligatoryFields")
    public void givenNullInObligatoryFieldsJson_whenFromJson_thenThrowsMalformedJsonException(String json) {
        assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, TestClass1.class));
    }

    private static Stream<Arguments> provideValidJsons() {
        var gson = new Gson();
        return IntStream.iterate(5, n -> n + 1)
                .limit(40)
                .mapToObj(x -> gson.toJson(new TestClass1(randomString(x), randomMaps(x))))
                .map(Arguments::of);
    }

    private static Stream<Arguments> provideMissingObligatoryFields() {
        var gson = new Gson();
        return classWithNulls().map(gson::toJson)
                .map(Arguments::of);
    }
    private static Stream<Arguments> provideNullInObligatoryFields() {
        var gson = new GsonBuilder().serializeNulls().create();
        return classWithNulls().map(gson::toJson)
                .map(Arguments::of);
    }

    private static Stream<TestClass1> classWithNulls() {
        var gson = new Gson();
        return IntStream.iterate(5, n -> n + 1)
                .limit(40)
                .mapToObj(x -> {
                    if(x % 2 == 0)
                        return new TestClass1(null, randomMaps(x));
                    else if(x % 3 == 0)
                        return new TestClass1(randomString(x), null);
                    else
                        return new TestClass1(null, null);
                });
    }

    private static Map<String, Integer> randomMaps(int size) {
        Map<String, Integer> map = new HashMap<>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            map.put(randomString(10), random.nextInt());
        }
        return map;

    }

    private static String randomString(int size) {
        int a = 'a';
        int z = 'z';

        Random random = new Random();

        return random.ints(a, z)
                .limit(size)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private static class TestClass1 {
        private final String field1;
        private final Map<String, Integer> field2;

        private TestClass1(String field1, Map<String, Integer> field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
    }
}
