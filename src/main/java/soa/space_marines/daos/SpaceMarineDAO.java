package soa.space_marines.daos;

import org.hibernate.Session;
import org.hibernate.Transaction;
import soa.space_marines.enums.AstartesCategory;
import soa.space_marines.models.SpaceMarine;
import soa.space_marines.services.HibernateSessionFactoryService;

import javax.persistence.TypedQuery;
import java.util.List;


public class SpaceMarineDAO {
    public SpaceMarine findById(Integer id){
        return HibernateSessionFactoryService.getSessionFactory().openSession().get(SpaceMarine.class, id);
    }

    public void save(SpaceMarine spaceMarine){
        try(Session session = HibernateSessionFactoryService.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(spaceMarine);
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Exception: "  + e);
        }
    }

    public void upate(SpaceMarine spaceMarine){
        try(Session session = HibernateSessionFactoryService.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(spaceMarine);
            transaction.commit();
        }
    }

    public SpaceMarine delete(SpaceMarine spaceMarine){
        try(Session session = HibernateSessionFactoryService.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(spaceMarine);
            transaction.commit();
        }

        return spaceMarine;
    }

    public List<SpaceMarine> findAll(){
        return  (List<SpaceMarine>) HibernateSessionFactoryService
                .getSessionFactory()
                .openSession()
                .createQuery("From SpaceMarine")
                .list();
    }

    public List<SpaceMarine> findByHealth(long health){
        TypedQuery<SpaceMarine> query = HibernateSessionFactoryService
                .getSessionFactory()
                .openSession()
                .createQuery("Select marine FROM SpaceMarine marine WHERE marine.health = ?1", SpaceMarine.class);
        return query.setParameter(1, health).getResultList();
    }

    public List<SpaceMarine> findSpaceMarineWhenCategoryGreater(AstartesCategory category){
        TypedQuery<SpaceMarine> query = HibernateSessionFactoryService
                .getSessionFactory()
                .openSession()
                .createQuery("Select marine FROM SpaceMarine marine WHERE marine.category > ?1", SpaceMarine.class);
        return query.setParameter(1, category).getResultList();
    }
}
