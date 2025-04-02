package ai.Mayi.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

public class CookieUtil {
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true); // XSS 공격 방지 (JS에서 접근 불가)
//        cookie.setSecure(true); // HTTPS에서만 사용 가능
        cookie.setPath("/"); // 모든 경로에서 사용 가능
        cookie.setMaxAge(maxAge); // 쿠키 유효기간 (초 단위)
        response.addCookie(cookie);
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        // 쿠키가 아무것도 없으면 로그인을 해야지
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
