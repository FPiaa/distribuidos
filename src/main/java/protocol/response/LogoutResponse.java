package protocol.response;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "payload")
public record LogoutResponse(String payload) implements Response<String> {

    public LogoutResponse() {
        this("desconectado");
    }
}