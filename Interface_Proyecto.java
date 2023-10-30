package encyptiondecryptionproject;

import javax.crypto.SecretKey;
import java.io.File;

public interface Interface_Proyecto{

    static void Encrypt(String File, String path) {}
    static boolean checkFile(String file, boolean encriptar) {
        return false;
    }
    static String choose_file_option(File dir) {
        return null;
    }
    static void decrypt_menu() {}
    static void encrypt_menu() {}
    static String getTextToEncrypt(String path) {
        return null;
    }
    static String getText(String path) {
        return null;
    }
    static void store_in_file(String textToStore, String path) {}
    static SecretKey getSecretKey(String keyString) {
        return null;
    }

}