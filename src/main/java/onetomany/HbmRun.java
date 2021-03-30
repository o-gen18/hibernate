package onetomany;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            List<Model> models = List.of(
                    Model.of("Accent"),
                    Model.of("i40"),
                    Model.of("Solaris"),
                    Model.of("Genesis"),
                    Model.of("County")
            );
            models.forEach(session::save);

            Car hyundai = Car.of("Hyundai");
            for (int i = 1; i < 6; i++) {
                hyundai.addModel(session.load(Model.class, i));
            }

            session.save(hyundai);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
