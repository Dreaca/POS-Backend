package lk.ijse.gdse.pos_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemDto {
    private String itemCode;
    private String desc;
    private double unitPrice;
    private int qty;
    private double totalPrice;
}
