package protocol.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotBlank;

@JsonRootName(value = "payload")
public record LogoutResponse(@NotBlank String payload) implements Response<String> {

    public LogoutResponse() {
        this("desconectado");
    }
}