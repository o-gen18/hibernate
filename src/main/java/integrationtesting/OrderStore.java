package integrationtesting;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderStore {
    private BasicDataSource pool;

    public OrderStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Order save(Order order) {
        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO orders(name, description, created) VALUES(?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            ps.setString(1, order.getName());
            ps.setString(2, order.getDescription());
            ps.setTimestamp(3, order.getCreated());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                order.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public Collection<Order> findAll() {
        List<Order> rsl = new ArrayList<>();
        try (Connection con = pool.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM orders")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rsl.add(Order.of(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp("created")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    public Order findById(int id) {
        Order rsl = null;
        try (Connection con = pool.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM orders WHERE id = ?"
        )) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rsl = Order.of(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp("created")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    public Order findByName(String name) {
        Order rsl = null;
        try (Connection con = pool.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM orders WHERE name = ?"
        )) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rsl = Order.of(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp("created")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    public boolean update(Order order) {
        boolean result = false;
        try (Connection con = pool.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "UPDATE orders SET name = ?, description = ?, created = ? WHERE id = ?",
                PreparedStatement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, order.getName());
            ps.setString(2, order.getDescription());
            ps.setTimestamp(3, order.getCreated());
            ps.setInt(4, order.getId());
            int updated = ps.executeUpdate();
            result = updated == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
