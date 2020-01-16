package ru.javaops.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import ru.javaops.topjava.service.UserService;
import ru.javaops.topjava.to.UserTo;
import ru.javaops.topjava.web.SecurityUtil;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileUIController extends AbstractUserController {
    @Autowired
    public ProfileUIController(UserService service) {
        super(service);
    }

    @GetMapping
    public String profile() {
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid UserTo userTo, BindingResult result, SessionStatus status) {
        return result.hasErrors() || !update(userTo, result, status) ? "profile" : "redirect:meals";
    }

    private boolean update(UserTo userTo, BindingResult result, SessionStatus status) {
        try {
            var authorizedUser = SecurityUtil.get();
            super.update(userTo, authorizedUser.getId());
            authorizedUser.update(userTo);
            status.setComplete();
            return true;
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("email", EXCEPTION_DUPLICATE_EMAIL);
            return false;
        }

    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model) {
        if (result.hasErrors() || !save(userTo, result, status)) {
            model.addAttribute("register", true);
            return "profile";
        }
        return "redirect:/login?message=app.registered&username=" + userTo.getEmail();
    }

    private boolean save(UserTo userTo, BindingResult result, SessionStatus status) {
        try {
            super.create(userTo);
            status.setComplete();
            return true;
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("email", EXCEPTION_DUPLICATE_EMAIL);
            return false;
        }
    }
}
