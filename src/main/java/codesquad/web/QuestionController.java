package codesquad.web;

import codesquad.domain.qna.Question;
import codesquad.domain.qna.QuestionRepository;
import codesquad.domain.user.User;
import codesquad.security.SessionUtil;
import codesquad.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    private static final Logger log = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/form")
    public String form(HttpSession session) {
        if (!SessionUtil.isLoginUser(session)) return "redirect:/users/loginForm";
        return "/qna/form";
    }

    @PostMapping("")
    public String create(String title, String contents, HttpSession session) {
        log.debug("title : {}, contents : {}", title, contents);
        if (!SessionUtil.isLoginUser(session)) return "redirect:/users/loginForm";
        questionRepository.save(new Question(SessionUtil.getUserFromSession(session), title, contents));
        return "redirect:/";
    }

    @GetMapping("")
    public String list(){
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionRepository.findById(id).orElseThrow(IllegalArgumentException::new));
        return "/qna/show";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
        Question question = questionRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Result result = valid(session, question);
        if (!result.isValid()) {
            model.addAttribute("errorMessage", result.getErrorMessage());
            return "/user/login";
        }

        model.addAttribute("question", question);
        return "/qna/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, Question updatedQuestion, Model model, HttpSession session) {
        log.debug("updatedQuestion : {}", updatedQuestion);

        Question question = questionRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Result result = valid(session, question);
        if (!result.isValid()) {
            model.addAttribute("errorMessage", result.getErrorMessage());
            return "/user/login";
        }

        question.update(updatedQuestion);
        questionRepository.save(question);
        return String.format("redirect:/questions/%d", id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, Model model, HttpSession session) {
        Question question = questionRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Result result = valid(session, question);
        if (!result.isValid()) {
            model.addAttribute("errorMessage", result.getErrorMessage());
            return "/user/login";
        }

        if (!question.checkDeletePossibility()) {
            return String.format("redirect:/questions/%d", id);
        }

        question.delete();
        questionRepository.save(question);
        return "redirect:/";
    }

    private Result valid(HttpSession session, Question question) {
        if (!SessionUtil.isLoginUser(session)) {
            return Result.fail("You need login");
        }

        User loginUser = SessionUtil.getUserFromSession(session);
        if (!question.isSameWriter(loginUser)) {
            return Result.fail("You can't access other user's question");
        }

        return Result.ok(question);
    }
}