package lk.ijse.gdse.pos_backend.persistence;

import lk.ijse.gdse.pos_backend.dto.ItemDto;
import lk.ijse.gdse.pos_backend.dto.OrderDetailDto;
import lk.ijse.gdse.pos_backend.dto.OrderDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public sealed interface OrderData permits OrderDataProcess {
    ItemDto getOrder(String orderId, Connection connection) throws SQLException;

    boolean saveOrder(OrderDto orderDto, Connection connection) throws SQLException;

    boolean deleteOrder(String orderId, Connection connection) throws SQLException;

    boolean updateOrder(String orderId, OrderDto orderDto, Connection connection) throws SQLException;

    List<OrderDto> getAllItem(Connection connection) throws SQLException;
    public List<OrderDto> searchItem(String query, Connection connection) throws SQLException;

    List<String> getNameSuggestions(String query, Connection connection) throws SQLException;

}
