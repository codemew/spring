package com.somnathdev.eazyschool.controller;

import com.somnathdev.eazyschool.model.Courses;
import com.somnathdev.eazyschool.model.EazyClass;
import com.somnathdev.eazyschool.model.Person;
import com.somnathdev.eazyschool.repository.CoursesRepository;
import com.somnathdev.eazyschool.repository.EazyClassRepository;
import com.somnathdev.eazyschool.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    EazyClassRepository eazyClassRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CoursesRepository coursesRepository;

    @RequestMapping(value = "/displayClasses")
    public ModelAndView displayClasses(Model model){
        List<EazyClass> eazyClasses = eazyClassRepository.findAll();
       ModelAndView modelAndView=new ModelAndView("classes.html");
        modelAndView.addObject("eazyClass", new EazyClass());
        modelAndView.addObject("eazyClasses",eazyClasses);

        modelAndView.addObject("eazyClass", new EazyClass());
       return modelAndView;
    }



    @RequestMapping(value = "/displayCourses")
    public ModelAndView displayCourses(Model model){

        List<Courses> courses = coursesRepository.findAll();
        ModelAndView modelAndView=new ModelAndView("courses_secure.html");
        modelAndView.addObject("courses",courses);
        modelAndView.addObject("course",new Courses());
        return modelAndView;
    }

    @RequestMapping("/addNewClass")
    public ModelAndView addNewClass(Model model, @ModelAttribute("eazyClass") EazyClass eazyClass){
        eazyClassRepository.save(eazyClass);
        ModelAndView modelAndView=new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }


    @RequestMapping("/deleteClass")
    public ModelAndView deleteClass(Model model, @RequestParam int id){

        Optional<EazyClass> eazyClass = eazyClassRepository.findById(id);
        for(Person person:eazyClass.get().getPersons())
        {
            person.setEazyClass(null);
            personRepository.save(person);
        }
        eazyClassRepository.deleteById(id);
        ModelAndView modelAndView= new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }
    @GetMapping("/displayStudents")
    public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession session,
                                        @RequestParam(value = "error", required = false) String error) {
        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("students.html");
        Optional<EazyClass> eazyClass = eazyClassRepository.findById(classId);
        modelAndView.addObject("eazyClass",eazyClass.get());
        modelAndView.addObject("person",new Person());
        session.setAttribute("eazyClass",eazyClass.get());
        if(error != null) {
            errorMessage = "Invalid Email entered!!";
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }

    @PostMapping("/addStudent")
    public ModelAndView addStudent(Model model, @ModelAttribute("person") Person person, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if(personEntity==null || !(personEntity.getPersonId()>0)){
            modelAndView.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId()
                    +"&error=true");
            return modelAndView;
        }
        personEntity.setEazyClass(eazyClass);
        personRepository.save(personEntity);
        eazyClass.getPersons().add(personEntity);
        eazyClassRepository.save(eazyClass);
        modelAndView.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
        return modelAndView;
    }

    @GetMapping("/deleteStudent")
    public ModelAndView deleteStudent(Model model, @RequestParam int personId, HttpSession session) {
        EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");
        Optional<Person> person = personRepository.findById(personId);
        person.get().setEazyClass(null);
        eazyClass.getPersons().remove(person.get());
        EazyClass eazyClassSaved = eazyClassRepository.save(eazyClass);
        session.setAttribute("eazyClass",eazyClassSaved);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
        return modelAndView;
    }

    @PostMapping("/addNewCourse")
    public ModelAndView addNewCourse(Model model,@ModelAttribute("course") Courses courses)
    {
        ModelAndView mav = new ModelAndView("redirect:/admin/displayCourses");
        coursesRepository.save(courses);
        return mav;

    }
}
