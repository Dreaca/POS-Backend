package lk.ijse.gdse.pos_backend.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse.pos_backend.dto.CustomerDto;
import lk.ijse.gdse.pos_backend.util.UtilProcess;

import java.io.IOException;
@WebServlet(urlPatterns = "/customer")
public class CustomerController extends HttpServlet {
    //TODO : Create save customer
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!req.getContentType().toLowerCase().startsWith("application/json")||req.getContentType()== null){
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }
        CustomerDto customerDto = new CustomerDto();
        try(var writer = resp.getWriter()){
            customerDto.setCustomerId(UtilProcess.generateId());

        };

    }
    //TODO : delete customer

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
    //TODO : Update Customer

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
    //TODO : get customer
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
