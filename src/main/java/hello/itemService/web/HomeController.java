package hello.itemService.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import hello.itemService.domain.member.Member;
import hello.itemService.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

  private final MemberRepository MemberRepository;
  
  @GetMapping("/")
  public String homeLogin(
    @CookieValue(name = "memberId", required = false) Long memberId,
    Model model
  ) {
    if (memberId == null) return "home";

    Member loginMember = MemberRepository.findById(memberId);
    if (loginMember == null) return "home";

    log.info("loginMember: {}", loginMember);

    model.addAttribute("member", loginMember);
    return "loginHome";
  }
}
