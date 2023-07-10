package hello.itemService.messages;

import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MessageSourceTests {
  
  @Autowired
  private MessageSource ms;

  @Test
  void helloMessage() {
    log.info("MessageSource란? {}", ms);
    String message = ms.getMessage("hello", null, null, null);
    Assertions.assertThat(message).isEqualTo("안녕");
  }

  @Test
  void notFoundMessageCode() {
    Assertions.assertThatThrownBy(() -> ms.getMessage("notfound", null, null))
      .isInstanceOf(NoSuchMessageException.class);
  };

  @Test
  void notFoundMesssageCodeDefaultMessage() {
    String message = ms.getMessage("notfound", null, "기본 메시지", null);
    Assertions.assertThat(message).isEqualTo("기본 메시지");
  }

  @Test
  void argumentMessage() {
    String message = ms.getMessage("hello.name", new Object[] {"Spring", "너무귀찮다"}, null, null);
    Assertions.assertThat(message)
      .isEqualTo("안녕 Spring")
      .isNotEqualTo("안녕 너무귀찮다");
  }

  @Test
  void languateCheck() {
    String message = ms.getMessage("hello", null, Locale.US);
    Assertions.assertThat(message)
      .isEqualTo("hello")
      .isNotEqualTo("안녕");
  }
}
