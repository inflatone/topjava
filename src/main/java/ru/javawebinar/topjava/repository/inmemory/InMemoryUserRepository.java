package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;

    private Map<Integer, User> usersMap = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    @Override
    public User save(User user) {
        if (user.isNew()) {
            log.info("save {}", user);
            user.setId(counter.incrementAndGet());
            usersMap.put(user.getId(), user);
            return user;
        }
        log.info("update {}", user);
        return usersMap.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return usersMap.remove(id) != null;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return usersMap.get(id);
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return usersMap.values().stream().filter(user -> user.getEmail().equals(email)).findAny().orElse(null);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return usersMap.values().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }
}
