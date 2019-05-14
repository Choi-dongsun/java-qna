package codesquad.web;

import codesquad.domain.user.User;
import codesquad.domain.user.UserRepository;
import codesquad.util.Result;
import codesquad.security.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static codesquad.security.SessionUtil.USER_SESSION_KEY;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/form")
    public String form() {
        return "/user/form";
    }

    @PostMapping("")
    public String create(User user) {
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "/user/login";
    }

    @PostMapping("/login")
    public String login(User inputUser, HttpSession session) {
        log.debug("updatedUser : {}", inputUser);
        User user = userRepository.findByUserId(inputUser.getUserId()).orElse(null);
        if(user == null || !user.matchPassword(inputUser)) {
            return "redirect:/users/loginForm";
        }
        session.setAttribute(USER_SESSION_KEY, user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(USER_SESSION_KEY);
        return "redirect:/";
    }

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/user/list";
    }

    @GetMapping("/{id}")
    public String profile(@PathVariable Long id, Model model) {
        model.addAttribute("user", userRepository.findById(id).orElseThrow(IllegalArgumentException::new));
        return "/user/profile";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Result result = valid(session, user);
        if (!result.isValid()) {
            model.addAttribute("errorMessage", result.getErrorMessage());
            return "/user/login";
        }

        model.addAttribute("user", user);
        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, User updatedUser, Model model, HttpSession session) {
        log.debug("updatedUser : {}", updatedUser);

        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Result result = valid(session, user);
        if (!result.isValid()) {
            model.addAttribute("errorMessage", result.getErrorMessage());
            return "/user/login";
        }

        user.update(updatedUser);
        userRepository.save(user);
        return "redirect:/users";
    }

    private Result valid(HttpSession session, User user) {
        if (!SessionUtil.isLoginUser(session)) {
            return Result.fail("You need login");
        }

        User loginUser = SessionUtil.getUserFromSession(session);
        if (!user.isSameUser(loginUser)) {
            return Result.fail("You can't access other user's info");
        }

        return Result.ok(user);
    }
}
