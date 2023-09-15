package response;

import response.error.ErrorResponse;

public interface Response<T> {
    T payload();
    ErrorResponse error();
}
