package lk.ijse.gdse.pos_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequestDto {
    private String orderId;
    private String customerId;
    private String date;
    private String customerName;
    private double total;
    private String discount;
    private double subTotal;

    private List<CartItemDto> cartItems;
}
