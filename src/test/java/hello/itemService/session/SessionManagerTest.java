package hello.itemService.session;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import hello.itemService.domain.member.Member;
import hello.itemService.web.session.SessionManager;

public class SessionManagerTest {
  SessionManager sessionManager = new SessionManager();

  @Test
  void sessiontTest() {
    MockHttpServletResponse res = new MockHttpServletResponse();
    Member member = new Member();
    sessionManager.createSession(member, res);

    MockHttpServletRequest req = new MockHttpServletRequest();
    req.setCookies(res.getCookies());

    Object session = sessionManager.getSession(req);
    Assertions.assertThat(session).isEqualTo(member);

    sessionManager.expire(req);
    Object expired = sessionManager.getSession(req);
    Assertions.assertThat(expired).isNull();
  }
}
