package encyptiondecryptionproject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class AppConfig {

    private static final Properties enVars = new Properties();

    static {
        loadEnVars();
    }

    public static void loadEnVars() {

        try {
            FileInputStream fileInputStream = new FileInputStream("paths.env");
            enVars.load(fileInputStream);
            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getEncryptionDirectory() {
        return enVars.getProperty("ENCRYPTION_FOLDER");
    }

    public static String getDecryptionDirectory() {
        return enVars.getProperty("DECRYPTION_FOLDER");
    }

    public static String getSecretKeysDirectory() {
        return enVars.getProperty("SECRET_KEYS_FOLDER");
    }


}