package codesquad.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {
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
        User user = userRepository.findByUserId(inputUser.getUserId());
        if(user == null || !user.matchPassword(inputUser)) {
            return "redirect:/users/loginForm";
        }
        session.setAttribute("loginUser", user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loginUser");
        return "redirect:/";
    }

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/user/list";
    }

    @GetMapping("/{id}")
    public String profile(@PathVariable Long id, Model model) {
        model.addAttribute("user", userRepository.findById(id).get());
        return "/user/profile";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userRepository.findById(id).get());
        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, User updatedUser) {
        User user = userRepository.findById(id).get();
        user.update(updatedUser);
        userRepository.save(user);
        return "redirect:/users";
    }
}
