package protocol.response;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import protocol.commons.dto.UserDTO;

import java.util.List;

public record FindUsersResponse(@NotNull @Valid Payload payload) implements Response<FindUsersResponse.Payload> {

    public FindUsersResponse(List<UserDTO> users) {
        this(new Payload(users));
    }

    public record Payload(@SerializedName(value = "usuarios") @NotNull List<@NotNull @Valid UserDTO> users) {
    }
}
