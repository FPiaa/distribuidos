package request;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import request.header.Header;


public interface Request<T> {
    @NonNull Header header();
    @SuppressWarnings("unused")
    @Nullable T payload();


}
