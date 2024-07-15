package com.aluracursos.literalura.service;

public interface IConvierteDatos {
    <T> T convierteDatosDesdeJson (String json, Class<T> clase);
}
