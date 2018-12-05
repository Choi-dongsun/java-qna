package codesquad.answer;

import codesquad.question.Question;
import codesquad.user.User;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class AnswerTest {
    User user1;
    User user2;

    Question question1;
    Question question2;

    Answer answer1;
    Answer answer2;
    Answer answer3;

    @Before
    public void setUp() throws Exception  {
        user1 = new User("finn", "test", "동선", "choi@naver.com");
        user2 = new User("pobi", "test", "재성", "javajigi@naver.com");
        user1.setId(1);
        user2.setId(2);

        question1 = Question.newInstance(user1, "finn글", "finn글 내용");
        question2 = Question.newInstance(user2, "pobi글", "pobi글 내용");

        answer1 = Answer.newInstance(user1, question1, "finn글의 finn댓글");
        answer2 = Answer.newInstance(user2, question1, "finn글의 pobi댓글");
        answer3 = Answer.newInstance(user2, question2, "pobi글의 pobi댓글");
    }

    @Test
    public void deleteTestTrue() {
        assertThat(answer1.delete(user1)).isTrue();
        assertThat(answer2.delete(user2)).isTrue();
        assertThat(answer3.delete(user2)).isTrue();
    }

    @Test
    public void deleteTestFalse() {
        assertThat(answer1.delete(user2)).isFalse();
        assertThat(answer2.delete(user1)).isFalse();
        assertThat(answer3.delete(user1)).isFalse();
    }
}