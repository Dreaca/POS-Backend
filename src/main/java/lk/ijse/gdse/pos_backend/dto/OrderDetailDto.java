package lk.ijse.gdse.pos_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailDto {
    private String orderId;
    private String customerId;
    private String itemId;
    private int qty;
    private double unitPrice;

}
