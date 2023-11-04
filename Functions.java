package encyptiondecryptionproject;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Scanner;

import static encyptiondecryptionproject.menu.checkFile;

public class Functions implements Interface_Proyecto {
    //global variables
    static String decryption_directory = AppConfig.getDecryptionDirectory();
    static String encryption_directory = AppConfig.getEncryptionDirectory();
    static String secretKeys_directory = AppConfig.getSecretKeysDirectory();

    // Implementa otros métodos de la interfaz

    @Override
    public void encrypt_menu() {
        String File = menu.choose_file_from_dir(encryption_directory, "encrypt");
        Scanner sc = new Scanner(System.in);
        String newFile;


        // Pide el nombre del archivo
        System.out.println("New encrypted file name: ");

        do {
            // Scanner , para el nombre del nuevo archivo, si este no tiene el .txt, se le agrega.
            newFile = sc.next();
            if (!newFile.contains(".txt")) {
                newFile = newFile + ".txt";
            }

            // si ya existe un archivo con ese nombre vuelve al loop del while.
            if (!checkFile(newFile, true)) {
                System.out.println("This file name already exists please type another filename: ");
            }
        } while (!checkFile(newFile, true));

        try {
            // Genera la key
            SecretKey secKey = AES.initializeKey();
            // Obtiene el texto que se va a encriptar
            String textToEncrypt = EncyptionDecryption.getTextFromFile(encryption_directory + "\\" + File);
            // Se encrypta lo devuelve en bytes y lo pasamos a Hex para guardarlo.
            byte[] test = AES.encrypt(textToEncrypt, secKey);
            String encryptedText = AES.bytesToHex(test);
            //Se guarda en forma de string
            EncyptionDecryption.store_in_file(encryptedText, decryption_directory + "\\" + newFile);
            // Encode de la key, se pasa a string y se guarda en un archivo
            byte[] keyBytes = secKey.getEncoded();
            String keyString = Base64.getEncoder().encodeToString(keyBytes);
            EncyptionDecryption.store_in_file(keyString, secretKeys_directory + "\\key_" + newFile);

            // Se guardan 2 archivos separados la key y el archivo encriptado.
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
}
