package lk.ijse.gdse.pos_backend.persistence;

import lk.ijse.gdse.pos_backend.dto.ItemDto;
import lk.ijse.gdse.pos_backend.dto.OrderDetailDto;
import lk.ijse.gdse.pos_backend.dto.OrderDto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class OrderDataProcess implements OrderData {
    static String SAVE_ORDER = "INSERT INTO orders(orderId,customerId,customerName,date,total,discount,subTotal) VALUES (?,?,?,?,?,?,?)";
    static String DELETE_ORDER = "DELETE FROM orders WHERE orderId=?";
    static String GET_ORDER = "SELECT * FROM orders WHERE orderId=?";
    static String GET_ALL_ORDERS = "SELECT * FROM orders";

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
            pstm.setString(3, orderDto.getCustomerName());
            pstm.setString(4, orderDto.getDate());
            pstm.setDouble(5,orderDto.getTotal());
            pstm.setString(6,orderDto.getDiscount());
            pstm.setDouble(7,orderDto.getSubtotal());
           return pstm.executeUpdate()!= 0;
        }catch (SQLException e){
            throw e;
        }
    }

    @Override
    public boolean deleteOrder(String orderId, Connection connection) throws SQLException {
        try(var pstm = connection.prepareStatement(DELETE_ORDER)){
            pstm.setString(1, orderId);
            return pstm.executeUpdate()!= 0;
        }
    }

    @Override
    public boolean updateOrder(String orderId, OrderDto orderDto, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public List<OrderDto> getAllItem(Connection connection) throws SQLException {
       try(var ptsm = connection.prepareStatement(GET_ALL_ORDERS)) {
           ResultSet rs = ptsm.executeQuery();
           List<OrderDto> orders = new ArrayList<>();
           while (rs.next()) {
               OrderDto orderDto = new OrderDto();
               orderDto.setOrderId(rs.getString(1));
               orderDto.setCustomerId(rs.getString(2));
               orderDto.setCustomerName(rs.getString(3));
               orderDto.setDate(rs.getString(4));
               orderDto.setTotal(rs.getDouble(5));
               orderDto.setDiscount(rs.getString(6));
               orderDto.setSubtotal(rs.getDouble(7));
               orders.add(orderDto);
           }
           return orders;
       }catch (SQLException e){
           throw e;
       }
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
