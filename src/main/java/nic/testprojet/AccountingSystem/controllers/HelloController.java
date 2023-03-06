package nic.testprojet.AccountingSystem.controllers;

import nic.testprojet.AccountingSystem.security.PersonDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public void sayHello() {
        System.out.println("hello");
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetailsImpl personDetails = (PersonDetailsImpl) authentication.getPrincipal();
        System.out.println(personDetails.getPerson());

        return "hello";
    }
}
