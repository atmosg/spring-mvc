package hello.itemService.domain.item.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;

import hello.itemService.domain.item.DeliveryCode;
import hello.itemService.domain.item.Item;
import hello.itemService.domain.item.ItemRepository;
import hello.itemService.domain.item.ItemType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/basic/items")
public class BasicItemController {
  private final ItemRepository itemRepository;

  @Autowired
  public BasicItemController(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }

  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasenames("messages", "errors");
    messageSource.setDefaultEncoding("utf-8");
    return messageSource;
  }

  @ModelAttribute("regions")
  public Map<String, String> regions() {
    Map<String, String> regions = new LinkedHashMap<>();
    regions.put("SEOUL", "서울");
    regions.put("BUSAN", "부산");
    regions.put("JEJU", "제주");
    return regions;
  }

  @ModelAttribute("itemTypes")
  public ItemType[] itemTypes() {
    return ItemType.values();
  }

  @ModelAttribute("deliveryCodes")
  public List<DeliveryCode> deliveryCodes() {
    List<DeliveryCode> deliveryCodes = new ArrayList<>();
    deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
    deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
    deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
    return deliveryCodes;
  }

  @GetMapping
  public String items(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "basic/items";
  }

  @GetMapping("/{itemId}")
  public String item(@PathVariable Long itemId, Model model) {
    Item item = itemRepository.findbyId(itemId);
    model.addAttribute("item", item);
    return "basic/item";
  }

  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("item", new Item());
    return "basic/addForm";
  }

  @PostMapping("/add")
  public String addItem(@ModelAttribute("item") Item item, RedirectAttributes redirectAttributes, Model model) {
    log.info("item : {}", item.toString());

    Map<String, String> errors = new HashMap<>();

    if (!StringUtils.hasText(item.getItemName())) {
      errors.put("itemName", "상품명을 입력해 주세요.");
    }

    Integer price = item.getPrice();
    Integer quantity = item.getQuantity();
    if (price == null || price < 1_000 || price > 1_000_000) {
      errors.put("price", "가격은 1,000 ~ 1,000,000원 까지 허용됩니다.");
    }

    if (quantity == null || quantity >= 9_999) {
      errors.put("quantity", "수량은 9,999개 까지 허용됩니다.");
    }

    if (price !=null && quantity !=null && price * quantity < 10_000) {
      errors.put("globalError", "주문 금액은 최소 10,000원 이상이어야 합니다.\n 현재 주문하신 금액은 " + price * quantity + "원 입니다.");
    }

    log.info("errors {}", errors);

    if (!errors.isEmpty()) {
      model.addAttribute("errors", errors);
      return "basic/addForm";
    }

    itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", item.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/basic/items/{itemId}";
  }

  @GetMapping("/{itemId}/edit")
  public String updateItem(@PathVariable Long itemId, Model model) {
    Item item = itemRepository.findbyId(itemId);
    model.addAttribute("item", item);
    return "basic/editForm";
  }

  @PostMapping("/{itemId}/edit")
  public String updateItem(
    @PathVariable Long itemId,
    @ModelAttribute("item") Item item
  ) {
    log.info("item: {}", item);
    itemRepository.uptdate(itemId, item);

    return "redirect:/basic/items/{itemId}";
  }

  @PostConstruct
  public void init() {
    itemRepository.save(new Item("아이템A", 10_000, 2));
    itemRepository.save(new Item("아이템B", 12_000, 3));
    itemRepository.save(new Item("아이템C", 29_900, 10));
  }
}
