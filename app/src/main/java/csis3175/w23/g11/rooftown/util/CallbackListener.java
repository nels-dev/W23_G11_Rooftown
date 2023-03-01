package csis3175.w23.g11.rooftown.util;

public interface CallbackListener<T> {

    void callback(T obj);

    CallbackListener<Void> DO_NOTHING = (unused) -> {
    };
}
