package hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .configure("hql.cfg.xml").build();

        Candidate robert = null;

        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

//            List<Vacancy> vacancies = List.of(
//                    Vacancy.of("Junior"),
//                    Vacancy.of("Middle"),
//                    Vacancy.of("Senior")
//            );
//
//            VacanciesStorage vs = VacanciesStorage.of("Java Vacancies");
//            vacancies.forEach(vs::addVacancy);
//
//            List<Candidate> candidates = List.of(
//                    Candidate.of("Petr", 0, 50000),
//                    Candidate.of("Ivan", 3, 80000),
//                    Candidate.of("Robert", 10, 150000)
//            );
//            candidates.forEach(candidate -> candidate.setVacanciesStorage(vs));
//
//            vacancies.forEach(session::save);
//            session.save(vs);
//            candidates.forEach(session::save);

            robert = session.createQuery("select distinct c from Candidate c "
                    + "join fetch c.vacanciesStorage vs "
                    + "join fetch vs.vacancies v "
                    + "where c.name = :cName", Candidate.class
            ).setParameter("cName", "Robert")
                    .uniqueResult();

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        System.out.println(robert);
    }
}