package hello.itemService.validation;

import java.util.Set;

import org.junit.jupiter.api.Test;

import hello.itemService.domain.item.Item;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Slf4j
class BeanValidationTest {
  
  @Test
  void beanValidation() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    Item item = new Item(" ", 0, 10_000);
    
    Set<ConstraintViolation<Item>> violations = validator.validate(item);
    for (ConstraintViolation<Item> violation: violations) {
      log.info(violation.toString() + "\n");
    }
  }
}
