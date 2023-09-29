package request.header;

import lombok.Data;

import java.util.Optional;

@Data
public class Header {
    private final String operation;
    private final Optional<String> token;
}
