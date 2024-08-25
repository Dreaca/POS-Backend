package lk.ijse.gdse.pos_backend.persistence;

import lk.ijse.gdse.pos_backend.dto.OrderDetailDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public sealed interface OrderDetailData permits OrderDetailDataProcess{
    public boolean saveOrderDetail(Connection connection, OrderDetailDto orderDetailDto) throws SQLException;

    List<OrderDetailDto> getAll(Connection connection, String orderId) throws SQLException;
}
