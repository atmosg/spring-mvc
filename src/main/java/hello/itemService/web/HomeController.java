package hello.itemService.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import hello.itemService.domain.member.Member;
import hello.itemService.domain.member.MemberRepository;
import hello.itemService.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
  // private final MemberRepository MemberRepository;
  // private final SessionManager sessionManager;
  
  @GetMapping("/")
  public String homeLogin(
    @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
    Model model
  ) {
    if (loginMember == null) {
      return "home";
    }
    
    model.addAttribute("member", loginMember);
    return "loginHome";
  }
}
