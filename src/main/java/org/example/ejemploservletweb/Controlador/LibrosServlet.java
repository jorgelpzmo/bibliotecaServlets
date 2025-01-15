package org.example.ejemploservletweb.Controlador;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ejemploservletweb.Modelo.DAOGenerico;
import org.example.ejemploservletweb.Modelo.Libro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "librosServlet", value = "/libros-servlet")
public class LibrosServlet extends HttpServlet {

        DAOGenerico<Libro, String> daolibro;

    public void init(){
            daolibro = new DAOGenerico<>(Libro.class,String.class);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String opcion = request.getParameter("opcion");

        if(opcion.equals("get")) {

            String isbn = request.getParameter("isbn");

            PrintWriter impresora = response.getWriter();
            ObjectMapper conversorJson = new ObjectMapper();
            conversorJson.registerModule(new JavaTimeModule());
            Libro libro = daolibro.getById(isbn);
            if(libro == null) {
                System.out.println("No existe el libro con ese isbn");
                impresora.println(conversorJson.writeValueAsString("No existe el libro con ese isbn"));
            }
            else {
                System.out.println("en java" + libro);

                String json_response = conversorJson.writeValueAsString(libro);
                System.out.println("En java json" + json_response);
                impresora.println(json_response);
            }
        } else if (opcion.equals("add")) {
            String isbn = request.getParameter("isbn");
            String titulo = request.getParameter("titulo");
            String autor = request.getParameter("autor");

            PrintWriter impresora = response.getWriter();
            ObjectMapper conversorJson = new ObjectMapper();
            conversorJson.registerModule(new JavaTimeModule());
            Libro libro = new Libro();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            if(daolibro.getById(isbn) != null) {
                impresora.println(conversorJson.writeValueAsString("El libro ya existe"));
                System.out.println("El libro ya existe");
            }
            else {
                daolibro.add(libro);
                System.out.println("en java" + libro);

                String json_response = conversorJson.writeValueAsString(libro);
                System.out.println("En java json" + json_response);
                impresora.println(json_response);
            }

        } else if (opcion.equals("update")) {
            String isbn = request.getParameter("isbn");
            String titulo = request.getParameter("titulo");
            String autor = request.getParameter("autor");
            PrintWriter impresora = response.getWriter();
            ObjectMapper conversorJson = new ObjectMapper();
            conversorJson.registerModule(new JavaTimeModule());
            Libro libro = daolibro.getById(isbn);
            if(daolibro.getById(isbn) == null) {
                impresora.println(conversorJson.writeValueAsString("el libro no existe"));
                System.out.println("El libro no existe");
            }
            else {
                libro.setTitulo(titulo);
                libro.setAutor(autor);
                daolibro.update(libro);
                System.out.println("en java" + libro);
                String json_response = conversorJson.writeValueAsString(libro);
                System.out.println("En java json" + json_response);
                impresora.println(json_response);
            }
        } else if (opcion.equals("delete")) {
            String isbn = request.getParameter("isbn");
            PrintWriter impresora = response.getWriter();
            ObjectMapper conversorJson = new ObjectMapper();
            conversorJson.registerModule(new JavaTimeModule());
            Libro libro = daolibro.getById(isbn);
            if(daolibro.getById(isbn) == null) {
                System.out.println("No existe el libro con ese isbn");
                impresora.println(conversorJson.writeValueAsString("El libro no existe"));
            }
            else {
                System.out.println("en java" + libro);
                String json_response = conversorJson.writeValueAsString(libro);
                System.out.println("En java json" + json_response);
                impresora.println(json_response);
                daolibro.deleteUsuario(libro);
            }
        }
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

            PrintWriter impresora = response.getWriter();
            ObjectMapper conversorJson = new ObjectMapper();
            conversorJson.registerModule(new JavaTimeModule());

            List<Libro> listaLibros  = daolibro.getAll();
            if(listaLibros.isEmpty()) {
                System.out.println("No hay libros en la biblioteca");
                impresora.println(conversorJson.writeValueAsString("No hay libros en la biblioteca"));
            }
            else {
                System.out.println("En java" + listaLibros);

                String json_response = conversorJson.writeValueAsString(listaLibros);
                System.out.println("En java json" + json_response);
                impresora.println(json_response);
            }

    }

    public void destroy(){

    }
}
