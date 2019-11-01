package ru.javaops.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {
    private static final Logger log = getLogger(InMemoryUserRepository.class);

    static final int USER_ID = 1;
    static final int ADMIN_ID = 2;

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return getCollection().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return getCollection().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }
}
