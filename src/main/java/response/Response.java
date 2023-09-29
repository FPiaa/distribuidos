package response;

import response.payload.ResponsePayload;

public abstract class Response<T extends ResponsePayload> {
    public abstract T payload();

    public abstract String toJson();
}
