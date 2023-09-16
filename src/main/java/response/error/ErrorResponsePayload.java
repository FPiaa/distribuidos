package response.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import response.payload.ResponsePayload;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class ErrorResponsePayload extends ResponsePayload {
    @NonNull private int code;
    @NonNull private String message;
}
