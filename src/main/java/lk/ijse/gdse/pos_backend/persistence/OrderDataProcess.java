package lk.ijse.gdse.pos_backend.persistence;

import lk.ijse.gdse.pos_backend.dto.ItemDto;
import lk.ijse.gdse.pos_backend.dto.OrderDetailDto;
import lk.ijse.gdse.pos_backend.dto.OrderDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public final class OrderDataProcess implements OrderData {
    static String SAVE_ORDER = "INSERT INTO orders(orderId,customerId,date,total,discount,subTotal) VALUES (?,?,?,?,?,?)";

    //TODO: create generate order id send to frontend

    @Override
    public ItemDto getOrder(String orderId, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public boolean saveOrder(OrderDto orderDto, Connection connection) throws SQLException {

        try(var pstm = connection.prepareStatement(SAVE_ORDER)) {
            pstm.setString(1, orderDto.getOrderId());
            pstm.setString(2, orderDto.getCustomerId());
            pstm.setString(3, orderDto.getDate());
            pstm.setDouble(4,orderDto.getTotal());
            pstm.setString(5,orderDto.getDiscount());
            pstm.setDouble(6,orderDto.getSubtotal());
           return pstm.executeUpdate()!= 0;
        }catch (SQLException e){
            throw e;
        }
    }

    @Override
    public boolean deleteOrder(String orderId, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public boolean updateOrder(String orderId, OrderDto orderDto, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public List<OrderDto> getAllItem(Connection connection) throws SQLException {
        return List.of();
    }

    @Override
    public List<OrderDto> searchItem(String query, Connection connection) throws SQLException {
        return List.of();
    }

    @Override
    public List<String> getNameSuggestions(String query, Connection connection) throws SQLException {
        return List.of();
    }

}
