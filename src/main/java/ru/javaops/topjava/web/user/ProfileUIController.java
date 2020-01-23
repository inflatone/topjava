package ru.javaops.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import ru.javaops.topjava.AuthorizedUser;
import ru.javaops.topjava.service.UserService;
import ru.javaops.topjava.to.UserTo;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileUIController extends AbstractUserController {
    @Autowired
    public ProfileUIController(UserService service, UniqueMailValidator mailValidator) {
        super(service, mailValidator);
    }

    @GetMapping
    public String profile(ModelMap model, @AuthenticationPrincipal AuthorizedUser auth) {
        model.addAttribute("userTo", auth.getUserTo());
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid UserTo userTo, BindingResult result, SessionStatus status,
                                @AuthenticationPrincipal AuthorizedUser auth) {
        if (result.hasErrors()) {
            return "profile";
        }
        super.update(userTo, auth.getId());
        auth.update(userTo);
        status.setComplete();
        return "redirect:/meals";
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("register", true);
            return "profile";
        }
        super.create(userTo);
        status.setComplete();
        return "redirect:/login?message=app.registered&username=" + userTo.getEmail();
    }
}
