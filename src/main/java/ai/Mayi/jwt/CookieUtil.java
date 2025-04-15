package ai.Mayi.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Component
public class CookieUtil {
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); //https환경이 아니라 나중에 바뀌면 true로 변경
        cookie.setPath("/");
        cookie.setDomain("localhost"); //나중에 배포 주소로 바꿀 것.
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static String getCookieValue(HttpServletRequest request, String name) {

        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
