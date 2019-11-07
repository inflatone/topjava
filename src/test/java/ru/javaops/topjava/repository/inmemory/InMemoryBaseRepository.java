package ru.javaops.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javaops.topjava.model.AbstractBaseEntity;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.javaops.topjava.model.AbstractBaseEntity.START_SEQ;

@Repository
public class InMemoryBaseRepository<T extends AbstractBaseEntity> {
    private static AtomicInteger counter = new AtomicInteger(START_SEQ);

    Map<Integer, T> storage = new ConcurrentHashMap<>();

    public T save(T entry) {
        Objects.requireNonNull(entry, "Entry must not be null");
        if (entry.isNew()) {
            entry.setId(counter.incrementAndGet());
            storage.put(entry.getId(), entry);
            return entry;
        }
        return storage.computeIfPresent(entry.getId(), (id, oldT) -> entry);
    }

    public boolean delete(int id) {
        return storage.remove(id) != null;
    }

    public T get(int id) {
        return storage.get(id);
    }

    Collection<T> getCollection() {
        return storage.values();
    }
}
