package bidirectional;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "types")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "automobile_id")
    private Automobile automobile;

    public static Type of(String name, Automobile automobile) {
        Type type = new Type();
        type.name = name;
        type.automobile = automobile;
        return type;
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

    public Automobile getAutomobile() {
        return automobile;
    }

    public void setAutomobile(Automobile automobile) {
        this.automobile = automobile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Type type = (Type) o;
        return id == type.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name +"'}";
    }
}
