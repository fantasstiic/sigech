package com.rrsol.geogov.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter{

    @Bean
    public DriverManagerDataSource dataSource() {
         DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
         driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
         driverManagerDataSource.setUrl("jdbc:postgresql://189.216.10.6:5433/GEOGOVDB");
         driverManagerDataSource.setUsername("postgres");
         driverManagerDataSource.setPassword("1234");
         return driverManagerDataSource;
     }    
}
