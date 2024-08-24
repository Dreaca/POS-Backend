package lk.ijse.gdse.pos_backend.persistence;

import lk.ijse.gdse.pos_backend.dto.CustomerDto;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public sealed interface CustomerData permits CustomerDataProcess {
    CustomerDto getCustomer(String customerId, Connection connection) throws SQLException;

    String saveCustomer(CustomerDto customerDto, Connection connection) throws SQLException;

    boolean deleteCustomer(String customerId, Connection connection) throws SQLException;

    boolean updateCustomer(String customerId, CustomerDto customerDto, Connection connection) throws SQLException;

    List<CustomerDto> getAllCustomer(Connection connection) throws SQLException;
}
