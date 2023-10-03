package protocol.request;

import protocol.request.header.Header;


public interface Request<T> {
    Header header();

    T payload();


}
