package marcobehler.blog.jta;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan
@EnableTransactionManagement
public class Application {

    @Bean
    public UserRegistryXA myService() {
        return new UserRegistryXA();
    }


    @Bean(destroyMethod = "shutdown")
    public BitronixTransactionManager bitronixManager() {
        return TransactionManagerServices.getTransactionManager();
    }


    @Bean
    public JtaTransactionManager jtaTransactionManager() {
        JtaTransactionManager jta = new JtaTransactionManager();
        jta.setTransactionManager(bitronixManager());
        jta.setUserTransaction(bitronixManager());
        return jta;
    }

    @Bean(destroyMethod = "close", name = "dataSourceOne")
    public DataSource dataSourceOne() {
        PoolingDataSource ds = new PoolingDataSource();
        // a h2 in-memory database...make sure to use the XADatasources for other databases
        ds.setClassName("org.h2.jdbcx.JdbcDataSource");
        ds.setUniqueName("ds1");
        ds.setMaxPoolSize(10);
        Properties props = new Properties();
        props.put("url", "jdbc:h2:mem:ds1");
        props.put("user", "sa");
        props.put("password", "");
        ds.setDriverProperties(props);
        ds.init();
        return ds;
    }

    @Bean(destroyMethod = "close", name = "dataSourceTwo")
    public DataSource dataSourceTwo() {
        PoolingDataSource ds = new PoolingDataSource();
        // another in-memory database...make sure to use the XADatasources for other databases
        ds.setClassName("org.h2.jdbcx.JdbcDataSource");
        ds.setUniqueName("ds2");
        ds.setMaxPoolSize(10);
        Properties props = new Properties();
        props.put("url", "jdbc:h2:mem:ds2");
        props.put("user", "sa");
        props.put("password", "");
        ds.setDriverProperties(props);
        ds.init();
        return ds;
    }
}
