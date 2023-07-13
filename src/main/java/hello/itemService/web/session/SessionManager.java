package hello.itemService.web.session;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SessionManager {
  public static final String SESSION_COOKIE_NAME = "mySessionId";

  private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

  public void createSession(Object value, HttpServletResponse res) {
    String sessionId = UUID.randomUUID().toString();
    sessionStore.put(sessionId, value);

    Cookie sessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
    res.addCookie(sessionCookie);
  }

  public Object getSession(HttpServletRequest req) {
    Cookie findSessionCookie = findCookie(req, SESSION_COOKIE_NAME);
    
    if (findSessionCookie == null) {
      return null;
    }

    return sessionStore.get(findSessionCookie.getValue());
  }

  public void expire(HttpServletRequest req) {
    Cookie findSessionCookie = findCookie(req, SESSION_COOKIE_NAME);
    
    if (findSessionCookie != null) {
      sessionStore.remove(findSessionCookie.getValue());
    }
  }

  private Cookie findCookie(HttpServletRequest req, String cookieName) {
    Cookie[] cookies = req.getCookies();

    if (cookies == null) return null;

    return Arrays.stream(cookies)
        .filter((cookie) -> cookie.getName().equals(cookieName))
        .findAny()
        .orElse(null);
  }
}
