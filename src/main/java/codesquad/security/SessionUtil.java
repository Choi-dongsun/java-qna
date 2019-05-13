package codesquad.security;

import codesquad.domain.user.User;

import javax.servlet.http.HttpSession;

public class SessionUtil {
    public static final String USER_SESSION_KEY = "loginUser";

    //로그인 판별
    public static boolean isLoginUser(HttpSession session) {
        return session.getAttribute(USER_SESSION_KEY) != null;
    }

    //로그인 판별 후, 유저를 세션에서 꺼내온다
    public static User getUserFromSession(HttpSession session) {
        if(!isLoginUser(session)) {
            return null;
        }
        return (User) session.getAttribute(USER_SESSION_KEY);
    }
}
