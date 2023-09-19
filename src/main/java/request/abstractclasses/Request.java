package request.abstractclasses;

import lombok.AllArgsConstructor;
import lombok.Data;
import request.header.Header;


@AllArgsConstructor
@Data
public abstract class Request<T extends RequestPayload> {
    Header header;
    T payload;
}
