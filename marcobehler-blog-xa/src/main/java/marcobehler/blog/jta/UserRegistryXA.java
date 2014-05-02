package marcobehler.blog.jta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marcobehler on 01/05/14.
 */
public class UserRegistryXA {

    private static Logger logger = LoggerFactory.getLogger(UserRegistryXA.class);

    @Autowired
    @Qualifier(value = "dataSourceOne")
    private DataSource ds1;

    @Autowired
    @Qualifier(value = "dataSourceTwo")
    private DataSource ds2;


    @Transactional
    public void registerUser(final String name, final Integer limit) {
        // let's commit the user in one database....
        new JdbcTemplate(ds1).execute("create table user (id bigint auto_increment primary key, name varchar)");
        new SimpleJdbcInsert(ds1).withTableName("user").execute(new HashMap<String, Object>() {{
            put("name", name);
        }});

        // let's assume we want to save this user's atm withdrawal limits to another database...
        // (and yes, there is no relationship or foreign key in this example :)
        new JdbcTemplate(ds2).execute("create table atm_widthdrawal_limit (id bigint auto_increment primary key, amount int )");
        new SimpleJdbcInsert(ds2).withTableName("atm_widthdrawal_limit").execute(new HashMap<String, Object>() {{
            put("amount", limit);
        }});

        logger.info("Registered user [{}] with withdrawal limit [{}]", name, limit);
    }


    // a list of maps is the poor man's pojo conversion :P
    @Transactional
    public List<Map<String, Object>> findAllUsers() {
        return new JdbcTemplate(ds1).queryForList("select * from user");
    }
}
