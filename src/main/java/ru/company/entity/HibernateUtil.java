package ru.company.entity;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();

        } catch (HibernateException ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new RuntimeException("Configuration problem: " + ex.getMessage(), ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
