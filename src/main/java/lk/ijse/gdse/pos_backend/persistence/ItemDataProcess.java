package lk.ijse.gdse.pos_backend.persistence;

import lk.ijse.gdse.pos_backend.dto.ItemDto;
import lk.ijse.gdse.pos_backend.dto.OrderDetailDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ItemDataProcess implements ItemData {
    static String SAVE_ITEM = "INSERT INTO item(itemCode,itemName,qto,author,price) VALUES (?,?,?,?,?)";
    static String GET_ITEM = "SELECT * FROM item WHERE itemCode = ?";
    static String DELETE_ITEM = "DELETE FROM item WHERE itemCode = ?";
    static String UPDATE_ITEM = "UPDATE item SET itemName = ?,qto = ?, author = ?, price = ? WHERE itemCode = ?";
    static  String GET_ALL_ITEM = "SELECT * FROM item";
    static String SEARCH_ITEM = "SELECT * FROM item WHERE LOWER(itemCode) = ? OR LOWER(itemName) LIKE ?  OR LOWER(author) = ?";
    static String GET_ITEM_NAME = "SELECT itemName FROM item WHERE LOWER(itemName) LIKE ?";
    static String CHANGE_ITEM_QTY = "UPDATE item SET qto = (qto - ?) WHERE LOWER(itemCode) = ?";
    @Override
    public ItemDto getItem(String itemCode, Connection connection) throws SQLException {
        ItemDto itemDto = new ItemDto();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ITEM);
            preparedStatement.setString(1, itemCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                itemDto.setItemCode(resultSet.getString("itemCode"));
                itemDto.setItemName(resultSet.getString("itemName"));
                itemDto.setQto(resultSet.getInt("qto"));
                itemDto.setAuthor(resultSet.getString("author"));
                itemDto.setPrice(resultSet.getInt("price"));
            }
            resultSet.close();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return itemDto;    }

    @Override
    public String saveItem(ItemDto ItemDto, Connection connection) throws SQLException {
        String message = "";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_ITEM);
            preparedStatement.setString(1,ItemDto.getItemCode());
            preparedStatement.setString(2,ItemDto.getItemName());
            preparedStatement.setInt(3,ItemDto.getQto());
            preparedStatement.setString(4,ItemDto.getAuthor());
            preparedStatement.setInt(5,ItemDto.getPrice());
            if (preparedStatement.executeUpdate() != 0){
                message = "Customer with id " + ItemDto.getItemCode() + " has been saved";
            }
            else {
                message = "Customer with id " + ItemDto.getItemCode() + " has not been saved";
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public boolean deleteItem(String itemCode, Connection connection) throws SQLException {
        try {
            PreparedStatement pstm = connection.prepareStatement(DELETE_ITEM);
            pstm.setString(1, itemCode);
            return pstm.executeUpdate() != 0;
        }
        catch (SQLException e){
            throw e;
        }
    }

    @Override
    public boolean updateItem(String itemCode, ItemDto itemDto, Connection connection) throws SQLException {
        try(var pstm = connection.prepareStatement(UPDATE_ITEM)){
            pstm.setString(1,itemDto.getItemName());
            pstm.setInt(2,itemDto.getQto());
            pstm.setString(3,itemDto.getAuthor());
            pstm.setInt(4,itemDto.getPrice());
            pstm.setString(5,itemCode);

            return pstm.executeUpdate() != 0;
        }
        catch (SQLException e){
            throw e;
        }
    }

    @Override
    public List<ItemDto> getAllItem(Connection connection) throws SQLException {
        List<ItemDto> itemDtoList = new ArrayList<>();
        try(var pstm = connection.prepareStatement(GET_ALL_ITEM)){
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()){
                ItemDto itemDto = new ItemDto(
                        resultSet.getString("itemCode"),
                        resultSet.getString("itemName"),
                        resultSet.getInt("qto"),
                        resultSet.getString("author"),
                        resultSet.getInt("price")
                );
                itemDtoList.add(itemDto);
            }
            resultSet.close();
            return itemDtoList;
        }
        catch (SQLException e){
            throw e;
        }
    }

    @Override
    public List<ItemDto> searchItem(String query, Connection connection) throws SQLException {
        List<ItemDto> itemDtoList = new ArrayList<>();
        try(var pstm = connection.prepareStatement(SEARCH_ITEM)){
            pstm.setString(1,query);
            pstm.setString(2,"%"+query+"%");
            pstm.setString(3,"%"+query+"%");
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()){
                ItemDto itemDto = new ItemDto();
                itemDto.setItemCode(resultSet.getString("itemCode"));
                itemDto.setItemName(resultSet.getString("itemName"));
                itemDto.setQto(resultSet.getInt("qto"));
                itemDto.setAuthor(resultSet.getString("author"));
                itemDto.setPrice(resultSet.getInt("price"));
                itemDtoList.add(itemDto);
            }
            resultSet.close();
            return itemDtoList;
        }
        catch (SQLException e){
            throw e;
        }
    }

    @Override
    public List<String> getNameSuggestions(String query, Connection connection) throws SQLException {
        List<String> suggestions = new ArrayList<>();
        try(var pstm = connection.prepareStatement(GET_ITEM_NAME)){
            pstm.setString(1,query+"%");

            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()){
                suggestions.add(resultSet.getString("itemName"));
            }
            resultSet.close();
        }
        catch (SQLException e){
            throw e;
        }
        return suggestions;
    }

    @Override
    public boolean updateItemQty(Connection connection, OrderDetailDto orderDetailDto) {
        try(var pstm = connection.prepareStatement(CHANGE_ITEM_QTY)){
            pstm.setInt(1,orderDetailDto.getQty());
            pstm.setString(2,orderDetailDto.getItemId());
            return pstm.executeUpdate() !=0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
