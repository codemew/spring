package com.eazybytes.eazyschool.security;

import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Roles;
import com.eazybytes.eazyschool.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class EazySchoolUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
       String email=authentication.getName();
       String pwd=authentication.getCredentials().toString();

       Person person=personRepository.readByEmail(email);
        log.info("Person details: "+person.toString());

        if(null!=person && person.getPersonId()>0 && passwordEncoder.matches(pwd,person.getPwd()))
            return new UsernamePasswordAuthenticationToken(email,null,getGrantedAuthorities(person.getRoles()));
        else
            throw new BadCredentialsException("Bad Credentials!");

    }

    private List<GrantedAuthority> getGrantedAuthorities(Roles roles) {
       List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
       grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+roles.getRoleName()));
       return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);

    }
}
