package hello.itemService.web.validation.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hello.itemService.domain.member.Member;
import hello.itemService.domain.member.MemberRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/members")
public class MemberController {
  private final MemberRepository memberRepository;

  @Autowired
  public MemberController(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @GetMapping("/add")
  public String addForm(@ModelAttribute("member") Member member) {
    return "/members/addMemberForm";
  }

  @PostMapping("/add")
  public String save(@Valid @ModelAttribute("member") Member member, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "members/addMemberForm";
    }

    memberRepository.save(member);
    return "redirect:/";
  }

}
