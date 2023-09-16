package request;

import request.header.Header;

public abstract class Request<T> {
    public abstract Header header();
    public abstract T payload();

}
