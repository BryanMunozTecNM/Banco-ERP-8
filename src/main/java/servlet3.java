/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/servlet3")
public class servlet3 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
                try {
                    Connection con;
                    PreparedStatement pst;
            
                    PrintWriter out = response.getWriter();
                    response.setContentType("text/html");
            
                    Class.forName("com.mysql.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost/bar", "root", "");
                    ServletContext context = getServletContext();
                    Object obj = context.getAttribute("accid");
                    String accid = obj.toString();
            
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String date = dateFormatter.format(now);
                    String hour = timeFormatter.format(now); // Cambiamos el nombre a 'hour'
                    String amountStr = request.getParameter("amount");
                    double amount = Double.parseDouble(request.getParameter("amount"));
                    String transactionType = request.getParameter("transactionType"); //deposit or charge
            
            
                    // Manejar depósito
                    if (transactionType.equals("deposit")) {
                        // Verificar si ya existe un registro para el accid en account_balance
                        pst = con.prepareStatement("SELECT balance FROM account_balance WHERE accid = ?");
                        pst.setString(1, accid);
                        ResultSet rs = pst.executeQuery();
            
                        if (rs.next()) {
                            // Si existe, actualizar el saldo
                            double existingBalance = rs.getDouble("balance");
                            double newBalance = existingBalance + amount;
            
                            pst = con.prepareStatement("UPDATE account_balance SET balance = ? WHERE accid = ?");
                            pst.setDouble(1, newBalance);
                            pst.setString(2, accid);
                            pst.executeUpdate();
                        } else {
                            // Si no existe, insertar un nuevo registro con el saldo inicial
                            pst = con.prepareStatement("INSERT INTO account_balance (accid, balance) VALUES (?, ?)");
                            pst.setString(1, accid);
                            pst.setDouble(2, amount);
                            pst.executeUpdate();
                        }
                    } 
                    // Manejar cargo
                    else if (transactionType.equals("charge")) {
                        // Verificar si ya existe un registro para el accid en account_balance
                        pst = con.prepareStatement("SELECT balance FROM account_balance WHERE accid = ?");
                        pst.setString(1, accid);
                        ResultSet rs = pst.executeQuery();
            
                        if (rs.next()) {
                            // Si existe, actualizar el saldo
                            double existingBalance = rs.getDouble("balance");
                            double newBalance = existingBalance - amount;
            
                            pst = con.prepareStatement("UPDATE account_balance SET balance = ? WHERE accid = ?");
                            pst.setDouble(1, newBalance);
                            pst.setString(2, accid);
                            pst.executeUpdate();
                        } else {
                            // Si no existe, no se puede hacer un cargo
                            out.println("No se puede realizar el cargo, la cuenta no existe.");
                            return;
                        }
                    }
            
                    // Registrar la transacción en la tabla de historial
                    pst = con.prepareStatement("INSERT INTO transactions (accid, date, hour, amount, type) VALUES (?, ?, ?, ?, ?)");
                    pst.setString(1, accid);
                    pst.setString(2, date);
                    pst.setString(3, hour); // Insertar la hora en la columna 'hour'
                    pst.setDouble(4, amount);
                    pst.setString(5, transactionType);
                    pst.executeUpdate();
            
        String transactionMessage = "La transaccion fue realizada con exito";
        response.sendRedirect("secondservlet?transactionMessage=" + URLEncoder.encode(transactionMessage, "UTF-8"));
    } catch (Exception ex) {
        ex.printStackTrace();
    }
       
    }
}