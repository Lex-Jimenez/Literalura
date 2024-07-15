package com.aluracursos.literalura.model;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("id") Long libroId,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") Autor autor,
        @JsonAlias("subjects") Genero genero,
        @JsonAlias("languages") String idioma,
        @JsonAlias("download_count") Long cantidadDeDescargas) {
}
