package codesquad.qna.answer;

import codesquad.qna.question.Question;
import codesquad.qna.question.QuestionRepository;
import codesquad.user.User;
import codesquad.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController // rest를 붙이면 각 메서드 반환값을 json으로 변환하여 뱉도록 만든다.
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {
    private static final Logger log = LoggerFactory.getLogger(AnswerController.class);

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping()
    public Answer create(@PathVariable Long questionId, String contents, HttpSession session) {
        if (!SessionUtil.isLoginUser(session)) {
            return null;
        }

        User loginUser = SessionUtil.getUserFromSession(session);
        Question question = questionRepository.findById(questionId).orElseThrow(IllegalArgumentException::new);
        Answer answer = new Answer(loginUser, question, contents);
        question.addAnswer();
        return answerRepository.save(answer);
    }
}
