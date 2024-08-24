package lk.ijse.gdse.pos_backend.controller;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
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
import org.json.JSONArray;
import org.json.JSONObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/customer",loadOnStartup = 2)
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
        if(!req.getContentType().toLowerCase().startsWith("application/json")||req.getContentType() == null){
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }
        CustomerDataProcess dataProcess = new CustomerDataProcess();
        try(var writer = resp.getWriter()){
            var id = req.getParameter("customerId");
            boolean flag = dataProcess.deleteCustomer(id, connection);
            if(flag){
                writer.write("{\"status\":\"success\"}");
            }
            else{
                writer.write("{\"status\":\"error\"}");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    //TODO : Update Customer

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!req.getContentType().toLowerCase().startsWith("application/json")||req.getContentType() == null){
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }
        CustomerDataProcess dataProcess = new CustomerDataProcess();
        try(var writer = resp.getWriter()){
            String id = req.getParameter("customerId");
            Jsonb customerJson = JsonbBuilder.create();
            var updatedCustomer = customerJson.fromJson(req.getReader(), CustomerDto.class);
            boolean b = dataProcess.updateCustomer(id, updatedCustomer, connection);
            if (b) {
                writer.write("{\"status\":\"success\"}");
            }
            else {
                writer.write("{\"status\":\"error\"}");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //TODO : get customer
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        CustomerDataProcess dataProcess = new CustomerDataProcess();

        if (req.getHeader("Request-Type").equals("table")) {

            try (var writer = resp.getWriter()) {

                List<CustomerDto> customerList = dataProcess.getAllCustomer(connection);

                JsonArrayBuilder jb = Json.createArrayBuilder();
                Jsonb jsonb = JsonbBuilder.create();

                for (CustomerDto customerDto : customerList) {
                    var jasonObject = Json.createReader(new StringReader(jsonb.toJson(customerDto))).readObject();
                    jb.add(jasonObject);
                }
                writer.write(jb.build().toString());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public CustomerController() {
        super();
    }
}
