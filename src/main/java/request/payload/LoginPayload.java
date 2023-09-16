package request.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;


@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class LoginPayload extends RequestPayload {
    @NonNull String email;
    @NonNull String password;
}
