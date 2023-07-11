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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

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
  public String addItem(
    @ModelAttribute("item") Item item,
    BindingResult bindingResult,
    RedirectAttributes redirectAttributes
  ) {
    log.info("objectName > {}", bindingResult.getObjectName());
    log.info("target > {}", bindingResult.getTarget());
    

    if (!StringUtils.hasText(item.getItemName())) {
      bindingResult.rejectValue("itemName", "required");
    }

    Integer price = item.getPrice();
    Integer quantity = item.getQuantity();
    if (price == null || price < 1_000 || price > 1_000_000) {
      bindingResult.rejectValue("price", "range", new Object[]{1_000, 1_000_000}, null);
    }

    if (quantity == null || quantity >= 9_999) {
      bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
    }

    if (price !=null && quantity !=null && price * quantity < 10_000) {
      bindingResult.reject("totalPriceMin", new Object[]{10_000, price * quantity}, null);
    }
    
    if (bindingResult.hasErrors()) {
      log.info("Binding Result.getTarget {}", bindingResult.getTarget());
      log.info("Binding Result.getAllErrors {}", bindingResult.getAllErrors());
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
