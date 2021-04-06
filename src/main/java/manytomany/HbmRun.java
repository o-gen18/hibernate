package manytomany;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Author herbertSchildt = Author.of("Herbert Schildt");
            Author bruceEckel = Author.of("Bruce Eckel");
            Author kathySierra = Author.of("Kathy Sierra");
            Author bertBates = Author.of("Bert Bates");

            Book javaCompleteReference = Book.of("Java - The Complete Reference, 9th Edition");
            javaCompleteReference.getAuthors().add(herbertSchildt);

            Book thinkingInJava = Book.of("Thinking in Java");
            thinkingInJava.getAuthors().add(bruceEckel);

            Book thinkingInCPlusPlus = Book.of("Thinking in C++");
            thinkingInCPlusPlus.getAuthors().add(bruceEckel);

            Book headFirstJava = Book.of("Head First Java");
            headFirstJava.getAuthors().add(kathySierra);
            headFirstJava.getAuthors().add(bertBates);

            Book headFirstDesignPatterns = Book.of("Head First Design Patterns");
            headFirstDesignPatterns.getAuthors().add(kathySierra);

            session.persist(javaCompleteReference);
            session.persist(thinkingInJava);
            session.persist(thinkingInCPlusPlus);
            session.persist(headFirstJava);
            session.persist(headFirstDesignPatterns);

            Book headFirstJava1 = session.load(Book.class, 4);
            session.remove(headFirstJava1);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
