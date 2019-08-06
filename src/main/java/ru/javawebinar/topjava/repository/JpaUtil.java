package ru.javawebinar.topjava.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class JpaUtil {
    @PersistenceContext
    private EntityManager em;

    public void clear2ndLevelHibernateCache() {
        Session s = (Session) em.getDelegate();
        SessionFactory factory = s.getSessionFactory();
        // factory.getCache().evict(User.class);
        // factory.getCache().evictEntity(User.class, AbstractBaseEntity.START_SEQ);
        // factory.getCache().evictEntityRegion(User.class);
        factory.getCache().evictAllRegions();
    }
}
