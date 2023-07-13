package hello.itemService.web.login;

import org.apache.tomcat.util.net.SSLSessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hello.itemService.domain.login.LoginService;
import hello.itemService.domain.member.Member;
import hello.itemService.web.session.SessionManager;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
  
  private final LoginService loginService;
  private final SessionManager sessionManager;

  @GetMapping("/login")
  public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
    return "login/loginForm";
  }

  @PostMapping("/login")
  public String login(
    @Valid @ModelAttribute("loginForm") LoginForm form,
    BindingResult bindingResult,
    HttpServletResponse res
  ) {
    if (bindingResult.hasErrors()) {
      return "login/loginForm";
    }

    Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
    log.info("login 시도 {}", loginMember);

    if (loginMember == null) {
      bindingResult.reject("loginFail", "아이디 또는 비밀번호가 올바르지 않습니다.");
      return "login/loginForm";
    }

    sessionManager.createSession(loginMember, res);

    return "redirect:/";
  }

  @PostMapping("/logout")
  public String logout(HttpServletRequest req) {
    sessionManager.expire(req);
    
    return "redirect:/";
  }
}
