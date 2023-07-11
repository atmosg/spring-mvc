package hello.itemService.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MessageCodesResolverTest {
  MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

  @Test
  void messageCodesResolverObject() {
    String[] resolveMessageCodes = codesResolver.resolveMessageCodes("required", "item");
    for (String code : resolveMessageCodes) {
      log.info(code);
    }
    Assertions.assertThat(resolveMessageCodes).containsExactly("required.item", "required");
  }

  @Test
  void MessageCodesResolverField() {
    String[] resolveMessageCodes = codesResolver.resolveMessageCodes(
      "required", 
      "item", 
      "itemName", 
      String.class
    );

    for (String code : resolveMessageCodes) {
      log.info(code);
    }

    Assertions.assertThat(resolveMessageCodes).containsExactly(
      "required.item.itemName",
      "required.itemName",
      "required.java.lang.String", 
      "required"
    );
  }
}
