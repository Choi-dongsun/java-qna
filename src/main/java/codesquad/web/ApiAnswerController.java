package codesquad.web;

import codesquad.domain.qna.Answer;
import codesquad.domain.qna.AnswerRepository;
import codesquad.domain.qna.Question;
import codesquad.domain.qna.QuestionRepository;
import codesquad.domain.user.User;
import codesquad.util.Result;
import codesquad.security.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController // rest를 붙이면 각 메서드 반환값을 json으로 변환하여 뱉도록 만든다.
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {
    private static final Logger log = LoggerFactory.getLogger(ApiAnswerController.class);

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping()
    public Result<Answer> create(@PathVariable Long questionId, String contents, HttpSession session) {
        if (!SessionUtil.isLoginUser(session)) {
            return Result.fail("You need login");
        }

        User loginUser = SessionUtil.getUserFromSession(session);
        Question question = questionRepository.findById(questionId).orElseThrow(IllegalArgumentException::new);
        Answer answer = new Answer(loginUser, question, contents);
        question.addAnswer();
        return Result.ok(answerRepository.save(answer));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long questionId, @PathVariable Long id, HttpSession session) {
        log.debug("questionId : {}, answerId : {}", questionId, id);
        Answer answer = answerRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Result result = valid(session, answer);

        if(result.isValid()) {
            answer.delete();
            answerRepository.save(answer);
        }
        return result;
    }

    @GetMapping("/{id}/form")
    public Result updateForm(@PathVariable Long id, HttpSession session, Model model) {
        Answer answer = answerRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        return valid(session, answer);
    }

    @PutMapping("/{id}/form")
    public Result<Answer> modifyAnswer(@PathVariable Long id, String contents, HttpSession session, Model model) {
        Answer answer = answerRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Result result = valid(session, answer);

        if(result.isValid()) {
            answer.update(contents);
            answerRepository.save(answer);
        }

        return result;
    }

    private Result valid(HttpSession session, Answer answer) {
        if (!SessionUtil.isLoginUser(session)) {
            return Result.fail("You need login");
        }

        User loginUser = SessionUtil.getUserFromSession(session);
        if (!answer.isSameWriter(loginUser)) {
            return Result.fail("You can't access other user's answer");
        }

        return Result.ok(answer);
    }
}
