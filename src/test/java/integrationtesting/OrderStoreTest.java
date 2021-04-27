package integrationtesting;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrderStoreTest {
    private BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream("src/main/java/integrationtesting/db/update_001.sql"))
        )) {
            reader.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @After
    public void destroyTable() throws SQLException {
        pool.getConnection().prepareStatement("DROP TABLE orders").executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrderStore store = new OrderStore(pool);
        store.save(Order.of("name1", "description1"));
        List<Order> all = (List<Order>) store.findAll();
        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveOrderAndFindByIdOneRowWithInitialOrder() {
        OrderStore store = new OrderStore(pool);

        Order expected = store.save(Order.of(
                "first",
                "descriptionFirst",
                new Timestamp(1L)));

        Order result = store.findById(1);

        assertThat(result.getId(), is(1));
        assertThat(result.getName(), is("first"));
        assertThat(result.getDescription(), is("descriptionFirst"));
        assertThat(result.getCreated(), is(new Timestamp(1L)));
        assertThat(result, is(expected));
    }

    @Test
    public void whenSaveOrderAndFindByNameOneRowWithInitialOrder() {
        OrderStore store = new OrderStore(pool);

        Order expected = store.save(Order.of(
                "first",
                "descriptionFirst",
                new Timestamp(1L)));

        Order result = store.findByName("first");

        assertThat(result.getId(), is(1));
        assertThat(result.getName(), is("first"));
        assertThat(result.getDescription(), is("descriptionFirst"));
        assertThat(result.getCreated(), is(new Timestamp(1L)));
        assertThat(result, is(expected));
    }

    @Test
    public void whenSaveOrderAndUpdateItOneRowWithNewOrder() {
        OrderStore store = new OrderStore(pool);

        Order old = store.save(Order.of(
                "first",
                "descriptionFirst",
                new Timestamp(1L)));

        Order expected = Order.of(
                1,
                "First-Updated",
                "DescUpdated",
                new Timestamp(100L));

        boolean updateSuccessful = store.update(expected);
        Order result = store.findById(1);

        assertThat(updateSuccessful, is(true));
        assertThat(result.getId(), is(1));
        assertThat(result.getName(), is("First-Updated"));
        assertThat(result.getDescription(), is("DescUpdated"));
        assertThat(result.getCreated(), is(new Timestamp(100L)));
        assertThat(result, is(expected));
    }
}
