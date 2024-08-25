package lk.ijse.gdse.pos_backend.controller;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse.pos_backend.dto.*;
import lk.ijse.gdse.pos_backend.persistence.ItemDataProcess;
import lk.ijse.gdse.pos_backend.persistence.OrderDataProcess;
import lk.ijse.gdse.pos_backend.persistence.OrderDetailDataProcess;
import lk.ijse.gdse.pos_backend.util.UtilProcess;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/order",loadOnStartup = 2)
public class OrderController extends HttpServlet {
    Connection connection;
    @Override
    public void init(){
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
        OrderDataProcess orderDataProcess = new OrderDataProcess();

        if (req.getHeader("Request-Type").equals("getOrderId")){
            try(var writer = resp.getWriter()){

                writer.write(UtilProcess.generateId());
                resp.setStatus(HttpServletResponse.SC_OK);

            } catch (Exception e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if (req.getHeader("Request-Type").equals("table")) {

            try(var writer = resp.getWriter()){
                List<OrderDto> orderList = orderDataProcess.getAllItem(connection);
                JsonArrayBuilder jb = Json.createArrayBuilder();
                Jsonb jsonb = JsonbBuilder.create();


                for (OrderDto orderDto : orderList){
                    var jObject = Json.createReader(new StringReader(jsonb.toJson(orderDto))).readObject();
                    jb.add(jObject);
                }
                writer.write(jb.build().toString());
                resp.setStatus(HttpServletResponse.SC_OK);

            } catch (SQLException e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }/* else if (req.getHeader("Request-Type").equals("suggest")) {
            String qur = req.getParameter("query").toLowerCase();
            try(var writer = resp.getWriter()){
                List<String> suggestions = itemDataProcess.getNameSuggestions(qur,connection);
                JsonArrayBuilder jb = Json.createArrayBuilder();

                for (String suggestion : suggestions){
                    jb.add(suggestion);
                }
                JsonArray array = jb.build();
                writer.write(array.toString());
            } catch (SQLException e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }*/
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null) {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }

        resp.setContentType("application/json");

        OrderDataProcess dp = new OrderDataProcess();
        OrderDetailDataProcess odp = new OrderDetailDataProcess();
        ItemDataProcess itemDataProcess = new ItemDataProcess();

        try (var writer = resp.getWriter()) {
            connection.setAutoCommit(false);

            Jsonb jsonb = JsonbBuilder.create();

            OrderRequestDto requestDto = jsonb.fromJson(req.getReader(), OrderRequestDto.class);

            OrderDto orderDto = new OrderDto(
                    requestDto.getOrderId(),
                    requestDto.getCustomerId(),
                    requestDto.getCustomerName(),
                    requestDto.getDate(),
                    requestDto.getTotal(),
                    requestDto.getDiscount(),
                    requestDto.getSubTotal()
            );
            System.out.println(requestDto);
            // Save order
            if (dp.saveOrder(orderDto, connection)) {
                boolean allOperationsSuccessful = true;
                    List<CartItemDto> cartItemDtos = requestDto.getCartItems();
                for (CartItemDto cartItemDto : cartItemDtos ) {
                    OrderDetailDto orderDetailDto = new OrderDetailDto(
                            orderDto.getOrderId(),
                            orderDto.getCustomerId(),
                            cartItemDto.getItemCode(),
                            cartItemDto.getQty(),
                            cartItemDto.getTotalPrice()
                    );

                    // Save order detail
                    if (odp.saveOrderDetail(connection, orderDetailDto)) {
                        // Update item quantity
                        if (!itemDataProcess.updateItemQty(connection, orderDetailDto)) {
                            allOperationsSuccessful = false;
                            break;
                        }
                    } else {
                        allOperationsSuccessful = false;
                        break;
                    }
                }

                if (allOperationsSuccessful) {
                    connection.commit();  // Commit transaction
                    writer.write("{\"message\":\"Order Saved\"}");
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    connection.rollback();  // Rollback transaction
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                connection.rollback();  // Rollback transaction
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
