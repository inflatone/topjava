package ru.javaops.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class JpaUserRepository implements UserRepository {
   /*
   @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.openSession();
    }
    */

    @PersistenceContext
    private EntityManager manager;


    @Override
    public User save(User user) {
        return user.isNew() ? persist(user) : manager.merge(user);
    }

    private User persist(User user) {
        manager.persist(user);
        return user;
    }

    @Override
    public boolean delete(int id) {
        /*
        User ref = manager.getReference(User.class, id);
        manager.remove(ref);
        */
        Query query = manager.createQuery("DELETE FROM User u WHERE u.id=:id");
        return query.setParameter("id", id).executeUpdate() != 0;
    }

    @Override
    public User get(int id) {
        return manager.find(User.class, id);
    }

    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
