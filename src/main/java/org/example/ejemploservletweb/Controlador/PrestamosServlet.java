package org.example.ejemploservletweb.Controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ejemploservletweb.Modelo.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@WebServlet(name = "prestamosServlet", value="/prestamos-servlet")
public class PrestamosServlet extends HttpServlet {
    DAOGenerico<Prestamo, String> daoprestamo;
    DAOGenerico<Ejemplar, String> daoejemplar;
    DAOGenerico<Usuario, String> daousuario;
    DAOGenerico<Libro, String> daolibro;

    public void init(){
        daoprestamo = new DAOGenerico<>(Prestamo.class, String.class);
        daoejemplar = new DAOGenerico<>(Ejemplar.class, String.class);
        daousuario = new DAOGenerico<>(Usuario.class, String.class);
        daolibro = new DAOGenerico<>(Libro.class, String.class);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String opcion = request.getParameter("opcion");

        if(opcion.equals("get")){
            String id = request.getParameter("id");
            PrintWriter impresora = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            Prestamo prestamo = daoprestamo.getById(id);
            if(prestamo == null){
                System.out.println("Prestamo no encontrado");
                impresora.println(mapper.writeValueAsString("Prestamo no encontrado"));
            }else{
                System.out.println(prestamo);
                impresora.println(mapper.writeValueAsString(prestamo));
            }
        }
        else if(opcion.equals("add")){
            String usuario_id = request.getParameter("usuario_id");
            String ejemplar_id = request.getParameter("ejemplar_id");
            PrintWriter impresora = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            Prestamo prestamo = new Prestamo();
            Usuario usuario = daousuario.getById(usuario_id);
            Ejemplar ejemplar = daoejemplar.getById(ejemplar_id);
            if(usuario == null && ejemplar == null){
                System.out.println("Usuario no encontrado o ejemplar no encontrado");
                impresora.println(mapper.writeValueAsString("Usuario no encontrado o ejemplar no encontrado"));
            }else {
                if(usuario.getPenalizacionHasta()==null || LocalDate.now().isAfter(usuario.getPenalizacionHasta())){
                    prestamo.setUsuario(usuario);
                    prestamo.setEjemplar(ejemplar);
                    prestamo.setFechaInicio(LocalDate.now());
                    prestamo.setFechaDevolucion(LocalDate.now().plusDays(15));
                    ejemplar.setEstado("Prestado");
                    daoprestamo.add(prestamo);
                    daoejemplar.update(ejemplar);
                    System.out.println(prestamo);
                    impresora.println(mapper.writeValueAsString(prestamo));
                }else {
                    System.out.println("No posible prestamo por penalizacion activa");
                    impresora.println(mapper.writeValueAsString("No posible prestamo por penalizacion activa"));
                }
            }
        } else if (opcion.equals("update")) {
            String id = request.getParameter("id");
            String ejemplar_id = request.getParameter("ejemplar_id");
            String usuario_id = request.getParameter("usuario_id");
            PrintWriter impresora = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            Usuario usuario = daousuario.getById(usuario_id);
            Ejemplar ejemplar = daoejemplar.getById(ejemplar_id);
            Prestamo prestamo = daoprestamo.getById(id);
            if(prestamo == null){
                System.out.println("Prestamo no encontrado");
                impresora.println(mapper.writeValueAsString("Prestamo no encontrado"));
            }else{
                prestamo.setUsuario(usuario);
                prestamo.setEjemplar(ejemplar);
                ejemplar.setEstado("Prestado");
                daoejemplar.update(ejemplar);
                daoprestamo.update(prestamo);
                impresora.println(mapper.writeValueAsString(prestamo));
                System.out.println("Prestamo actualizado");
            }

        }else if(opcion.equals("delete")){
            String id = request.getParameter("id");
            PrintWriter impresora = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            Prestamo prestamo = daoprestamo.getById(id);
            Ejemplar ejemplar = daoejemplar.getById(prestamo.getEjemplar().getId().toString());
            if(prestamo == null){
                System.out.println("Prestamo no encontrado");
                impresora.println(mapper.writeValueAsString(prestamo));
            }else{
                impresora.println(mapper.writeValueAsString(prestamo));
                System.out.println("Prestamo eliminado");
                ejemplar.setEstado("Disponible");
                daoejemplar.update(ejemplar);
                daoprestamo.deleteUsuario(prestamo);
            }
        }
    }
}
