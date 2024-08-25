package lk.ijse.gdse.pos_backend.persistence;

import lk.ijse.gdse.pos_backend.dto.OrderDetailDto;

import java.sql.Connection;
import java.sql.SQLException;

public final class OrderDetailDataProcess implements OrderDetailData {

    static String SAVE_ORDER_DETAIL = "INSERT INTO orderDetails (orderId,customerId,itemId,qty,unitPrice) VALUES (?,?,?,?,?)";
    @Override
    public boolean saveOrderDetail(Connection connection, OrderDetailDto orderDetailDto) throws SQLException {
        try(var pstm = connection.prepareStatement(SAVE_ORDER_DETAIL)){
            pstm.setString(1, orderDetailDto.getOrderId());
            pstm.setString(2, orderDetailDto.getCustomerId());
            pstm.setString(3, orderDetailDto.getItemId());
            pstm.setInt(4, orderDetailDto.getQty());
            pstm.setDouble(5, orderDetailDto.getUnitPrice());
           return pstm.executeUpdate()!=0;
        } catch (SQLException e) {
            throw e;
        }
    }
}
