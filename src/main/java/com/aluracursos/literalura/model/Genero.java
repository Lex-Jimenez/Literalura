package com.aluracursos.literalura.model;

public enum Genero {
    ACCION ("Action"),
    ROMANCE ("Romance"),
    COMEDIA ("Comedy"),
    DRAMA ("Drama"),
    AVENTURA ("Adventure"),
    FICCION ("Fiction"),
    DESCONOCIDO ("Desconocido");

    private String genero;

    Genero(String generoFromApi){
        this.genero = generoFromApi;
    }

    public static Genero fromString(String text){
        for (Genero generoEnum: Genero.values()){
            if (generoEnum.genero.equals(text)){
                return generoEnum;
            }
        }return Genero.DESCONOCIDO;
    }
}
