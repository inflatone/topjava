package ru.javaops.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = getLogger(InMemoryUserRepository.class);

    @Override
    public User save(User user) {
        log.info("save {}", user);
        return user;
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return true;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return null;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return null;
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return Collections.emptyList();
    }
}
