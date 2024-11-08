import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

/**
 * Esta clase SistemaArchivos, mustra un menu con 6 opciones distintas, el cual se repite despues 
 * de elegir cada opcion o equivocarte al escribir una opcion, exceptuando la sexta, que te saca del programa
 * y es la unica manera de hacerlo. El resto de opciones, llaman a metodos internos de la propia clase
 * que sirven para cumplir con lo deseado: crear o listar un directorio, copiar o mover un archivo de sitio,
 * eliminar un directorio o archivo y parar la ejecucuion del menu.
 * 
 * @author Joel
 */
public class SistemaArchivos {

    /**
     * Crea un nuevo directorio en la ruta especificada por el usuario.
     * Si el directorio ya existe, informa al usuario. Si no existe, intenta crearlo.
     * para ello, usa mkdirs
     * 
     * @param scanner Scanner utilizado para leer la entrada del usuario.
     */
    public static void crearDirectorio(Scanner scanner) {
        System.out.print("Escribe la ruta donde crear el directorio: ");
        String ruta = scanner.nextLine();
        
        System.out.print("Introduce el nombre del nuevo directorio: ");
        String nombreDirectorio = scanner.nextLine();

        File nuevoDirectorio = new File(ruta + "/" + nombreDirectorio);

        if (nuevoDirectorio.exists()) {
            System.out.println("El directorio ya existe.");
        } else {
            if (nuevoDirectorio.mkdirs()) {
                System.out.println("Directorio creado con éxito.");
            } else {
                System.out.println("Error al crear el directorio.");
            }
        }
    }

    /**
     * Lista el contenido de un directorio especificado por el usuario.
     * Distingue entre archivos y directorios en la salida de consola.
     * para ello usa el metodo isDirectory, asi sabremos si se trata de un archivo o directorio
     * itera sobre el directorio y recoje cada nombre de archivo, crea un nuevo
     * file a partir de la ruta del directorio y el objeto recojido y evalua lo que es.
     * 
     * @param scanner Scanner utilizado para leer la entrada del usuario.
     */
    public static void listarDirectorio(Scanner scanner) {
        System.out.print("Introduce la ruta del directorio a listar: ");
        String ruta = scanner.nextLine();

        File directorio = new File(ruta);
        if (directorio.isDirectory()) {
            String[] contenido = directorio.list();
            if (contenido != null) {
                for (String item : contenido) {
                    File archivoODirectorio = new File(ruta + "/" + item);
                    if (archivoODirectorio.isDirectory()) {
                        System.out.println("[DIRECTORIO] " + item);
                    } else {
                        System.out.println("[ARCHIVO] " + item);
                    }
                }
            } else {
                System.out.println("El directorio está vacío.");
            }
        } else {
            System.out.println("La ruta no es un directorio.");
        }
    }

    /**
     * Copia un archivo a un directorio de destino especificado por el usuario.
     * Si el archivo ya existe en el destino, lo sobrescribe.
     * se asegura de que existe y es un archivo y no un directorio
     * 
     * @param scanner Scanner utilizado para leer la entrada del usuario.
     */
    public static void copiarArchivo(Scanner scanner) {
        System.out.print("Introduce la ruta del archivo a copiar: ");
        String rutaArchivoOriginal = scanner.nextLine();
        System.out.print("Introduce la ruta del directorio de destino: ");
        String rutaDestino = scanner.nextLine();

        File archivoOriginal = new File(rutaArchivoOriginal);
        File archivoDestino = new File(rutaDestino + "/" + archivoOriginal.getName());

        if (archivoOriginal.exists() && archivoOriginal.isFile()) {
            try {
                Files.copy(archivoOriginal.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Archivo copiado con éxito.");
            } catch (IOException e) {
                System.out.println("Error al copiar el archivo: " + e.getMessage());
            }
        } else {
            System.out.println("El archivo no existe o no es válido.");
        }
    }

    /**
     * Mueve un archivo a un directorio de destino especificado por el usuario.
     * Si el archivo ya existe en el destino, lo sobrescribe.
     * se asegura de que existe y de que es un archivo y no un directorio
     * 
     * @param scanner Scanner utilizado para leer la entrada del usuario.
     */
    public static void moverArchivo(Scanner scanner) {
        System.out.print("Introduce la ruta del archivo a mover: ");
        String rutaArchivo = scanner.nextLine();
        System.out.print("Introduce la ruta del directorio de destino: ");
        String rutaDestino = scanner.nextLine();

        File archivo = new File(rutaArchivo);
        File archivoDestino = new File(rutaDestino + "/" + archivo.getName());

        if (archivo.exists() && archivo.isFile()) {
            try {
                Files.move(archivo.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Archivo movido con éxito.");
            } catch (IOException e) {
                System.out.println("Error al mover el archivo: " + e.getMessage());
            }
        } else {
            System.out.println("El archivo no existe o no es válido.");
        }
    }

    /**
     * Elimina un archivo o directorio especificado por el usuario.
     * Si es un directorio, solo lo elimina si está vacío.
     * primedo pide la ruta al usuario, para despues hacer varias comprobaciones
     * primero si existe, despues si es un directorio o no y despues si contiene algo dentro.
     * 
     * @param scanner Scanner utilizado para leer la entrada del usuario.
     */
    public static void eliminarArchivoODirectorio(Scanner scanner) {
        System.out.print("Introduce la ruta del archivo o directorio a eliminar: ");
        String ruta = scanner.nextLine();

        File archivoODirectorio = new File(ruta);

        if (archivoODirectorio.exists()) {
            if (archivoODirectorio.isDirectory()) {
                if (archivoODirectorio.list().length == 0) {
                    archivoODirectorio.delete();
                    System.out.println("Directorio eliminado con éxito.");
                } else {
                    System.out.println("El directorio no está vacío. No se puede eliminar.");
                }
            } else {
                if (archivoODirectorio.delete()) {
                    System.out.println("Archivo eliminado con éxito.");
                } else {
                    System.out.println("Error al eliminar el archivo.");
                }
            }
        } else {
            System.out.println("El archivo o directorio no existe.");
        }
    }

    /**
     * inicia la ejecución del programa.
     * Muestra un menú con varias opciones para manejar archivos y directorios.
     * 
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int opcion;
            
            do {
                // Mostrar menú
                System.out.println("\nMenú de opciones:");
                System.out.println("1. Crear un directorio");
                System.out.println("2. Listar un directorio");
                System.out.println("3. Copiar un archivo");
                System.out.println("4. Mover un archivo");
                System.out.println("5. Eliminar un archivo/directorio");
                System.out.println("6. Salir");
                System.out.print("Elige una opción: ");
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
                
                switch (opcion) {
                    case 1:
                        crearDirectorio(scanner);
                        break;
                    case 2:
                        listarDirectorio(scanner);
                        break;
                    case 3:
                        copiarArchivo(scanner);
                        break;
                    case 4:
                        moverArchivo(scanner);
                        break;
                    case 5:
                        eliminarArchivoODirectorio(scanner);
                        break;
                    case 6:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } while (opcion != 6);
        }
    }
}

