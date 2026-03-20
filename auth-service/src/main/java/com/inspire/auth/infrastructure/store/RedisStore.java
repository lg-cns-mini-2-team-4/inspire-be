package com.inspire.auth.infrastructure.store;

import java.time.Duration;
import java.util.Optional;

public interface RedisStore<K, V> {
    void save(K key, V payload, Duration ttl);
    void delete(K key);
    boolean exists(K key);
    Optional<V> get(K key);
    Optional<V> getAndDelete(K key);
}
