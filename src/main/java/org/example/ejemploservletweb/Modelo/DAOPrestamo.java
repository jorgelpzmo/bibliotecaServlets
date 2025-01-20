package org.example.ejemploservletweb.Modelo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class DAOPrestamo {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("biblioteca");
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    public DAOPrestamo() {
    }

    public List<Prestamo> getPrestamoByUsuario(Usuario usuario) {
        return em.createQuery("SELECT p FROM Prestamo p WHERE p.usuario = :usuario", Prestamo.class)
                .setParameter("usuario", usuario)
                .getResultList();
    }
}
