package com.rrsol.geogov.security.config;

import com.rrsol.geogov.security.web.LoggingAccessDeniedHandler;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoggingAccessDeniedHandler accessDeniedHandler;
    
    @Autowired
    private CustomAuthentication authProvider;
    
    @Autowired
    DataSource dataSource;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(
                            "/",
                            "/js/**",
                            "/css/**",
                            "/img/**",
                            "/webjars/**").permitAll()
                    .anyRequest().authenticated()
                    .antMatchers("/sigech/**").hasRole("ADMIN")
                .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/?logout")
                    .permitAll()
                .and()
                .exceptionHandling()
                	.accessDeniedHandler(accessDeniedHandler);
    }

/*    @Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		
    	ShaPasswordEncoder encoder = new ShaPasswordEncoder(1);
    	
    	auth.jdbcAuthentication()
		.passwordEncoder(encoder)
		.dataSource(dataSource)
		.usersByUsernameQuery("select username,password,enabled from _geogov.fn_login('ENSENADA',?)")
		.authoritiesByUsernameQuery("select username,authority as role FROM _geogov.fn_login_roles('ENSENADA',?)");
    }*/
    
    /*@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}*/
    
    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}
}