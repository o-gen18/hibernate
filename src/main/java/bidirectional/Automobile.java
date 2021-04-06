package bidirectional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "automobiles")
public class Automobile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "automobile")
    private List<Type> types = new ArrayList<>();

    public static Automobile of(String name) {
        Automobile automobile = new Automobile();
        automobile.name = name;
        return automobile;
    }

    public void addType(Type type) {
        types.add(type);
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

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Automobile automobile = (Automobile) o;
        return id == automobile.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Automobile{" +
                "id=" + id +
                ", name='" + name +"'}";
    }
}
