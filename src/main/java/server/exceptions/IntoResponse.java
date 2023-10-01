package server.exceptions;

import protocol.response.ErrorResponse;

public interface IntoResponse {
    ErrorResponse intoResponse();

}
