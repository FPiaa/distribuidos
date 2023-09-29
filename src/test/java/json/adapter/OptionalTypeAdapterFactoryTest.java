package json.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OptionalTypeAdapterFactoryTest {
    private final static Gson gson = new GsonBuilder().registerTypeAdapterFactory(OptionalTypeAdapterFactory.FACTORY)
            .registerTypeAdapterFactory(ConvertNullToEmptyAdapterFactory.FACTORY).create();

    private static Stream<Arguments> provideObjects() {
        return Stream.of(
                Arguments.of(new TestClass1("", new HashMap<>())),
                Arguments.of(new TestClass1(null, new HashMap<>())),
                Arguments.of(new TestClass1("", null)),
                Arguments.of(new TestClass1(null, null))
        );
    }

    private static Stream<Arguments> provideValidJsons() {
        return IntStream.iterate(5, n -> n + 1)
                .limit(40)
                .mapToObj(x -> gson.toJson(new TestClass1(randomString(x), randomMaps(x))))
                .map(Arguments::of);
    }

    private static Stream<Arguments> provideMissingOptionalFields() {
        return classWithNulls().map(gson::toJson)
                .map(Arguments::of);
    }

    private static Stream<Arguments> provideNullInOptionalFields() {
        return classWithNulls().map(gson::toJson)
                .map(Arguments::of);
    }

    private static Stream<TestClass1> classWithNulls() {
        return IntStream.iterate(5, n -> n + 1)
                .limit(40)
                .mapToObj(x -> {
                    if (x % 2 == 0)
                        return new TestClass1(null, randomMaps(x));
                    else if (x % 3 == 0)
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

    @ParameterizedTest
    @MethodSource("provideValidJsons")
    public void givenValidJson_whenFromJson_thenReturnObject(String json) {

        assertNotNull(gson.fromJson(json, TestClass1.class));
    }

    @ParameterizedTest
    @MethodSource("provideObjects")
    public void givenObject_whenToJson_thenSerializeObject(TestClass1 o) {
        gson.toJson(o);
    }

    @ParameterizedTest
    @MethodSource("provideMissingOptionalFields")
    public void givenMissingObligatoryFieldsJson_whenFromJson_thenThrowsMalformedJsonException(String json) {
        var obj = gson.fromJson(json, TestClass1.class);
        assertNotNull(obj.field1);
        assertNotNull(obj.field1);
    }

    @ParameterizedTest
    @MethodSource("provideNullInOptionalFields")
    public void givenNullInObligatoryFieldsJson_whenFromJson_thenThrowsMalformedJsonException(String json) {
        var obj = gson.fromJson(json, TestClass1.class);
        assertNotNull(obj.field1);
        assertNotNull(obj.field1);
    }

    @Data
    public static class TestClass1 {
        private final Optional<String> field1;
        private final Optional<Map<String, Integer>> field2;

        private TestClass1(String field1, Map<String, Integer> field2) {
            this.field1 = Optional.ofNullable(field1);
            this.field2 = Optional.ofNullable(field2);
        }
    }
}
