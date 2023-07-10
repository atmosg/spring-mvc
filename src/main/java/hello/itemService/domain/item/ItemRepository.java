package hello.itemService.domain.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class ItemRepository {
  private static final Map<Long, Item> store = new HashMap<>();
  private static Long sequence = 0L;

  public Item save(Item item) {
    item.setId(sequence++);
    store.put(item.getId(), item);

    return item;
  }

  public Item findbyId(Long id) {
    return store.get(id);
  }

  public List<Item> findAll() {
    return new ArrayList<>(store.values());
  }

  public void uptdate(Long itemId, Item updateParam) {
    Item found = findbyId(itemId);
    found.setItemName(updateParam.getItemName());
    found.setPrice(updateParam.getPrice());
    found.setQuantity(updateParam.getQuantity());
    found.setOpen(updateParam.getOpen());
    found.setItemType(updateParam.getItemType());
    found.setRegions(updateParam.getRegions());
    found.setDeliveryCode(updateParam.getDeliveryCode());
  }

  public void clearStore() {
    store.clear();
  }
}
