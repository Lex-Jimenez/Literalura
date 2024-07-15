package com.aluracursos.literalura.main;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.IAutorRepository;
import com.aluracursos.literalura.repository.ILibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvertirDatos;
import com.aluracursos.literalura.service.RespuestaApi;
import jakarta.transaction.Transactional;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.security.PublicKey;
import java.util.*;

public class Principal {

private Scanner teclado = new Scanner(System.in);
private ConsumoAPI consumoApi = new ConsumoAPI();
private static String API_BASE = "https://gutendex.com/books/?search=";
private ConvertirDatos convertirDatos = new ConvertirDatos();
private List<Libro> datosLibro = new ArrayList<>();
private ILibroRepository libroRepository;
private IAutorRepository autorRepository;
public Principal(ILibroRepository libroRepository, IAutorRepository autorRepository){
    this.libroRepository = libroRepository;
    this.autorRepository = autorRepository;
}

    public void LiteraluraMenu(){
        var option = -1;
        while (option!=0){
            var menu = """
                    1 - Buscar libro por título.
                    2 - Listar libros registrados.
                    3 - Listar autores registrados.
                    4 - Listar autores vivos en un año determinado.
                    5 - Listar libros por idioma.
                    
                    0 - Salir.
                    """;
            try{
                System.out.println(menu);
                option = teclado.nextInt();
                teclado.nextLine();
            }catch (InputMismatchException e){
                System.out.println("Por favor ingrese una opción valida.");
                teclado.nextLine();
                continue;
            }
            switch (option){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    librosBuscados();
                    break;
                case 3:
                    buscarLibroPorNombre();
                    break;
                case 4:
                    buscarAutores();
                    break;
                case 5:
                    buscarAutoresPorAno();
                    break;
                case 6:
                    buscarLibrosPorIdioma();
                    break;
                case 7:
                    findTop10ByTituloByCantidadDescargas();
                    break;
                case 8:
                    buscarAutorPorNombre();
                    break;
            }



        }
    }

    private Libro obtenerDatosLibros(){
        System.out.println("Escriba el nombre del libro que desea buscar: ");
        var libroBuscado = teclado.nextLine().toLowerCase();
        var json = consumoApi.obtenerDatos(API_BASE + libroBuscado.replace(" ", "%20"));
        RespuestaApi datos = convertirDatos.convierteDatosDesdeJson(json, RespuestaApi.class);

        if (datos!=null && datos.getResultadoLibros()!=null && !datos.getResultadoLibros().isEmpty()){
            DatosLibro topListaLibro = datos.getResultadoLibros().get(0);
            return new Libro(topListaLibro);
        }else {
            System.out.println("No se encontró el libro buscado.");
            return null;
        }
    }

    private void buscarLibro(){
        Libro libro = obtenerDatosLibros();

        if (libro == null){
            System.out.println("No se encontró el libro buscado.");
            return;
        }

        try {
            boolean libroExists = libroRepository.existByTitulo(libro.getTitulo());
            if (libroExists){
                System.out.println("Libro previamente archivado.");
            }else {
                libroRepository.save(libro);
                System.out.println(libro.toString());
            }
        }catch (InvalidDataAccessApiUsageException e){
            System.out.println("Ocurrió un error");
        }
    }
    @Transactional
    private void librosBuscados(){
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()){
            System.out.println("Aun no hay registros");
        }else {
            System.out.println("Libros registrados:");
            for (Libro libro:libros){
                System.out.println(libro.toString());
            }
        }
    }
    private void buscarLibroPorNombre(){
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()){
            System.out.println("No se encontró el libro en la base de datos.");
        }else {
            System.out.println("Libros encontrados en la base de datos: ");
            for (Libro libro:libros){
                System.out.println(libro.toString());
            }
        }
    }
    private void buscarAutores(){
    List<Autor> autores = autorRepository.findAll();

    if (autores.isEmpty()){
        System.out.println("No se encontraron autores registrados en la base de datos.\n");
        }else {
            System.out.println("Autores registrados en la base de datos: \n");
            Set<String> autoresRegistrados = new HashSet<>();
            for (Autor autor : autores){
                if (autoresRegistrados.add(autor.getNombre())){
                    System.out.println(autor.getNombre()+'\n');
                }
            }
        }
    }
    private void buscarAutoresPorAno(){
        System.out.println("Por favor indica el año: \n");
        var anoBuscado = teclado.nextInt();

        List<Autor> autoresAno = autorRepository.findByFechaNacimientoLessThanOrFechaFallecimientoGreaterThanEqual(anoBuscado, anoBuscado);

        if (autoresAno.isEmpty()){
            System.out.println("No se encontraron autores para el año" + anoBuscado + ".");
        }else {
            System.out.println("Los autores para el año " + anoBuscado + "son: ");
            Set<String> autoresUnicos = new HashSet<>();

            for (Autor autor: autoresAno){
                if (autor.getFechaNacimiento() != null && autor.getFechaFallecimiento() != null){
                    if (autor.getFechaNacimiento() <= anoBuscado && autor.getFechaFallecimiento() >= anoBuscado){
                        if (autoresUnicos.add(autor.getNombre())){
                            System.out.println("Autor: " + autor.getNombre());
                        }
                    }
                }
            }
        }
    }
    private void buscarLibrosPorIdioma(){
        System.out.println("""
                            Ingrese el idioma que desea buscar
                            Tenga en mente ingresar el código del idioma así:
                            
                            Español: es
                            Inglés: en
                            """);
        var idioma = teclado.nextLine();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()){
            System.out.println("No se encontraron libros en el idioma buscado");
        }else {
            System.out.println("libros encontrados en la base de datos:");
            for (Libro libro: librosPorIdioma){
                System.out.println(libro.toString());
            }
        }

    }
    private void findTop10ByTituloByCantidadDescargas(){
        List<Libro> top10Libros = libroRepository.findTop10ByTituloByCantidadDescargas();
        if (!top10Libros.isEmpty()) {
            int index = 1;
            for (Libro libro : top10Libros) {
                System.out.printf("Libro %d: %s Autor: %s Descargas: %d\n",
                        index, libro.getTitulo(), libro.getAutor().getNombre(), libro.getCantidadDeDescargas());
                index++;
            }
        }
    }
    private void buscarAutorPorNombre() {
        System.out.println("Ingrese nombre del escritor que quiere buscar: ");
        var autorPorNombre = teclado.nextLine();
        Optional<Autor> autorPorNombreBuscado = autorRepository.findFirstByNombreContainsIgnoreCase(autorPorNombre);
        if (autorPorNombreBuscado.isPresent()) {
            System.out.println("\nEl escritor buscado fue: " + autorPorNombreBuscado.get().getNombre());

        } else {
            System.out.println("\nEl escritor " + autorPorNombre + "' no se encuentra en la base de datos.");
        }
    }



}
