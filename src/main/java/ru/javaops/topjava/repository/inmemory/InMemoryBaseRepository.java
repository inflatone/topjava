package ru.javaops.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javaops.topjava.model.AbstractBaseEntity;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryBaseRepository<T extends AbstractBaseEntity> {
    private static AtomicInteger counter = new AtomicInteger();

    private Map<Integer, T> storage = new ConcurrentHashMap<>();

    public T save(T entry) {
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
