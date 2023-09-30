package request.header;

import json.annotation.JsonOptional;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public record Header(@NonNull String operation, @Nullable @JsonOptional String token) {
}
