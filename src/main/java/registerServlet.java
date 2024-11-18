/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

 import java.io.IOException;
 import java.io.PrintWriter;
 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.PreparedStatement;
 import java.sql.SQLException;
 import jakarta.servlet.ServletContext;
 import jakarta.servlet.ServletException;
 import jakarta.servlet.annotation.WebServlet;
 import jakarta.servlet.http.HttpServlet;
 import jakarta.servlet.http.HttpServletRequest;
 import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/registerServlet")
public class registerServlet extends HttpServlet {

    PrintWriter out = null;
    Connection con;
    PreparedStatement pst;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

                try {
                    // Obtener los parámetros necesarios para la inserción
                    String accno = request.getParameter("accno");
                    String pinno = request.getParameter("pinno");
            
                    // Establecer la conexión con la base de datos
                    con = DriverManager.getConnection("jdbc:mysql://localhost/bar", "root", "");
            
                    // Preparar la consulta SQL para insertar un nuevo registro en la tabla login
                    pst = con.prepareStatement("INSERT INTO login (accno, pinno) VALUES (?, ?)");
                    pst.setString(1, accno);
                    pst.setString(2, pinno);
            
                    // Ejecutar la inserción
                    int rowsAffected = pst.executeUpdate();
                    if (rowsAffected > 0) {
                        // Si la inserción fue exitosa, insertar un saldo inicial de 0 en account_balance
                        pst = con.prepareStatement("INSERT INTO account_balance (accnum, balance) VALUES (?, ?)");
                        pst.setString(1, accno);
                        pst.setDouble(2, 0.00); // Saldo inicial de 0
                        pst.executeUpdate();
            
                        // Mensaje de éxito
                        out = response.getWriter();
                        response.setContentType("text/html");
                        out.println("<html>");
                        out.println("<body bgcolor='#ECF0F1'>");
                        out.println("Registro realizado exitosamente.");
                        out.println("</body>");
                        out.println("</html>");
                    } else {
                        // Si no se afectaron filas
                        out = response.getWriter();
                        response.setContentType("text/html");
                        out.println("<html>");
                        out.println("<body bgcolor='#ECF0F1'>");
                        out.println("Error al registrarse.");
                        out.println("</body>");
                        out.println("</html>");
                    }
            
                    // Cerrar la conexión
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
    }
}