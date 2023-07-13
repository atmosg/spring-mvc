package hello.itemService.web.session;

import java.util.Date;
import java.util.Enumeration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SessionController {
  
  @GetMapping("/session-info")
  public String sessionInfo(HttpServletRequest req) {
    
    HttpSession session = req.getSession(false);
    if (session == null) return "No Session";

    session.getAttributeNames().asIterator()
      .forEachRemaining((name) -> log.info(
        "\nname: {}\nvalue: {}", name, session.getAttribute(name)
      )
    );
    
    log.info("sessionId={}", session.getId());
    log.info("maxInactiveInterval={}", session.getMaxInactiveInterval());
    log.info("creationTime={}", new Date(session.getCreationTime()));
    log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
    log.info("isNew={}", session.isNew());

    return "Session Completed";
  }
}
