package lk.ijse.gdse.pos_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDto {
    String customerId;
    String customerName;
    String customerAddress;
    String customerPhone;
}
