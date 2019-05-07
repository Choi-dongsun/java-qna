package codesquad.qna.answer;

import codesquad.qna.question.Question;
import codesquad.qna.question.QuestionRepository;
import codesquad.user.User;
import codesquad.util.Result;
import codesquad.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {
    private static final Logger log = LoggerFactory.getLogger(AnswerController.class);

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping()
    public String create(@PathVariable Long questionId, String contents, HttpSession session) {
        if (!SessionUtil.isLoginUser(session)) {
            return "/users/loginForm";
        }
        User loginUser = SessionUtil.getUserFromSession(session);
        Question question = questionRepository.findById(questionId).orElseThrow(IllegalArgumentException::new);
        Answer answer = new Answer(loginUser, question, contents);

        answerRepository.save(answer);
        return String.format("redirect:/questions/%d", questionId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long questionId, @PathVariable Long id, Model model, HttpSession session) {
        log.debug("questionId : {}, answerId : {}", questionId, id);

        Answer answer = answerRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Result result = valid(session, answer);
        if (!result.isValid()) {
            model.addAttribute("errorMessage", result.getErrorMessage());
            return "/user/login";
        }

        answer.delete();
        answerRepository.save(answer);
        return String.format("redirect:/questions/%d/", questionId);
    }

    private Result valid(HttpSession session, Answer answer) {
        if (!SessionUtil.isLoginUser(session)) {
            return Result.fail("You need login");
        }

        User loginUser = SessionUtil.getUserFromSession(session);
        if (!answer.isSameWriter(loginUser)) {
            return Result.fail("You can't access other user's answer");
        }

        return Result.ok();
    }
}
