/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/secondservlet")
public class secondservlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter  out = response.getWriter();
      response.setContentType("text/html");
      
       ServletContext context = getServletContext();
       Object obj = context.getAttribute("accno");
       String value = obj.toString();
       
       
       out.println("<html>");
       out.println("<body bgcolor=pink>"); 
       
       out.println("<center>");
       out.println("<h2>Banco - ERP</h2>"); 
       out.println("<center>");
        
        out.println("<Form method=post action=servlet3>"); 
        out.println("<b> Depósitos Bancarios</b>");
        
        out.println("<table>");
       
        out.println("<tr>");
        out.println("<td>");
        out.println("Número de Cuenta : " + value);
         out.println("</td>");
        out.println("</tr>");
        
         out.println("<tr>");
        out.println("<td>");
        out.println("Monto a depositar : </td>  <td> <input type=text name=amount value=0>");
         out.println("</td>");
        out.println("</tr>");
        out.println("</table>");
        
         out.println("<input type=submit value=Depositar>");
         out.println("</br>");
         out.println("</Form>");
         out.println("</body>");
         out.println("</html>");
    }

}
