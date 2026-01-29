package org.example.weatherapp.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@RequiredArgsConstructor
public class HibernateConfig {

    private static final String HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    private static final String HIBERNATE_HIGHLIGHT_SQL = "hibernate.highlight_sql";
    private static final String HIBERNATE_HBM_2_DDL_AUTO = "hibernate.hbm2ddl.auto";

    private final Environment env;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        config.setJdbcUrl(env.getProperty("jdbc.url"));
        config.setUsername(env.getProperty("jdbc.username"));
        config.setPassword(env.getProperty("jdbc.password"));

        return new HikariDataSource(config);
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("org.example.weatherapp");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);

        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(HIBERNATE_DIALECT, env.getProperty(HIBERNATE_DIALECT));
        properties.put(HIBERNATE_SHOW_SQL, env.getProperty(HIBERNATE_SHOW_SQL));
        properties.put(HIBERNATE_FORMAT_SQL, env.getProperty(HIBERNATE_FORMAT_SQL));
        properties.put(HIBERNATE_HIGHLIGHT_SQL, env.getProperty(HIBERNATE_HIGHLIGHT_SQL));
        properties.put(HIBERNATE_HBM_2_DDL_AUTO, env.getProperty(HIBERNATE_HBM_2_DDL_AUTO));

        return properties;
    }

}
