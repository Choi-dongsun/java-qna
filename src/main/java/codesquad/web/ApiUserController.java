package codesquad.web;

import codesquad.domain.user.User;
import codesquad.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/")
public class ApiUserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("{id}")
    public User show(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
