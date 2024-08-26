package lk.ijse.gdse.pos_backend.controller;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse.pos_backend.dto.OrderDetailDto;
import lk.ijse.gdse.pos_backend.persistence.OrderDetailDataProcess;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/orderDetails")
public class OrderDetailController extends HttpServlet {
    Connection connection;

    @Override
    public void init() throws ServletException {
        try{
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/posdb");
            this.connection = pool.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        OrderDetailDataProcess orderDetailDataProcess = new OrderDetailDataProcess();
        String orderId = req.getParameter("orderId");
        try(var writer = resp.getWriter()){
            List<OrderDetailDto> orderDetailDtoList = orderDetailDataProcess.getAll(connection,orderId);
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            Jsonb jsonb = JsonbBuilder.create();
            for(OrderDetailDto orderDetailDto : orderDetailDtoList){
                JsonObject jsonObject = Json.createReader(new StringReader(jsonb.toJson(orderDetailDto))).readObject();
                jsonArrayBuilder.add(jsonObject);
            }
            writer.write(jsonArrayBuilder.build().toString());
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
