package ru.javaops.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.service.UserService;
import ru.javaops.topjava.to.UserTo;

import javax.validation.Valid;
import java.util.List;
import java.util.StringJoiner;

@RestController
@RequestMapping("/ajax/admin/users")
public class AdminUIController extends AbstractUserController {
    @Autowired
    public AdminUIController(UserService service) {
        super(service);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> createOrUpdate(@Valid UserTo userTo, BindingResult result) {
        return result.hasErrors() ? composeUnprocessableEntity(result) : createOrUpdate(userTo);
    }

    private ResponseEntity<String> createOrUpdate(UserTo userTo) {
        if (userTo.isNew()) {
            super.create(userTo);
        } else {
            super.update(userTo, userTo.id());
        }
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<String> composeUnprocessableEntity(BindingResult result) {
        var joiner = new StringJoiner("<br>");
        result.getFieldErrors().forEach(e -> {
            var message = e.getDefaultMessage();
            if (message != null) {
                joiner.add(message.startsWith(e.getField()) ? message : (e.getField() + ' ' + message));
            }
        });
        return ResponseEntity.unprocessableEntity().body(joiner.toString());
    }

    @Override
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        super.enable(id, enabled);
    }
}
