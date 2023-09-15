package request.header;

import lombok.NonNull;

public record Header(@NonNull String operation, String token) {
}
