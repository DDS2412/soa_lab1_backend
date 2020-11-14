package soa.space_marines.services;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import soa.space_marines.models.Coordinates;
import soa.space_marines.models.SpaceMarine;


public class HibernateSessionFactoryService {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryService() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Character.class);
                configuration.addAnnotatedClass(Coordinates.class);
                configuration.addAnnotatedClass(SpaceMarine.class);

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }
        }
        return sessionFactory;
    }
}
