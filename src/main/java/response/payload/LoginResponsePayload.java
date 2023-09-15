package response.payload;

import lombok.NonNull;

public record LoginResponsePayload(@NonNull String token)  {
}
