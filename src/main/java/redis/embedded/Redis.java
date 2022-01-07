package redis.embedded;

import redis.embedded.exceptions.EmbeddedRedisException;

public interface Redis {
    boolean isActive();

    void start() throws EmbeddedRedisException;

    void stop() throws EmbeddedRedisException;
}
