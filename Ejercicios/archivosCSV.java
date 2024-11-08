import java.io.*;
import java.util.Scanner;

public class archivosCSV {

	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n--- Menú de gestión de olimpiadas ---");
            System.out.println("1. Generar fichero CSV de olimpiadas");
            System.out.println("2. Buscar deportista");
            System.out.println("3. Buscar deportistas por deporte y olimpiada");
            System.out.println("4. Añadir deportista");
            System.out.println("5. Salir");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    generarFicheroOlimpiadas(scanner);
                    break;
                case 2:
                    buscarDeportista(scanner);
                    break;
                case 3:
                    buscarDeportistasPorDeporteYOlimpiada(scanner);
                    break;
                case 4:
                	aniadirDeportista(scanner);
                    break;
                case 5:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        } while (opcion != 5);
        scanner.close();
    }
    // Opción 1: Generar fichero olimpiadas.csv a partir del CSV original de atletas
    public static void generarFicheroOlimpiadas(Scanner scanner) {
        System.out.print("Introduce la ruta del archivo CSV de eventos de atletas: ");
        String rutaCSVOriginal = scanner.nextLine();
        System.out.print("Introduce la ruta donde se generará el archivo olimpiadas.csv: ");
        String rutaCSVSalida = scanner.nextLine();

        File archivoSalida = new File(rutaCSVSalida);

        // Asegurarse de que el directorio de destino exista
        File directorio = archivoSalida.getParentFile();
        if (directorio != null && !directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println("Directorio de destino creado: " + directorio.getAbsolutePath());
            } else {
                System.out.println("Error al crear el directorio de destino.");
                return;
            }
        }

        // Intentar generar el archivo CSV
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaCSVOriginal));
             BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida))) {

            writer.write("Games,Year,Season,City\n");  // Escribimos la cabecera
            String linea;
            reader.readLine();  // Saltamos la cabecera original
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(","); // Separar la línea por comas
                // Extraer la información de la olimpiada (Games, Year, Season, City)
                String games = datos[8];
                String year = datos[9];
                String season = datos[10];
                String city = datos[11];

                // Escribimos la información de la olimpiada
                writer.write(games + "," + year + "," + season + "," + city + "\n");
            }

            System.out.println("Fichero olimpiadas.csv generado con éxito en " + archivoSalida.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error al generar el fichero de olimpiadas: " + e.getMessage());
        }
    }

    // Opción 2: Buscar deportista por nombre
    public static void buscarDeportista(Scanner scanner) {
        System.out.print("Introduce la ruta del archivo CSV de eventos de atletas: ");
        String rutaCSV = scanner.nextLine();
        System.out.print("Introduce el nombre del deportista a buscar: ");
        String nombre = scanner.nextLine().toLowerCase();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaCSV))) {
            String linea;
            reader.readLine();  // Saltamos la cabecera
            boolean encontrado = false;

            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombreDeportista = datos[1].toLowerCase();

                if (nombreDeportista.contains(nombre)) {
                    encontrado = true;
                    // Mostrar detalles del deportista y sus participaciones
                    System.out.println("Deportista encontrado: " + datos[1]);
                    System.out.println("Sexo: " + datos[2] + ", Edad: " + datos[3] + ", Altura: " + datos[4] +
                            ", Peso: " + datos[5] + ", Equipo: " + datos[6] + ", País: " + datos[7]);
                    System.out.println("Participación olímpica: " + datos[8] + ", Año: " + datos[9] + 
                            ", Temporada: " + datos[10] + ", Ciudad: " + datos[11] + ", Deporte: " + datos[12] +
                            ", Evento: " + datos[13] + ", Medalla: " + datos[14]);
                }
            }

            if (!encontrado) {
                System.out.println("No se encontraron deportistas con el nombre proporcionado.");
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    // Opción 3: Buscar deportistas por deporte y olimpiada
    public static void buscarDeportistasPorDeporteYOlimpiada(Scanner scanner) {
        System.out.print("Introduce la ruta del archivo CSV de eventos de atletas: ");
        String rutaCSV = scanner.nextLine();
        System.out.print("Introduce el nombre del deporte: ");
        String deporte = scanner.nextLine().toLowerCase();
        System.out.print("Introduce el año de la olimpiada: ");
        String year = scanner.nextLine();
        System.out.print("Introduce la temporada (Summer o Winter): ");
        String season = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaCSV))) {
            String linea;
            reader.readLine();  // Saltamos la cabecera
            boolean encontrado = false;

            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                String deporteActual = datos[12].toLowerCase();
                String yearActual = datos[9];
                String seasonActual = datos[10].toLowerCase();

                if (deporteActual.equals(deporte) && yearActual.equals(year) && seasonActual.equals(season.toLowerCase())) {
                    encontrado = true;
                    // Mostrar detalles del deportista y la edición olímpica
                    System.out.println("Deportista: " + datos[1] + ", Evento: " + datos[13] + ", Medalla: " + datos[14]);
                }
            }

            if (!encontrado) {
                System.out.println("No se encontraron deportistas para el deporte y la olimpiada especificados.");
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    // Opción 4: Añadir deportista
    public static void aniadirDeportista(Scanner scanner) {
        System.out.print("Introduce la ruta del archivo CSV de eventos de atletas: ");
        String rutaCSV = scanner.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaCSV, true))) {
            // Pedir información al usuario
            System.out.print("ID: ");
            String id = scanner.nextLine();
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Sexo (M/F): ");
            String sexo = scanner.nextLine();
            System.out.print("Edad: ");
            String edad = scanner.nextLine();
            System.out.print("Altura: ");
            String altura = scanner.nextLine();
            System.out.print("Peso: ");
            String peso = scanner.nextLine();
            System.out.print("Equipo: ");
            String equipo = scanner.nextLine();
            System.out.print("País (NOC): ");
            String noc = scanner.nextLine();
            System.out.print("Juegos (e.g., 1992 Summer): ");
            String games = scanner.nextLine();
            System.out.print("Año: ");
            String year = scanner.nextLine();
            System.out.print("Temporada (Summer/Winter): ");
            String season = scanner.nextLine();
            System.out.print("Ciudad: ");
            String ciudad = scanner.nextLine();
            System.out.print("Deporte: ");
            String deporte = scanner.nextLine();
            System.out.print("Evento: ");
            String evento = scanner.nextLine();
            System.out.print("Medalla (Gold/Silver/Bronze/NA): ");
            String medalla = scanner.nextLine();

            // Escribimos los datos del nuevo deportista en el archivo CSV
            writer.write(id + "," + nombre + "," + sexo + "," + edad + "," + altura + "," + peso + "," + equipo + "," +
                         noc + "," + games + "," + year + "," + season + "," + ciudad + "," + deporte + "," + evento + "," + medalla + "\n");

            System.out.println("Deportista añadido con éxito.");
        } catch (IOException e) {
            System.out.println("Error al añadir el deportista al archivo CSV: " + e.getMessage());
        }
    }
}