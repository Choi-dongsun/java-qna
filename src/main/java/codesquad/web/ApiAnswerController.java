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
        if (!SessionUtil.isLoginUser(session)) {
            return Result.fail("You need login");
        }

        Answer answer = answerRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        User loginUser = SessionUtil.getUserFromSession(session);
        if (!answer.isSameWriter(loginUser)) {
            return Result.fail("You can't access other user's answer");
        }

        answer.delete();
        return Result.ok(answerRepository.save(answer));
    }
}
