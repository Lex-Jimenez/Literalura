package com.aluracursos.literalura.repository;
import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface ILibroRepository extends JpaRepository<Libro, Long> {

    boolean existByTitulo(String titulo);

    List<Libro> findByIdioma(String idioma);

    @Query("SELEC l FROM Libro l ORDER BY l.cantidadDescargas DESC LIMIT 10")
    List<Libro> findTop10ByTituloByCantidadDescargas();
}
