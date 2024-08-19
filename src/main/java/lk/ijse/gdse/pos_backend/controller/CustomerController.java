package lk.ijse.gdse.pos_backend.controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse.pos_backend.dto.CustomerDto;
import lk.ijse.gdse.pos_backend.persistence.CustomerDataProcess;
import lk.ijse.gdse.pos_backend.util.UtilProcess;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/customer")
public class CustomerController extends HttpServlet {
    Connection connection;

    @Override
    public void init() {
        try {
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/posdb");
            this.connection = pool.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO : Create save customer
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(!req.getContentType().toLowerCase().startsWith("application/json")||req.getContentType()== null){
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }
        try(var writer = resp.getWriter()){

            Jsonb customerJson = JsonbBuilder.create();
            CustomerDto customerDto = customerJson.fromJson(req.getReader(), CustomerDto.class);
            CustomerDataProcess dataProcess = new CustomerDataProcess();
            customerDto.setCustomerId(UtilProcess.generateId());


            writer.write(dataProcess.saveCustomer(customerDto,connection));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        }
        catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

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
