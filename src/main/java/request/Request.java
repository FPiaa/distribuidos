package request;

import request.header.Header;

public interface Request<T> {
    Header header();
    T payload();

}
