package hello.itemService.web.validation.form;

import java.util.List;

import org.hibernate.validator.constraints.Range;

import hello.itemService.domain.item.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemUpdateForm {

  @NotNull
  private Long id;
  
  @NotBlank
  private String itemName;

  @NotNull
  @Range(min = 1_000, max = 1_000_000)
  private Integer price;

  @NotNull
  @Range(min = 1)
  private Integer quantity;

  private Boolean open;
  private List<String> regions;
  private ItemType itemType;
  private String deliveryCode;
}



