package ru.javaops.topjava.repository;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class JpaUtil {
    @PersistenceContext
    private EntityManager manager;

    public void clear2ndLevelHibernateCache() {
        var session = (Session) manager.getDelegate();
        var sessionFactory = session.getSessionFactory();
//        sessionFactory.getCache().evict(User.class);
//        sessionFactory.getCache().evictEntityData(User.class, AbstractBaseEntity.START_SEQ);
//        sessionFactory.getCache().evictEntityData(User.class);
        sessionFactory.getCache().evictAllRegions();
    }
}