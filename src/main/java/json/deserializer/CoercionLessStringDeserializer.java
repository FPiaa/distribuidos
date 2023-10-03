package json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public class CoercionLessStringDeserializer extends StringDeserializer {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        List<JsonToken> forbiddenTypes =
                Arrays.asList(
                        JsonToken.VALUE_NUMBER_INT,
                        JsonToken.VALUE_NUMBER_FLOAT,
                        JsonToken.VALUE_TRUE,
                        JsonToken.VALUE_FALSE);

        if (forbiddenTypes.contains(p.getCurrentToken())) {
            String message =
                    MessageFormat.format("Cannot coerce {0} to String value", p.getCurrentToken());
            throw MismatchedInputException.from(p, String.class, message);
        }
        return super.deserialize(p, ctxt);
    }
}
