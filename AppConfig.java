package encyptiondecryptionproject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class AppConfig {

    private static final Properties enVars = new Properties();

    static {
        // Cargar las variables de entorno al inicializar la clase
        loadEnVars();
    }

    public static void loadEnVars() {
        try {
            // Crear un flujo de entrada para leer el archivo "paths.env"
            FileInputStream fileInputStream = new FileInputStream("paths.env");
            // Cargar las variables de entorno desde el archivo
            enVars.load(fileInputStream);
            // Cerrar el flujo de entrada
            fileInputStream.close();
        } catch (IOException e) {
            // Manejar una excepción si ocurre un error al cargar las variables de entorno
            e.printStackTrace();
        }
    }

    public static String getEncryptionDirectory() {
        // Obtener la ruta del directorio de encriptación desde las variables de entorno
        return enVars.getProperty("ENCRYPTION_FOLDER");
    }

    public static String getDecryptionDirectory() {
        // Obtener la ruta del directorio de desencriptación desde las variables de entorno
        return enVars.getProperty("DECRYPTION_FOLDER");
    }

    public static String getSecretKeysDirectory() {
        // Obtener la ruta del directorio de claves secretas desde las variables de entorno
        return enVars.getProperty("SECRET_KEYS_FOLDER");
    }
}
