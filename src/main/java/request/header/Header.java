package request.header;

import json.annotation.JsonOptional;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
public class Header {
    @NonNull
    private final String operation;
    @Nullable
    @JsonOptional
    private final String token;
}
