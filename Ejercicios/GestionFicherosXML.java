
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Scanner;
import java.util.TreeMap;

public class GestionFicherosXML {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\nMenú de opciones:");
            System.out.println("1. Crear fichero XML de olimpiadas");
            System.out.println("2. Crear fichero XML de deportistas");
            System.out.println("3. Listado de olimpiadas");
            System.out.println("4. Salir");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();  // limpiar buffer

            switch (opcion) {
                case 1:
                    crearXMLDeOlimpiadas(scanner);
                    break;
                case 2:
                    crearXMLDeDeportistas(scanner);
                    break;
                case 3:
                    listadoDeOlimpiadas(scanner);
                    break;
                case 4:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 4);
    }

    // Opción 1: Crear fichero XML de olimpiadas
    public static void crearXMLDeOlimpiadas(Scanner scanner) {
        try {
            // Pedir la ruta del archivo CSV de olimpiadas
            System.out.print("Introduce la ruta del archivo CSV de olimpiadas: ");
            String rutaCSV_Olimpiadas = scanner.nextLine();

            // Pedir la ruta del archivo XML donde se va a guardar el resultado
            System.out.print("Introduce la ruta donde se guardará el archivo XML de olimpiadas: ");
            String rutaXML_Olimpiadas = scanner.nextLine();

            // Leer el archivo CSV de olimpiadas usando la ruta proporcionada
            BufferedReader csvReader = new BufferedReader(new FileReader(rutaCSV_Olimpiadas));
            String row;
            TreeMap<String, String[]> olimpiadasMap = new TreeMap<>();

            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                String year = data[0];  // Suponiendo que el año está en la primera columna
                String games = data[1];  // Suponiendo que el nombre de los juegos está en la segunda columna
                String season = data[2]; // Suponiendo que la temporada está en la tercera columna
                String city = data[3];   // Suponiendo que la ciudad está en la cuarta columna

                olimpiadasMap.put(year + season, new String[]{games, season, city});
            }
            csvReader.close();

            // Crear documento XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // Elemento raíz
            Element rootElement = doc.createElement("olimpiadas");
            doc.appendChild(rootElement);

            // Crear nodos de olimpiadas
            for (String key : olimpiadasMap.keySet()) {
                String[] data = olimpiadasMap.get(key);

                Element olimpiada = doc.createElement("olimpiada");
                olimpiada.setAttribute("year", key.substring(0, 4));  // Tomar solo el año
                rootElement.appendChild(olimpiada);

                Element juegos = doc.createElement("juegos");
                juegos.appendChild(doc.createTextNode(data[0]));
                olimpiada.appendChild(juegos);

                Element temporada = doc.createElement("temporada");
                temporada.appendChild(doc.createTextNode(data[1]));
                olimpiada.appendChild(temporada);

                Element ciudad = doc.createElement("ciudad");
                ciudad.appendChild(doc.createTextNode(data[2]));
                olimpiada.appendChild(ciudad);
            }

            // Guardar el archivo XML en la ruta especificada
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(rutaXML_Olimpiadas));
            transformer.transform(source, result);

            System.out.println("Archivo olimpiadas.xml creado con éxito en la ruta: " + rutaXML_Olimpiadas);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Opción 2: Crear fichero XML de deportistas
    public static void crearXMLDeDeportistas(Scanner scanner) {
        try {
            // Pedir la ruta del archivo CSV de deportistas
            System.out.print("Introduce la ruta del archivo CSV de deportistas: ");
            String rutaCSV_Deportistas = scanner.nextLine();

            // Pedir la ruta del archivo XML donde se va a guardar el resultado
            System.out.print("Introduce la ruta donde se guardará el archivo XML de deportistas: ");
            String rutaXML_Deportistas = scanner.nextLine();

            // Leer el archivo CSV de deportistas usando la ruta proporcionada
            BufferedReader csvReader = new BufferedReader(new FileReader(rutaCSV_Deportistas));
            String row;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("deportistas");
            doc.appendChild(rootElement);

            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                Element deportista = doc.createElement("deportista");
                deportista.setAttribute("id", data[0]);  // Asumimos que el ID está en la primera columna
                rootElement.appendChild(deportista);

                Element nombre = doc.createElement("nombre");
                nombre.appendChild(doc.createTextNode(data[1]));
                deportista.appendChild(nombre);

                Element sexo = doc.createElement("sexo");
                sexo.appendChild(doc.createTextNode(data[2]));
                deportista.appendChild(sexo);

                Element altura = doc.createElement("altura");
                altura.appendChild(doc.createTextNode(data[3]));
                deportista.appendChild(altura);

                Element peso = doc.createElement("peso");
                peso.appendChild(doc.createTextNode(data[4]));
                deportista.appendChild(peso);

                // Suponiendo que tenemos múltiples participaciones
                Element participaciones = doc.createElement("participaciones");
                deportista.appendChild(participaciones);

                Element deporte = doc.createElement("deporte");
                deporte.setAttribute("nombre", data[5]);  // Asumimos que el deporte está en la columna 6
                participaciones.appendChild(deporte);

                Element participacion = doc.createElement("participacion");
                participacion.setAttribute("edad", data[6]);  // Edad del deportista
                deporte.appendChild(participacion);

                Element equipo = doc.createElement("equipo");
                equipo.setAttribute("abbr", data[7]);  // Abreviatura del país o NOC
                equipo.appendChild(doc.createTextNode(data[8]));  // Nombre completo del equipo
                participacion.appendChild(equipo);

                Element juegos = doc.createElement("juegos");
                juegos.appendChild(doc.createTextNode(data[9]));  // Juegos + Ciudad
                participacion.appendChild(juegos);

                Element evento = doc.createElement("evento");
                evento.appendChild(doc.createTextNode(data[10]));  // Evento en el que participó
                participacion.appendChild(evento);

                Element medalla = doc.createElement("medalla");
                medalla.appendChild(doc.createTextNode(data[11]));  // Medalla ganada
                participacion.appendChild(medalla);
            }

            csvReader.close();

            // Guardar el archivo XML en la ruta especificada
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(rutaXML_Deportistas));
            transformer.transform(source, result);

            System.out.println("Archivo deportistas.xml creado con éxito en la ruta: " + rutaXML_Deportistas);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Opción 3: Listar olimpiadas utilizando SAX
    public static void listadoDeOlimpiadas(Scanner scanner) {
        try {
            // Pedir la ruta del archivo XML de olimpiadas
            System.out.print("Introduce la ruta del archivo XML de olimpiadas: ");
            String rutaXML_Olimpiadas = scanner.nextLine();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                boolean juegos = false;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("juegos")) {
                        juegos = true;
                    }
                    if (qName.equalsIgnoreCase("olimpiada")) {
                        System.out.println("Año: " + attributes.getValue("year"));
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (juegos) {
                        System.out.println("Juegos: " + new String(ch, start, length));
                        juegos = false;
                    }
                }
            };

            // Usar la ruta proporcionada para el XML de olimpiadas
            File inputFile = new File(rutaXML_Olimpiadas);
            saxParser.parse(inputFile, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

