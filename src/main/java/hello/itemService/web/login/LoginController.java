package hello.itemService.web.login;

import org.springframework.boot.web.server.Cookie;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hello.itemService.domain.login.LoginService;
import hello.itemService.domain.member.Member;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
  
  private final LoginService loginService;

  @GetMapping("/login")
  public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
    return "login/loginForm";
  }

  @PostMapping("/login")
  public String login(
    @ModelAttribute("loginForm") LoginForm form,
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

    res.addCookie(new jakarta.servlet.http.Cookie("memberId", String.valueOf(loginMember.getId())));

    return "redirect:/";
  }

  @PostMapping("/logout")
  public String logout(HttpServletResponse res) {
    jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("memberId", null);
    cookie.setMaxAge(0);
    res.addCookie(cookie);
    return "redirect:/";
  }
}
