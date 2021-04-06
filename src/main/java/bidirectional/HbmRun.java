package bidirectional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class HbmRun {
    public static void main(String[] args) {
        List<Automobile> automobiles = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Automobile hyundai = Automobile.of("Hyundai");

            List<Type> types = List.of(
                    Type.of("Accent", hyundai),
                    Type.of("i40", hyundai),
                    Type.of("Solaris", hyundai),
                    Type.of("Genesis", hyundai),
                    Type.of("County", hyundai)
            );

            types.forEach(hyundai::addType);

            types.forEach(session::save);

            session.save(hyundai);

            automobiles = session.createQuery("select distinct car from bidirectional.Automobile as car join fetch car.types")
                    .list();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        for (Automobile automobile : automobiles) {
            for (Type type : automobile.getTypes()) {
                System.out.println(type);
            }
        }
    }
}
