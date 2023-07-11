package hello.itemService.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import hello.itemService.domain.item.Item;

@Component
public class ItemValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Item.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Item item = (Item) target;
    Integer price = item.getPrice();
    Integer quantity = item.getQuantity();

    ValidationUtils.rejectIfEmptyOrWhitespace(
      errors,
      "itemName",
      "required"
    );

    if (price == null || price < 1_000 || price > 1_000_000) {
      errors.rejectValue("price", "range", new Object[]{1_000, 1_000_000}, null);
    }

    if (quantity == null || quantity >= 9_999) {
      errors.rejectValue("quantity", "max", new Object[]{9999}, null);
    }

    if (price !=null && quantity !=null && price * quantity < 10_000) {
      errors.reject("totalPriceMin", new Object[]{10_000, price * quantity}, null);
    }
  }
}
