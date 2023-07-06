package hello.itemService.domain.item.basic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemService.domain.item.Item;
import hello.itemService.domain.item.ItemRepository;
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
  public String addForm() {
    return "basic/addForm";
  }

  @PostMapping("/add")
  public String addItem(@ModelAttribute("item") Item item, RedirectAttributes redirectAttributes) {
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

  // @PostMapping("/add")
  // public String addItemV1(
  //   @RequestParam String itemName,
  //   @RequestParam int price,
  //   @RequestParam Integer quantity,
  //   Model model
  // ) {
  //   Item item = new Item(itemName, price, quantity);
  //   Item saved = itemRepository.save(item);
  //   model.addAttribute("item", saved);
  //   return "basic/item";
  // }

  @PostConstruct
  public void init() {
    itemRepository.save(new Item("아이템A", 10_000, 2));
    itemRepository.save(new Item("아이템B", 12_000, 3));
    itemRepository.save(new Item("아이템C", 29_900, 10));
  }
}
