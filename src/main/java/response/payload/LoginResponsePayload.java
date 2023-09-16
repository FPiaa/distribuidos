package response.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class LoginResponsePayload extends ResponsePayload {
    @NonNull private final String token;
}
