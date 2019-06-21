package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {
    static final int USER_ID = 1;
    static final int ADMIN_ID = 2;

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    @Override
    public User getByEmail(String email) {
        return getCollection().stream().filter(user -> user.getEmail().equals(email)).findAny().orElse(null);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return getCollection().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }
}
