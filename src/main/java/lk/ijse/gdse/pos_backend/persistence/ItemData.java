package lk.ijse.gdse.pos_backend.persistence;

import lk.ijse.gdse.pos_backend.dto.ItemDto;
import lk.ijse.gdse.pos_backend.dto.OrderDetailDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public sealed interface ItemData permits ItemDataProcess {
    ItemDto getItem(String itemCode, Connection connection) throws SQLException;

    String saveItem(ItemDto ItemDto, Connection connection) throws SQLException;

    boolean deleteItem(String itemCode, Connection connection) throws SQLException;

    boolean updateItem(String itemCode, ItemDto itemDto, Connection connection) throws SQLException;

    List<ItemDto> getAllItem(Connection connection) throws SQLException;
    public List<ItemDto> searchItem(String query, Connection connection) throws SQLException;

    List<String> getNameSuggestions(String query, Connection connection) throws SQLException;

    boolean updateItemQty(Connection connection, OrderDetailDto orderDetailDto);
}
