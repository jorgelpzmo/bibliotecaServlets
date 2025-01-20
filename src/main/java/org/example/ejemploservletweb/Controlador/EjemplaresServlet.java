
package org.example.ejemploservletweb.Controlador;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ejemploservletweb.Modelo.DAOGenerico;
import org.example.ejemploservletweb.Modelo.Ejemplar;
import org.example.ejemploservletweb.Modelo.Libro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ejemplaresServlet", value="/ejemplares-servlet")
public class EjemplaresServlet extends HttpServlet {
        DAOGenerico<Ejemplar, String> daoejemplar;
        DAOGenerico<Libro, String> daolibro;

        public void init(){
            daoejemplar = new DAOGenerico<>(Ejemplar.class, String.class);
            daolibro = new DAOGenerico<>(Libro.class, String.class);
        }

        public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("application/json");
            String opcion = request.getParameter("opcion");

            if(opcion.equals("get")){
                String id = request.getParameter("id");
                PrintWriter impresora = response.getWriter();
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                Ejemplar ejemplar = daoejemplar.getById(id);
                if(ejemplar == null){
                    System.out.println("No existe un libro con ese id");
                    impresora.println(mapper.writeValueAsString("No existe el libro con ese isbn"));
                }else{
                    System.out.println("En java" + ejemplar);
                    String json_response = mapper.writeValueAsString(ejemplar);
                    System.out.println("En java" + json_response);
                    impresora.println(json_response);
                }
            } else if (opcion.equals("add")) {
                String isbn = request.getParameter("isbn");
                String estado = request.getParameter("estado");
                PrintWriter impresora = response.getWriter();
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                Ejemplar ejemplar = new Ejemplar();
                Libro libro = daolibro.getById(isbn);
                if(libro == null){
                    System.out.println("No existe un libro con ese isbn");
                    impresora.println(mapper.writeValueAsString("No existe el libro con ese isbn"));
                }else {
                    ejemplar.setLibro(libro);
                    ejemplar.setEstado(estado);
                        daoejemplar.add(ejemplar);
                        String json_response2 = mapper.writeValueAsString(ejemplar);
                        System.out.println("En java" + ejemplar);
                        System.out.println("En java" + json_response2);
                        impresora.println(json_response2);
                    }
                } else if (opcion.equals("update")) {
                String id = request.getParameter("id");
                String isbn = request.getParameter("isbn");
                String estado = request.getParameter("estado");
                PrintWriter impresora = response.getWriter();
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                Ejemplar ejemplar = daoejemplar.getById(id);
                Libro libro = daolibro.getById(isbn);
                if(libro == null){
                    System.out.println("No existe un libro con ese isbn");
                    impresora.println(mapper.writeValueAsString("No existe un libro con ese isbn"));
                }else{
                    ejemplar.setLibro(libro);
                    ejemplar.setEstado(estado);
                    daoejemplar.update(ejemplar);
                    String json_response2 = mapper.writeValueAsString(ejemplar);
                    System.out.println("En java" + ejemplar);
                    System.out.println("En java" + json_response2);
                    impresora.println(json_response2);
                }

            }else if (opcion.equals("delete")) {
                String id = request.getParameter("id");
                PrintWriter impresora = response.getWriter();
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                Ejemplar ejemplar = daoejemplar.getById(id);
                if(ejemplar == null){
                    System.out.println("No existe un libro con ese isbn");
                    impresora.println(mapper.writeValueAsString("No existe un libro con ese isbn"));
                }else{
                    String json_response2 = mapper.writeValueAsString(ejemplar);
                    System.out.println("En java" + ejemplar);
                    System.out.println("En java" + json_response2);
                    impresora.println(json_response2);
                    daoejemplar.deleteUsuario(ejemplar);
                    impresora.println(mapper.writeValueAsString("Eliminado"));
                }
            }


        }
        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("application/json");
            PrintWriter impresora = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            List<Ejemplar> ejemplares = daoejemplar.getAll();
            if(ejemplares.isEmpty()){
                System.out.println("No hay ejemplares disponibles");
                impresora.println(mapper.writeValueAsString("No hay ejemplares disponibles"));
            }else{
                impresora.println(mapper.writeValueAsString(ejemplares));
                System.out.println("En java" + ejemplares);

            }
        }
        }
