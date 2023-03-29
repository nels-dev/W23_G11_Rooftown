package csis3175.w23.g11.rooftown.common;

public interface CallbackListener<T> {

    CallbackListener<Void> DO_NOTHING = (unused) -> {
    };

    void callback(T obj);
}
