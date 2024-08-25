package lk.ijse.gdse.pos_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto {
    private String orderId;
    private String customerId;
    private String customerName;
    private String date;
    private double total;
    private String discount;
    private double subtotal;
}
