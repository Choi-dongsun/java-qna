package codesquad.user;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UserTest {
    User user1;
    User user2;

    @Before
    public void setUp() throws Exception {
        user1 = new User("finn", "finntest", "동선", "choi@naver.com");
        user2 = new User("pobi", "pobitest", "재성", "javajigi@naver.com");
        user1.setId(1);
        user2.setId(2);
    }

    @Test
    public void updateTest() {
        User modifiedUser1 = new User("finn", "finntest1", "동선1", "choi1@naver.com");
        user1.update(modifiedUser1);

        assertThat(user1.getPassword()).isEqualTo(modifiedUser1.getPassword());
        assertThat(user1.getName()).isEqualTo(modifiedUser1.getName());
        assertThat(user1.getEmail()).isEqualTo(modifiedUser1.getEmail());
    }

    @Test
    public void matchIdTest() {
        assertThat(user1.matchId(1)).isTrue();
        assertThat(user2.matchId(2)).isTrue();
    }

    @Test
    public void matchPasswordTest() {
        assertThat(user1.matchPassword("finntest")).isTrue();
        assertThat(user2.matchPassword("pobitest")).isTrue();
    }
}
