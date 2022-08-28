package com.somnathdev.eazyschool.service;

import com.somnathdev.eazyschool.constants.EazySchoolConstants;
import com.somnathdev.eazyschool.model.Contact;
import com.somnathdev.eazyschool.model.Person;
import com.somnathdev.eazyschool.model.Roles;
import com.somnathdev.eazyschool.repository.ContactRepository;
import com.somnathdev.eazyschool.repository.PersonRepository;
import com.somnathdev.eazyschool.repository.RolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean createNewPerson(Person person){
        boolean isSaved = false;
        Roles role = rolesRepository.getByRoleName(EazySchoolConstants.STUDENT_ROLE);
        person.setRoles(role);

        log.info("********************************************************************************************");
        log.info("Role role = "+role.toString());
        log.info("Person role = "+person.getRoles());

        person.setPwd(passwordEncoder.encode(person.getPwd()));
        person = personRepository.save(person);
        if (null != person && person.getPersonId() > 0)
        {
            isSaved = true;
        }
        return isSaved;
    }
}
