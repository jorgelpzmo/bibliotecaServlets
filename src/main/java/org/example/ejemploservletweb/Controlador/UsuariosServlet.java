package org.example.ejemploservletweb.Controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.ejemploservletweb.Modelo.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "usuariosServlet", value="/usuarios-servlet")
public class UsuariosServlet extends HttpServlet {
    DAOUsuario daousuario;
    DAOPrestamo daoprestamo1;
    DAOGenerico<Prestamo, String> daoprestamo;

    public void init(){
        daousuario = new DAOUsuario();
        daoprestamo1 = new DAOPrestamo();
        daoprestamo = new DAOGenerico<>(Prestamo.class, String.class);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String opcion = request.getParameter("opcion");

        if(opcion.equals("login")){
            String dni = request.getParameter("dni");
            String password = request.getParameter("password");
            PrintWriter impresora = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            Usuario usuario = daousuario.getUsuarioByDni(dni);
            if(usuario == null){
                System.out.println("Usuario no encontrado");
                impresora.println(mapper.writeValueAsString("Usuario no encontrado"));
            }else if(usuario.getPassword().equals(password)){
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                System.out.println(usuario);
                if(usuario.getTipo().equals("normal")) {
                    List<Prestamo> prestamos = daoprestamo1.getPrestamoByUsuario(usuario);
                    System.out.println(usuario);
                    System.out.println(prestamos);
                    impresora.println(mapper.writeValueAsString(usuario));
                    impresora.println(mapper.writeValueAsString(prestamos));
                }else{
                    response.sendRedirect("prestamos-servlet");
                }
            }
        } else if (opcion.equals("registro")) {
            String dni = request.getParameter("dni");
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String tipo = request.getParameter("tipo");
            PrintWriter impresora = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            Usuario usuario = new Usuario();
            if(tipo.equals("normal")){
                usuario.setTipo("normal");
                usuario.setDni(dni);
                usuario.setNombre(nombre);
                usuario.setEmail(email);
                usuario.setPassword(password);
                usuario.setPenalizacionHasta(null);
                daousuario.addUsuario(usuario);
                impresora.println(mapper.writeValueAsString(usuario));
                System.out.println(usuario);
            } else if (tipo.equals("administrador")) {
                usuario.setTipo("administrador");
                usuario.setDni(dni);
                usuario.setNombre(nombre);
                usuario.setEmail(email);
                usuario.setPassword(password);
                usuario.setPenalizacionHasta(null);
                daousuario.addUsuario(usuario);
                impresora.println(mapper.writeValueAsString(usuario));
                System.out.println(usuario);
            }else{
                impresora.println(mapper.writeValueAsString("Elija tipo de usuario"));
                System.out.println("Elija tipo de usuario");
            }

        }
    }



}
