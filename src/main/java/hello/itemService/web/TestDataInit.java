package hello.itemService.web;

import org.springframework.stereotype.Component;

import hello.itemService.domain.item.Item;
import hello.itemService.domain.item.ItemRepository;
import hello.itemService.domain.member.Member;
import hello.itemService.domain.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestDataInit {
  
  private final ItemRepository itemRepository;
  private final MemberRepository memberRepository;

  @PostConstruct
  public void init() {
    itemRepository.save(new Item("아이템A", 10_000, 2));
    itemRepository.save(new Item("아이템B", 12_000, 3));
    itemRepository.save(new Item("아이템C", 29_900, 10));

    Member member = new Member();
    member.setLoginId("test");
    member.setPassword("test");
    member.setName("test");

    memberRepository.save(member);
  }
}
