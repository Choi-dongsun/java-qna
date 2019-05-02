package codesquad.qna.question;

import codesquad.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/form")
    public String form(HttpSession session) {
        if(!SessionUtil.isLoginUser(session)) return "redirect:/users/loginForm";
        return "/qna/form";
    }

    @PostMapping("")
    public String create(String title, String contents, HttpSession session) {
        if(!SessionUtil.isLoginUser(session)) return "redirect:/users/loginForm";
        questionRepository.save(new Question(SessionUtil.getUserFromSession(session).getUserId(), title, contents));
        return "redirect:/";
    }

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
        return "/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("question" , questionRepository.findById(id).orElseThrow(IllegalArgumentException::new));
        return "/qna/show";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
        if(!SessionUtil.isLoginUser(session)) return "redirect:/users/loginForm";
        if(!SessionUtil.getUserFromSession(session).matchId(id)) {
            throw new IllegalStateException("You can't access other user's info");
        }

        model.addAttribute("question", questionRepository.findById(id).orElseThrow(IllegalArgumentException::new));
        return "/qna/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, Question updatedQuestion, HttpSession session) {
        if(!SessionUtil.isLoginUser(session)) return "redirect:/users/loginForm";
        if(!SessionUtil.getUserFromSession(session).matchId(id)) {
            throw new IllegalStateException("You can't access other user's info");
        }

        Question question = questionRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        question.update(updatedQuestion);
        questionRepository.save(question);
        return "redirect:/questions";
    }
}
