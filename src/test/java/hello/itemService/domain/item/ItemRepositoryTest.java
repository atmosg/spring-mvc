package hello.itemService.domain.item;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ItemRepositoryTest {
  ItemRepository itemRepository = new ItemRepository();

  @AfterEach
  void afterEach() {
    itemRepository.clearStore();
  }

  @Test
  @DisplayName("물품 저장 테스트")
  void save() {
    Item item = new Item("아이템A", 10_000, 2);
    Item savedItem = itemRepository.save(item);

    Item foundItem = itemRepository.findbyId(item.getId());

    Assertions.assertThat(savedItem)
      .isEqualTo(foundItem)
      .isEqualTo(item);
  }

  @Test
  @DisplayName("모든 물품 조회 테스트")
  void findAll() {
    Item item1 = new Item("아이템A", 10_000, 2);
    Item item2 = new Item("아이템B", 20_000, 4);

    itemRepository.save(item1);
    itemRepository.save(item2);

    List<Item> foundList = itemRepository.findAll();
    Assertions.assertThat(foundList).contains(item1, item2);
  }

  @Test
  @DisplayName("물품 수정 테스트")
  void updateItem() {
    Item item = new Item("아이템A", 10_000, 2);
    
    Item savedItem = itemRepository.save(item);
    Long itemId = savedItem.getId();

    Item updatedParam = new Item("아이템A", 12_000, 3);
    itemRepository.uptdate(itemId, updatedParam);
    Item updatedItem = itemRepository.findbyId(itemId);

    Assertions.assertThat(updatedItem.getId()).isEqualTo(itemId);
    Assertions.assertThat(updatedParam.getItemName()).isEqualTo(updatedItem.getItemName());
    Assertions.assertThat(updatedParam.getPrice()).isEqualTo(updatedItem.getPrice());
    Assertions.assertThat(updatedParam.getQuantity()).isEqualTo(updatedItem.getQuantity());
  }
}
