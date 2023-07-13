package hello.itemService.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import hello.itemService.domain.member.Member;
import hello.itemService.domain.member.MemberRepository;
import hello.itemService.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

  private final MemberRepository MemberRepository;
  private final SessionManager sessionManager;
  
  @GetMapping("/")
  public String homeLogin(
    HttpServletRequest req,
    Model model
  ) {
    Member member = (Member) sessionManager.getSession(req);
    if (member == null) return "home";
    
    model.addAttribute("member", member);
    return "loginHome";
  }
}
