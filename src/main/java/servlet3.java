/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
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
        
        try 
        {
            Connection con;
            PreparedStatement pst;
            ResultSet rs;
            
            PrintWriter  out = response.getWriter();
            response.setContentType("text/html");
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/bar", "root", "");
            ServletContext context = getServletContext();
            Object obj = context.getAttribute("accno");
            String accno = obj.toString();
            
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyy/MM/dd");
            LocalDateTime now = LocalDateTime.now();
            String date = df.format(now);
            String amount = request.getParameter("amount");

            
            pst = con.prepareStatement("insert into account_holder(accnum,date,mdeposit)values(?,?,?)");
            pst.setString(1, accno);
            pst.setString(2, date);
            
            pst.setString(3, amount);
            int rows = pst.executeUpdate();
            
            if(rows==1)
            {
                out.println("La transacción fue realizada con éxito");
            }
            else
            {
                 out.println("La transacción fallo");
            }

        } 
        catch (ClassNotFoundException ex)
        {
           ex.printStackTrace();
        } 
        catch (SQLException ex) {
            ex.printStackTrace();
        }
       
    }

}