package com.ail.narad.config;

import com.ail.narad.config.liquibase.AsyncSpringLiquibase;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.inject.Inject;
import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableJpaRepositories("com.ail.narad.repository")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
@EnableElasticsearchRepositories("com.ail.narad.repository.search")
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Inject
    private Environment env;

    @Autowired(required = false)
    private MetricRegistry metricRegistry;



    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("1280KB");
        factory.setMaxRequestSize("1280KB");
        return factory.createMultipartConfig();
    }
    @Bean
    public UrlBasedViewResolver setupViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setPrefix("/views/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnExpression("#{!environment.acceptsProfiles('cloud') && !environment.acceptsProfiles('heroku')}")
    public DataSource dataSource(DataSourceProperties dataSourceProperties, JHipsterProperties jHipsterProperties, CacheManager cacheManager) {
        log.debug("Configuring Datasource");
        if (dataSourceProperties.getUrl() == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                    " cannot start. Please check your Spring profile, current profiles are: {}",
                Arrays.toString(env.getActiveProfiles()));

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(dataSourceProperties.getDriverClassName());
        config.addDataSourceProperty("url", dataSourceProperties.getUrl());
        if (dataSourceProperties.getUsername() != null) {
            config.addDataSourceProperty("user", dataSourceProperties.getUsername());
        } else {
            config.addDataSourceProperty("user", ""); // HikariCP doesn't allow null user
        }
        if (dataSourceProperties.getPassword() != null) {
            config.addDataSourceProperty("password", dataSourceProperties.getPassword());
        } else {
            config.addDataSourceProperty("password", ""); // HikariCP doesn't allow null password
        }

        //MySQL optimizations, see https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        if ("com.mysql.jdbc.jdbc2.optional.MysqlDataSource".equals(dataSourceProperties.getDriverClassName())) {
            config.addDataSourceProperty("cachePrepStmts", jHipsterProperties.getDatasource().isCachePrepStmts());
            config.addDataSourceProperty("prepStmtCacheSize", jHipsterProperties.getDatasource().getPrepStmtCacheSize());
            config.addDataSourceProperty("prepStmtCacheSqlLimit", jHipsterProperties.getDatasource().getPrepStmtCacheSqlLimit());
        }
        if (metricRegistry != null) {
            config.setMetricRegistry(metricRegistry);
        }
        return new HikariDataSource(config);
    }
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource, DataSourceProperties dataSourceProperties,
        LiquibaseProperties liquibaseProperties) {

        // Use liquibase.integration.spring.SpringLiquibase if you don't want Liquibase to start asynchronously
        SpringLiquibase liquibase = new AsyncSpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_FAST)) {
            if ("org.h2.jdbcx.JdbcDataSource".equals(dataSourceProperties.getDriverClassName())) {
                liquibase.setShouldRun(true);
                log.warn("Using '{}' profile with H2 database in memory is not optimal, you should consider switching to" +
                    " MySQL or Postgresql to avoid rebuilding your database upon each start.", Constants.SPRING_PROFILE_FAST);
            } else {
                liquibase.setShouldRun(false);
            }
        } else {
            log.debug("Configuring Liquibase");
        }
        return liquibase;
    }

    @Bean
    public Hibernate4Module hibernate4Module() {
        return new Hibernate4Module();
    }
}
