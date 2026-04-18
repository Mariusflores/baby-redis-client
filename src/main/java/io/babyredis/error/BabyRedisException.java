package io.babyredis.error;

public class BabyRedisException extends RuntimeException {
    public BabyRedisException(String message) {
        super(message);
    }
}
