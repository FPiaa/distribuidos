package protocol.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotBlank;

@JsonRootName(value = "payload")
public record LoginResponse(@NotBlank String payload) implements Response<String> {

}
