package ru.kata.spring.boot_security.demo.controllers;



import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.UserDetailsImpl;
import ru.kata.spring.boot_security.demo.servises.RoleService;
import ru.kata.spring.boot_security.demo.servises.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.Objects;


/**
 * @author Neil Alishev
 */

@Controller
public class HelloController {


    private final RoleService roleService;
    private final UserService userService;

    private final UserValidator userValidator;

    public HelloController(RoleService roleService, UserService userService, UserValidator userValidator) {
        this.roleService = roleService;
        this.userService = userService;
        this.userValidator = userValidator;
    }



    @GetMapping("/user")
    public String userPage(Model model) {
        model.addAttribute("users", userService.index());
        model.addAttribute("currentUser", userService.findCurrentUser());
        return "user";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("listRoles", roleService.index());

        return "registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user, Model model,
                                        BindingResult bindingResult) {
        model.addAttribute("listRoles", roleService.index());
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            return "registration";
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("users", userService.index());
        model.addAttribute("listRoles", roleService.index());
        model.addAttribute("currentUser", userService.findCurrentUser());
        return "admin";
    }

    @PostMapping("/admin")
    public String updatePage(User user) {
        if (Objects.equals(user.getPassword(), "")) {
            String password = userService.findById(user.getId()).getPassword();
            user.setPassword(password);
            userService.update(user.getId(), user);
        } else {
            userService.save(user);
        }

        return "redirect:/admin";

    }

    @DeleteMapping("/admin/delete/{id}")
    public String deletePage(@PathVariable("id") int id) {
        System.out.println(id);
       userService.delete(id);

        return "redirect:/admin";

    }

    @GetMapping("/admin/edit/{id}")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.show(id));
        model.addAttribute("listRoles", roleService.index());
        return "edit";
    }

    @PatchMapping("/admin/edit/{id}")
    public String update(User user, @PathVariable("id") int id) {
        System.out.println(user);
        userService.update(id, user);
        return "redirect:/admin";
    }



    @GetMapping("/admin/new")
    public String adminNewPage(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("users", userService.index());
        model.addAttribute("listRoles", roleService.index());
        model.addAttribute("currentUser", userService.findCurrentUser());
        return "admin-new";
    }

    @PostMapping("/admin/new")
    public String adminNew(User user) {
        userService.save(user);
        return "redirect:/admin";
    }
}
