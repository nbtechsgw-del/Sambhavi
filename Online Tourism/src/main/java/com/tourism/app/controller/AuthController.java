package com.tourism.app.controller;

import com.tourism.app.model.User;
import com.tourism.app.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginType", "Customer");
        model.addAttribute("loginAction", "/customer/login");
        model.addAttribute("showPersonalCode", false);
        return "login";
    }

    @GetMapping("/customer/login")
    public String customerLoginPage(Model model) {
        model.addAttribute("loginType", "Customer");
        model.addAttribute("loginAction", "/customer/login");
        model.addAttribute("showPersonalCode", false);
        return "login";
    }

    @PostMapping("/customer/login")
    public String customerLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        return loginForRole(email, password, null, User.Role.USER, "/user/dashboard", "Customer", "/customer/login", session, model);
    }

    @GetMapping("/admin/login")
    public String adminLoginPage(Model model) {
        model.addAttribute("loginType", "Admin");
        model.addAttribute("loginAction", "/admin/login");
        model.addAttribute("showPersonalCode", true);
        return "login";
    }

    @PostMapping("/admin/login")
    public String adminLogin(@RequestParam String email, @RequestParam String password,
                             @RequestParam String personalCode, HttpSession session, Model model) {
        return loginForRole(email, password, personalCode, User.Role.ADMIN, "/admin", "Admin", "/admin/login", session, model);
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        return userRepository.findByEmailAndPassword(email, password)
                .map(user -> {
                    session.setAttribute("loggedInUser", user);
                    if (user.getRole() == User.Role.ADMIN) {
                        session.invalidate();
                        return "redirect:/admin/login";
                    }
                    return "redirect:/user/dashboard";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Invalid email or password");
                    model.addAttribute("loginType", "Customer");
                    model.addAttribute("loginAction", "/customer/login");
                    model.addAttribute("showPersonalCode", false);
                    return "login";
                });
    }

    private String loginForRole(String email, String password, String personalCode, User.Role role, String successUrl, String loginType,
                                String loginAction, HttpSession session, Model model) {
        return userRepository.findByEmailAndPassword(email, password)
                .filter(user -> user.getRole() == role)
                .filter(user -> role != User.Role.ADMIN || codesMatch(user.getPersonalCode(), personalCode))
                .map(user -> {
                    session.setAttribute("loggedInUser", user);
                    return "redirect:" + successUrl;
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Invalid " + loginType.toLowerCase() + " email, password or personal code");
                    model.addAttribute("loginType", loginType);
                    model.addAttribute("loginAction", loginAction);
                    model.addAttribute("showPersonalCode", role == User.Role.ADMIN);
                    return "login";
                });
    }

    private boolean codesMatch(String savedCode, String submittedCode) {
        return savedCode != null && submittedCode != null && savedCode.equals(submittedCode.trim());
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already registered");
            return "register";
        }
        if (user.getRole() == User.Role.ADMIN) {
            if (user.getPersonalCode() == null || user.getPersonalCode().trim().isEmpty()) {
                model.addAttribute("error", "Personal code is required for admin registration");
                return "register";
            }
            user.setPersonalCode(user.getPersonalCode().trim());
        } else {
            user.setRole(User.Role.USER);
            user.setPersonalCode(null);
        }
        userRepository.save(user);
        return "redirect:/customer/login?registered";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
