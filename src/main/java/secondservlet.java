/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.ResultSet;

@WebServlet("/secondservlet")
public class secondservlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Llama a doPost para manejar la lógica de mostrar la pantalla de transacciones
        doPost(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
    
        ServletContext context = getServletContext();
        Object obj = context.getAttribute("accid");
        String value = obj.toString();
        
        double totalBalance = 0.0;
        String transactionMessage = ""; // Mensaje de transacción
    
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/bar", "root", "");
            PreparedStatement pst = con.prepareStatement("SELECT balance FROM account_balance WHERE accid = ?");
            pst.setString(1, value);
            ResultSet rs = pst.executeQuery();
    
            if (rs.next()) {
                totalBalance = rs.getDouble("balance");
            } else {
                out.println("Error: No se encontró la cuenta para el balance");
                return;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    
        // Mostrar el mensaje de transacción si existe
        if (request.getParameter("transactionMessage") != null) {
            transactionMessage = request.getParameter("transactionMessage");
        }
    
        out.println("<html>");
        out.println("<body bgcolor='#ECF0F1'>");
        out.println("<center>");
        out.println("<h2>Banco - ERP</h2>");
        out.println("<form method='post' action='servlet3'>");
        out.println("<b>Transacciones Bancarias</b>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<td>Número de Cuenta: " + value + "</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td>Saldo Total: " + totalBalance + "</td>");
        out.println("</tr>");
        out.println("</table>");
    
        // Mostrar el mensaje de transacción
        if (!transactionMessage.isEmpty()) {
            out.println("<p style='color: green;'>" + transactionMessage + "</p>");
        }
    
        // Agregar opción para realizar un depósito o un cargo
        out.println("<input type='text' name='amount' placeholder='Monto' required>");
        out.println("<select name='transactionType'>");
        out.println("<option value='deposit'>Depósito</option>");
        out.println("<option value='charge'>Cargo</option>");
        out.println("</select>");
        out.println("<input type='submit' value='Realizar Transacción'>");
        out.println("</form>");
        out.println("</center>");
        out.println("</body>");
        out.println("</html>");
    }
}
