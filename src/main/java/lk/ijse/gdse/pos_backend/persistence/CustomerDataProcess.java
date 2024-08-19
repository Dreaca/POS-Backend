package lk.ijse.gdse.pos_backend.persistence;

import lk.ijse.gdse.pos_backend.dto.CustomerDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class CustomerDataProcess implements CustomerData {

    static String SAVE_CUSTOMER = "INSERT INTO customer(customerId,customerName,customerAddress,customerPhone) VALUES (?,?,?,?)";
    static String GET_CUSTOMER = "SELECT * FROM student WHERE id = ?";
    static String DELETE_CUSTOMER = "DELETE FROM student WHERE id = ?";
    static String UPDATE_CUSTOMER = "UPDATE student SET name = ?,city = ?,level = ?,email = ? WHERE id = ?";
    @Override
    public CustomerDto getCustomer(String customerId, Connection connection) {
        return null;
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
    public boolean deleteCustomer(String customerId, Connection connection) {
        return false;
    }

    @Override
    public boolean updateCustomer(String customerId, CustomerDto customerDto, Connection connection) {
        return false;
    }
}
