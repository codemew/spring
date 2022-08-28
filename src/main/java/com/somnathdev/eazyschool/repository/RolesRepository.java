package com.somnathdev.eazyschool.repository;

import com.somnathdev.eazyschool.model.Contact;
import com.somnathdev.eazyschool.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

    Roles getByRoleName(String roleName);
}
