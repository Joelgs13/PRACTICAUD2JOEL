import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GestionBinario {
    private static final String FILE_PATH = "olimpiadas.bin";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\nMenú de opciones:");
            System.out.println("1. Crear fichero serializable de olimpiadas");
            System.out.println("2. Añadir edición olímpica");
            System.out.println("3. Buscar olimpiadas por sede");
            System.out.println("4. Eliminar edición olímpica");
            System.out.println("5. Salir");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    crearFicheroSerializable(scanner);
                    break;
                case 2:
                    añadirEdicionOlimpica(scanner);
                    break;
                case 3:
                    buscarOlimpiadasPorSede(scanner);
                    break;
                case 4:
                    eliminarEdicionOlimpica(scanner);
                    break;
                case 5:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 5);
    }

    // Opción 1: Crear fichero serializable de olimpiadas a partir del XML
    public static void crearFicheroSerializable(Scanner scanner) {
        try {
            System.out.print("Introduce la ruta del archivo XML de olimpiadas: ");
            String rutaXML_Olimpiadas = scanner.nextLine();

            List<String[]> olimpiadas = leerXMLyConvertir(rutaXML_Olimpiadas);
            guardarOlimpiadasEnBinario(olimpiadas);

            System.out.println("Archivo binario de olimpiadas creado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Leer el archivo XML y convertir en una lista de arrays (String[]), cada uno representando una olimpiada
    public static List<String[]> leerXMLyConvertir(String rutaXML) {
        List<String[]> olimpiadas = new ArrayList<>();
        try {
            File inputFile = new File(rutaXML);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                String year, juegos, temporada, ciudad;
                boolean isJuegos = false, isTemporada = false, isCiudad = false;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("olimpiada")) {
                        year = attributes.getValue("year");
                    }
                    if (qName.equalsIgnoreCase("juegos")) {
                        isJuegos = true;
                    }
                    if (qName.equalsIgnoreCase("temporada")) {
                        isTemporada = true;
                    }
                    if (qName.equalsIgnoreCase("ciudad")) {
                        isCiudad = true;
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (isJuegos) {
                        juegos = new String(ch, start, length);
                        isJuegos = false;
                    }
                    if (isTemporada) {
                        temporada = new String(ch, start, length);
                        isTemporada = false;
                    }
                    if (isCiudad) {
                        ciudad = new String(ch, start, length);
                        isCiudad = false;
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equalsIgnoreCase("olimpiada")) {
                        olimpiadas.add(new String[]{year, juegos, temporada, ciudad});
                    }
                }
            };

            saxParser.parse(inputFile, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return olimpiadas;
    }

    // Guardar la lista de olimpiadas (List<String[]>) en un archivo binario
    public static void guardarOlimpiadasEnBinario(List<String[]> olimpiadas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(olimpiadas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Leer el archivo binario de olimpiadas
    public static List<String[]> leerOlimpiadasDesdeBinario() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<String[]>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Opción 2: Añadir edición olímpica
    public static void añadirEdicionOlimpica(Scanner scanner) {
        List<String[]> olimpiadas = leerOlimpiadasDesdeBinario();

        System.out.print("Introduce el año de la olimpiada: ");
        String year = scanner.nextLine();
        System.out.print("Introduce los juegos de la olimpiada: ");
        String juegos = scanner.nextLine();
        System.out.print("Introduce la temporada (Summer/Winter): ");
        String temporada = scanner.nextLine();
        System.out.print("Introduce la ciudad sede: ");
        String ciudad = scanner.nextLine();

        olimpiadas.add(new String[]{year, juegos, temporada, ciudad});
        guardarOlimpiadasEnBinario(olimpiadas);
        System.out.println("Edición olímpica añadida con éxito.");
    }

    // Opción 3: Buscar olimpiadas por sede
    public static void buscarOlimpiadasPorSede(Scanner scanner) {
        List<String[]> olimpiadas = leerOlimpiadasDesdeBinario();

        System.out.print("Introduce la ciudad a buscar: ");
        String ciudadBuscar = scanner.nextLine();

        List<String[]> resultado = olimpiadas.stream()
                .filter(o -> o[3].equalsIgnoreCase(ciudadBuscar))
                .collect(Collectors.toList());

        if (resultado.isEmpty()) {
            System.out.println("No se encontraron olimpiadas en la ciudad: " + ciudadBuscar);
        } else {
            resultado.forEach(o -> System.out.println("Año: " + o[0] + ", Juegos: " + o[1] + ", Temporada: " + o[2] + ", Ciudad: " + o[3]));
        }
    }

    // Opción 4: Eliminar edición olímpica
    public static void eliminarEdicionOlimpica(Scanner scanner) {
        List<String[]> olimpiadas = leerOlimpiadasDesdeBinario();

        System.out.print("Introduce el año de la olimpiada a eliminar: ");
        String yearEliminar = scanner.nextLine();
        System.out.print("Introduce la temporada de la olimpiada a eliminar (Summer/Winter): ");
        String temporadaEliminar = scanner.nextLine();

        List<String[]> olimpiadasFiltradas = olimpiadas.stream()
                .filter(o -> !(o[0].equals(yearEliminar) && o[2].equalsIgnoreCase(temporadaEliminar)))
                .collect(Collectors.toList());

        if (olimpiadasFiltradas.size() == olimpiadas.size()) {
            System.out.println("No se encontró una olimpiada con el año " + yearEliminar + " y temporada " + temporadaEliminar);
        } else {
            guardarOlimpiadasEnBinario(olimpiadasFiltradas);
            System.out.println("Edición olímpica eliminada con éxito.");
        }
    }
}
