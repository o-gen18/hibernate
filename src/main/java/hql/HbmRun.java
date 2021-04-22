package hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .configure("hql.cfg.xml").build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

//            List<Candidate> candidates = List.of(
//                    Candidate.of("Junior", 0, 50000),
//                    Candidate.of("Middle", 3, 80000),
//                    Candidate.of("Senior", 10, 150000)
//            );
//            candidates.forEach(session::save);

            Query<Candidate> queryList = session.createQuery("from Candidate");
            queryList.list().forEach(System.out::println);

            Query<Candidate> queryById = session.createQuery("from Candidate where id = :fId").setParameter("fId", 2);
            System.out.println(queryById.uniqueResult());

            Query<Candidate> queryByName = session.createQuery("from hql.Candidate c where c.name = :cName");
            queryByName.setParameter("cName", "Senior");
            System.out.println(queryByName.uniqueResult());

            //updating
            Query<Candidate> updateJunior = session.createQuery(
                    "update hql.Candidate c set c.name = :newName, c.experience = :newExperience, c.salary = :newSalary "
                            + "where c.name = :cName"
            );
            updateJunior
                    .setParameter("cName", "Junior")
                    .setParameter("newName", "Junior-Experienced")
                    .setParameter("newExperience", 1.5)
                    .setParameter("newSalary", 75000.50)
                    .executeUpdate();
            session.getTransaction().commit();

            System.out.println(session.createQuery("from Candidate where name = :cName")
                    .setParameter("cName", "Junior-Experienced")
                    .uniqueResult());

            //deleting
            session.beginTransaction();
            session.createQuery("delete from Candidate c where id = :dId")
                    .setParameter("dId", 1)
                    .executeUpdate();
            session.getTransaction().commit();

            Query<Candidate> queryList2 = session.createQuery("from Candidate");
            queryList2.list().forEach(System.out::println);

            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
