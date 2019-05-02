package codesquad.user;

import codesquad.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static codesquad.util.SessionUtil.USER_SESSION_KEY;

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
        if(!SessionUtil.isLoginUser(session)) return "redirect:/users/loginForm";
        if(!SessionUtil.getUserFromSession(session).matchId(id)) {
            throw new IllegalStateException("You can't access other user's info");
        }

        model.addAttribute("user", userRepository.findById(id).orElseThrow(IllegalArgumentException::new));
        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, User updatedUser, HttpSession session) {
        log.debug("updatedUser : {}", updatedUser);
        if(!SessionUtil.isLoginUser(session)) return "redirect:/users/loginForm";
        if(!SessionUtil.getUserFromSession(session).matchId(id)) {
            throw new IllegalStateException("You can't access other user's info");
        }

        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        user.update(updatedUser);
        userRepository.save(user);
        return "redirect:/users";
    }
}
