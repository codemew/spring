package com.somnathdev.eazyschool.repository;

import com.somnathdev.eazyschool.model.EazyClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EazyClassRepository  extends JpaRepository<EazyClass,Integer> {

}
