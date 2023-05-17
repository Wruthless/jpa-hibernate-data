package org.wruthless;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Test;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldHibernateTest {

    /*
        Create SessionFactory.
     */
    private static SessionFactory createSessionFactory() {

        Configuration configuration = new Configuration();
        // Call configuration method to add Message to as an annotated class.
        configuration.configure().addAnnotatedClass(Message.class);

        // The builder pattern helps create the service registry and configure
        // it. ServiceRegistry hosts and manages services requiring access to
        // the SessionFactory.
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        // Build a SessionFactory using the configuration and service registry.
        return configuration.buildSessionFactory(serviceRegistry);

    }

    @Test
    public void storeLoadMessage() {

        // try with resources, AutoClosable
        try( SessionFactory sessionFactory = createSessionFactory();
             Session session = sessionFactory.openSession()) {

            // Start transaction 1
            session.beginTransaction();

            Message message = new Message();
            message.setText("Hello World from Hibernate!");

            session.persist(message);

            // INSERT into MESSAGE (ID, TEXT) values (1, 'Hello World from Hibernate!')
            session.getTransaction().commit();

            // Start transaction 2
            session.beginTransaction();

            /*
                Create an instance of CriteriaQuery by calling the CriteriaBuilder
                createQuery() method. A CriteriaBuilder is used to construct criteria queries,
                compound selections, expressions, predicates, and orderings.

                A CriteriaQuery defines functionality that is specific to top-level queries.
                CriteriaBuilder and CriteriaQuery belong to the Criteria API, which allows us
                to build a query programmatically.
             */
            CriteriaQuery<Message> criteriaQuery = session.getCriteriaBuilder().createQuery(Message.class);
            criteriaQuery.from(Message.class);

            // SELECT * from MESSAGE
            List<Message> messages = session.createQuery(criteriaQuery).getResultList();

            session.getTransaction().commit();

            assertAll(
                    () -> assertEquals(1, messages.size()),
                    () -> assertEquals("Hello World from Hibernate!", messages.get(0).getText())
            );
        }
    }
}
