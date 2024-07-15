package com.aluracursos.literalura.repository;
import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAutorRepository extends JpaRepository<Autor, Long> {

    List<Autor> findAll();

    List<Autor> findByFechaNacimientoLessThanOrFechaFallecimientoGreaterThanEqual(int anoBuscado, int anoBuscado1);

    Optional<Autor> findFirstByNombreContainsIgnoreCase(String escritor);
}
