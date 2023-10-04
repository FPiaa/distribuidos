package protocol.response;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "payload")
public record LoginResponse(String payload) implements Response<String> {

}
