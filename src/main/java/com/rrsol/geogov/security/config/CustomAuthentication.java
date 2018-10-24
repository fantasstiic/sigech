package com.rrsol.geogov.security.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.rrsol.geogov.security.UserAndRole;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;


@Component
public class CustomAuthentication implements AuthenticationProvider{
	
	@Autowired
	private UserAndRole userAndRole;
	
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		
		UserDetails userDetails = userAndRole.loadUserByUsername(auth.getName());
		
        String passAuth ="";
		try {
			passAuth = sha1(auth.getCredentials().toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        boolean flagAuth = (userDetails.getPassword().equals(passAuth)) ? true : false ; 
        
        if (userDetails.isEnabled()) {
            if (userDetails != null && flagAuth) {
            	if(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) == false) {
            		 throw new BadCredentialsException("Acceso denegado, no cuentas con permisos suficientes, contacta con tu administrador.");
            	}
            	return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            }
            throw new BadCredentialsException("Usuario o contraseña invalidos.");
        } else {
            throw new DisabledException("Usuario bloqueado, contacte a su administrador.");
        }	
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        return sb.toString();
    }
	
}
