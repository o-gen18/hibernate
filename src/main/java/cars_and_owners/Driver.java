package cars_and_owners;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "driver")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Car> cars = new HashSet<>();

    public Set<Car> getCars() {
        return cars;
    }
    public void addCar(Car car) {
        this.cars.add(car);
    }

    public static Driver of(String name) {
        Driver driver = new Driver();
        driver.name = name;
        return driver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Driver{name = " + name + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Driver driver = (Driver) o;
        return id == driver.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
