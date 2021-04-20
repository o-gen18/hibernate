package cars_and_owners;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("carsEnginesDrivers.cfg.xml").build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Engine benzine = Engine.of("Benzine");
            session.persist(benzine);

            Engine diesel = Engine.of("Diesel");
            session.persist(diesel);

            Car bus = Car.of("Bus", diesel);
            Driver busDriver = Driver.of("BusDriver");
            bus.addDriver(busDriver);
            busDriver.addCar(bus);
            session.persist(bus);
            session.persist(busDriver);

            Car bmw = Car.of("SportCar", benzine);
            Driver bmwDriver = Driver.of("BmwDriver");
            bmw.addDriver(bmwDriver);
            bmwDriver.addCar(bmw);
            session.persist(bmw);
            session.persist(bmwDriver);

            Car mitsubishi = Car.of("Mitsubishi", diesel);
            Driver mitsubishiDriver = Driver.of("MitsubishiDriver");
            mitsubishi.addDriver(mitsubishiDriver);
            mitsubishiDriver.addCar(mitsubishi);
            session.persist(mitsubishi);
            session.persist(mitsubishiDriver);

            Driver bmwAndMitsubishiDriver = Driver.of("BmwAndMitsubishiDriver");
            bmwAndMitsubishiDriver.addCar(bmw);
            bmwAndMitsubishiDriver.addCar(mitsubishi);
            bmw.addDriver(bmwAndMitsubishiDriver);
            mitsubishi.addDriver(bmwAndMitsubishiDriver);
            session.persist(bmwAndMitsubishiDriver);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
