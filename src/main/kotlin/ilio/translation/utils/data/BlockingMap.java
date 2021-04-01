package ilio.translation.utils.data;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

public final class BlockingMap<K, V> {
    private final Map<K, LinkedTransferQueue<V>> map = new ConcurrentHashMap<>();

    public void push(@Nonnull K key, @Nonnull V value) {
        getQueue(key).add(value);
    }

    public V tryTake(@Nonnull K key) {
        return getQueue(key).poll();
    }

    public V take(@Nonnull K key) throws InterruptedException {
        return getQueue(key).take();
    }

    public V take(@Nonnull K key, long timeout, @Nonnull TimeUnit unit) throws InterruptedException {
        return getQueue(key).poll(timeout, unit);
    }

    private BlockingQueue<V> getQueue(@Nonnull K key) {
        return map.computeIfAbsent(key, k -> new LinkedTransferQueue<>());
    }
}