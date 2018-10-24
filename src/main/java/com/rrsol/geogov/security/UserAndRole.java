package com.rrsol.geogov.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserAndRole {
	@Autowired
	private DataAccess dataAccess;
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public ArrayList<Map<String, Object>> getUser(String username) {
        String[][] parametro = {{"municipio", "ENSENADA"}, {"usuario", username}};
        Map<String, Object> map = (Map<String, Object>) dataAccess.executeSP("fn_login", parametro, jdbcTemplate , "_geogov");
        ArrayList<Map<String, Object>> array = (ArrayList<Map<String, Object>>) map.get("#result-set-1");
        return array;
    }
	
	public ArrayList<Map<String, Object>> getRoles(String username) {
        Object[][] parametro = {{"municipio", "ENSENADA"}, {"usuario", username}};
        Map<String, Object> map = (Map<String, Object>) dataAccess.executeSP("fn_login_roles", parametro, jdbcTemplate, "_geogov");
        ArrayList<Map<String, Object>> array =  (ArrayList<Map<String, Object>>) map.get("#result-set-1");
        return array;
    }
	
    public UserDetails loadUserByUsername(String username) {
        UserDetails userDetails = null;
        try {
            ArrayList<Map<String, Object>> usuarioBD = getUser(username);
            ArrayList<Map<String, Object>> rolesDB = getRoles(username);

            if (usuarioBD.isEmpty()) {
                throw new UsernameNotFoundException("El nombre de usuario no se encuentra en la base de datos");
            }

            List<String> roles = new ArrayList<String>();
            for (int i = 0; i < rolesDB.size(); i++) {
                roles.add((String) rolesDB.get(i).get("authority"));
            }

            List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
            if (!roles.isEmpty()) {
                for (String role : roles) {
                    GrantedAuthority authority = new SimpleGrantedAuthority(role);
                    grantList.add(authority);
                }
            }

            
            String usuario = (String) usuarioBD.get(0).get("username");
            String contasena = (String) usuarioBD.get(0).get("password");
            boolean enabledBD = (boolean) usuarioBD.get(0).get("enabled");
            
            userDetails=new User(usuario, contasena, enabledBD, false, false, false, grantList);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return userDetails;
    }
}
