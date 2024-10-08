package lk.ijse.gdse.pos_backend.persistence;

import lk.ijse.gdse.pos_backend.dto.CustomerDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class CustomerDataProcess implements CustomerData {

    static String SAVE_CUSTOMER = "INSERT INTO customer(customerId,customerName,customerAddress,customerPhone) VALUES (?,?,?,?)";
    static String GET_CUSTOMER = "SELECT * FROM customer WHERE customerId = ?";
    static String DELETE_CUSTOMER = "DELETE FROM customer WHERE customerId = ?";
    static String UPDATE_CUSTOMER = "UPDATE customer SET customerName = ?,customerAddress = ?,customerPhone = ? WHERE customerId = ?";
    static  String GET_ALL_CUSTOMER = "SELECT * FROM customer";
    static String SEARCH_CUSTOMER = "SELECT * FROM customer WHERE LOWER(customerId) = ? OR LOWER(customerName) LIKE ? OR LOWER(customerAddress) LIKE ? OR LOWER(customerPhone) = ?";
    static String GET_CUSTOMER_NAME = "SELECT customerName FROM customer WHERE LOWER(customerName) LIKE ?";
    @Override
    public CustomerDto getCustomer(String customerId, Connection connection) {
        CustomerDto customerDto = new CustomerDto();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CUSTOMER);
            preparedStatement.setString(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                customerDto.setCustomerId(resultSet.getString("customerId"));
                customerDto.setCustomerName(resultSet.getString("customerName"));
                customerDto.setCustomerAddress(resultSet.getString("customerAddress"));
                customerDto.setCustomerPhone(resultSet.getString("customerPhone"));
            }
            resultSet.close();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return customerDto;
    }

    @Override
    public String saveCustomer(CustomerDto customerDto, Connection connection) {
        String message = "";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_CUSTOMER);
            preparedStatement.setString(1,customerDto.getCustomerId());
            preparedStatement.setString(2,customerDto.getCustomerName());
            preparedStatement.setString(3, customerDto.getCustomerAddress());
            preparedStatement.setString(4,customerDto.getCustomerPhone());
            if (preparedStatement.executeUpdate() != 0){
                message = "Customer with id " + customerDto.getCustomerId() + " has been saved";
            }
            else {
                message = "Customer with id " + customerDto.getCustomerId() + " has not been saved";
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public boolean deleteCustomer(String customerId, Connection connection) throws SQLException {
        try {
            PreparedStatement pstm = connection.prepareStatement(DELETE_CUSTOMER);
            pstm.setString(1, customerId);
            return pstm.executeUpdate() != 0;
        }
        catch (SQLException e){
            throw e;
        }
    }

    @Override
    public boolean updateCustomer(String customerId, CustomerDto customerDto, Connection connection) throws SQLException {
        try (var pstm = connection.prepareStatement(UPDATE_CUSTOMER)){

            pstm.setString(4,customerDto.getCustomerId());
            pstm.setString(1,customerDto.getCustomerName());
            pstm.setString(2,customerDto.getCustomerAddress());
            pstm.setString(3,customerDto.getCustomerPhone());

            return pstm.executeUpdate() != 0;
        }
        catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<CustomerDto> getAllCustomer(Connection connection) throws SQLException {
         List<CustomerDto> customerDtoList = new ArrayList<>();
         try(var pstm = connection.prepareStatement(GET_ALL_CUSTOMER) ){
             ResultSet resultSet = pstm.executeQuery();
             while (resultSet.next()){
                 CustomerDto customerDto = new CustomerDto(
                         resultSet.getString("customerId"),
                         resultSet.getString("customerName"),
                         resultSet.getString("customerAddress"),
                         resultSet.getString("customerPhone")
                 );
                 customerDtoList.add(customerDto);
                 }
             resultSet.close();
             return customerDtoList;

         }catch (SQLException e){
             throw e;
         }

    }
    @Override
    public List<CustomerDto> searchCustomers(String query, Connection connection) throws SQLException {

        List<CustomerDto> customerList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(SEARCH_CUSTOMER)) {
            stmt.setString(1, query);
            stmt.setString(2, "%" + query + "%");
            stmt.setString(3, "%" + query + "%");
            stmt.setString(4, query);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CustomerDto customer = new CustomerDto();
                customer.setCustomerId(rs.getString("customerId"));
                customer.setCustomerName(rs.getString("customerName"));
                customer.setCustomerAddress(rs.getString("customerAddress"));
                customer.setCustomerPhone(rs.getString("customerPhone"));
                customerList.add(customer);
            }
        }
        return customerList;
    }

    @Override
    public List<String> getNameSuggestions(String query, Connection connection) throws SQLException {
        List<String> nameList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(GET_CUSTOMER_NAME)) {
            stmt.setString(1, query + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                nameList.add(rs.getString("customerName"));
            }
        } catch (SQLException e) {
            throw(e);
        }
        return nameList;
    }

}
