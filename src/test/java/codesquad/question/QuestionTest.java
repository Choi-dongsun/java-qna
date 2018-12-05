package codesquad.question;

import codesquad.answer.Answer;
import codesquad.user.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class QuestionTest {
    User user1;
    User user2;

    Question question1;
    Question question2;

    Answer answer1;

    List<Answer> answers = new ArrayList<>();

    @Before
    public void setUp() throws Exception  {
        user1 = new User("finn", "test", "동선", "choi@naver.com");
        user2 = new User("pobi", "test", "재성", "javajigi@naver.com");
        user1.setId(1);
        user2.setId(2);

        question1 = Question.newInstance(user1, "finn글", "finn글 내용");
        question2 = Question.newInstance(user2, "pobi글", "pobi글 내용");
        question1.setId(1);
        question2.setId(2);

        answer1 = Answer.newInstance(user1, question1, "finn글의 finn댓글");
        answer1.setId(1);
    }

    @Test
    public void updateTest() {
        Question modifiedQuestion1 = Question.newInstance(user1, "finn글1", "finn글 내용1");
        question1.update(modifiedQuestion1, user1);

        assertThat(question1.update(modifiedQuestion1, user2)).isFalse();

        assertThat(question1.update(modifiedQuestion1, user1)).isTrue();
        assertThat(question1.getTitle()).isEqualTo(modifiedQuestion1.getTitle());
        assertThat(question1.getContents()).isEqualTo(modifiedQuestion1.getContents());
    }

    @Test
    public void isSameWriterTest() {
        assertThat(question1.isSameWriter(user1)).isTrue();
        assertThat(question1.isSameWriter(user2)).isFalse();

        assertThat(question2.isSameWriter(user2)).isTrue();
        assertThat(question2.isSameWriter(user1)).isFalse();
    }

    @Test // 나의 글 & 나의 댓글만
    public void deleteTest1() {
        answers.add(answer1);
        question1.setAnswers(answers);

        assertThat(question1.delete(user1)).isTrue();
    }

    @Test // 나의 글 & 나의 댓글 + 남의 댓글
    public void deleteTest2() {
        Answer answer2 = Answer.newInstance(user2, question1, "finn글의 pobi댓글");
        answer2.setId(2);

        answers.add(answer1);
        answers.add(answer2);
        question1.setAnswers(answers);

        assertThat(question1.delete(user1)).isFalse();
    }

    @Test // 나의 글 & 나의 댓글 + 삭제된 댓글
    public void deleteTest3() {
        Answer answer2 = Answer.newInstance(user2, question1, "finn글의 삭제된 pobi댓글");
        answer2.setId(2);
        answer2.setDeleted(true);

        answers.add(answer1);
        answers.add(answer2);
        question1.setAnswers(answers);

        assertThat(question1.delete(user1)).isTrue();
    }

    @Test // 남의 글
    public void deleteTest4() {
        assertThat(question1.delete(user2)).isFalse();
        assertThat(question2.delete(user1)).isFalse();
    }
}
