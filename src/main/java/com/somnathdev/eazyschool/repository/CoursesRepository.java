package com.somnathdev.eazyschool.repository;

import com.somnathdev.eazyschool.model.Courses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursesRepository extends JpaRepository<Courses,Integer> {
}
