package request.payload;

import lombok.NonNull;


public record LoginPayload(@NonNull String email, @NonNull String password) {
}
