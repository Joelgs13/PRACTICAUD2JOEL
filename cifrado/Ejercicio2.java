import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.nio.file.Paths;  // Para construir rutas basadas en el directorio actual

public class Ejercicio2 {

    public static void main(String[] args) {
        try {
            /**
             * Generamos una clave AES de 128 bits (16 bytes) que es
             * lo mas comun en el cifrado AES
             */
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);  // Clave de 128 bits
            SecretKey secretKey = keyGen.generateKey();
            
            /**
             * generamos un vector de inicializacion aleatorio
             */
            byte[] iv = new byte[16]; // AES usa un IV de 16 bytes
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            /**
             * Conseguimos la ruta absoluta del directorio donde se ejecuta este 
             * programa, por lo que el archivo que intentamos cifrar (palabras.txt)
             * y en el que las descifraremos estan en el mismo directorio
             */
            String currentDir = Paths.get("").toAbsolutePath().toString()+"/Prácticas/Practica 1/cifrado/";
            System.out.println("Directorio actual: " + currentDir);

            /**
             * crea la ruta de los 3 archivos a leer y escribir. los cuales se encuentran
             * en el mismo directorio del programa
             */
            File inputFile = new File(currentDir + "/palabras.txt");
            File encryptedFile = new File(currentDir + "/Encriptadas.bin");
            File decryptedFile = new File(currentDir + "/Desecryptadas.txt");

            /**
             * primero comprobamos que el archivo de entrada si existe para poder
             * leer las palabras que queremos cifrar
             */
            if (!inputFile.exists()) {
                System.out.println("El archivo de entrada no se encuentra: " + inputFile.getAbsolutePath());
                return;
            }

            /**
             * primero ciframos el archivo palabras.txt, usando la clave y
             * escribe el contenido en Encriptadas.bin
             */
            encryptFile(secretKey, ivSpec, inputFile, encryptedFile);

            /**
             * metodo para desencriptar el archivo, mismos parametros que el anterior
             * y misma funcionalidad
             */
            decryptFile(secretKey, ivSpec, encryptedFile, decryptedFile);
            
            System.out.println("Cifrado y descifrado completado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para cifrar un fichero
    public static void encryptFile(SecretKey key, IvParameterSpec iv, File inputFile, File outputFile) throws Exception {
        /**
         * Configuramos el cifrado AES con el modo CBC y le metemos de relleno
         * PKCS5Padding y despues iniciamos el Cipher en modo de encriptación 
         * con la clave secreta y el IV.
         */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        /**
         * abrimos el FileInputStream para leer el fichero y el 
         * FileOutputStream para escribir en otro el resulatdo de la encriptacion
         */
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        /**
         * leemos el archivo en bloques de 64 bytes, y lo ciframos, despues
         * lo escribimos en el fichero del OutputSream
         */
        byte[] buffer = new byte[64];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }
        /**
         * por ultimo, cojemos le contenido restante en el fichero y lo escribimos 
         * en el fichero de salida y se cierra los treams
         */
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }

        inputStream.close();
        outputStream.close();
    }

    /**
     * Metodo que desencripta el fichero 
     */
    public static void decryptFile(SecretKey key, IvParameterSpec iv, File inputFile, File outputFile) throws Exception {
        /**
         * configuramos el cypher en modo desencriptacion, tenemos el mismo proceso
         * que en el anterior proceso de cifrado
         */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        /**
         * se lee el archivo encriptado y se escribe el texto desencriptado en otro
         */
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        /**
         * leemos el archivo en bloques de 64 bytes, y lo ciframos, despues
         * lo escribimos en el fichero del OutputSream
         */
        byte[] buffer = new byte[64];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }
        /**por ultimo escribe lo que falte y cierra los streams */
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }

        inputStream.close();
        outputStream.close();
    }
}
