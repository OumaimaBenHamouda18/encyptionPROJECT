/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package encyptiondecryptionproject;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.Scanner;

//custom exception class
class EmptyFileException extends Exception {
    public EmptyFileException(String message) {
        super(message);
    }
}

public class Test implements Interface_Proyecto {
    @Override

}

public class EncyptionDecryptionProject {

    public static Scanner openFile(String path) throws FileNotFoundException, EmptyFileException {

        File fileRef = new File(path);
        Scanner sc = new Scanner(fileRef);
        if (!sc.hasNextLine()) {
            throw new EmptyFileException("File is empty");
        } else
            return sc;
    }


    public static String getTextToEncrypt(String path) {
        Scanner sc = null;
        try {
            sc = openFile(path);
        } catch (FileNotFoundException | EmptyFileException e) {
            System.out.println("Error: " + e.getMessage());
        }
        String textToEncrypt = "";
        if (sc != null)
            while (sc.hasNextLine())
                textToEncrypt += sc.nextLine() + "\n";

        return textToEncrypt;
    }


    public static String getText(String path) {
        Scanner sc = null;
        try {
            sc = openFile(path);
        } catch (FileNotFoundException | EmptyFileException e) {
            System.out.println("Error: " + e.getMessage());
        }
        String textToEncrypt = "";
        if (sc != null)
            while (sc.hasNextLine())
                textToEncrypt += sc.nextLine();

        return textToEncrypt;
    }

    /**
     * @param args the command line arguments
     */

    //
    public static void store_in_file(String textToStore, String path) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(textToStore);
        bw.close();
        System.out.println("Encriptado exitoso");

    }


    @Override
    public void Encrypt(String File, String path) {
        String textToEncrypt = getTextToEncrypt("C:\\Users\\oumai\\OneDrive\\Escritorio\\ENCRYPTION_DECRYPTION_PROJECT_JAVA\\encyptionPROJECT\\files_encrypt\\" + File);
        System.out.println("El texto que se va a encriptar es:\n" + textToEncrypt);
        try {
            SecretKey secKey = AES.initializeKey();
            String encryptedText = AES.bytesToHex(AES.encrypt(textToEncrypt, secKey));
            System.out.println("Encrypted Text: " + encryptedText);
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            byte[] keyBytes = secKey.getEncoded();
            String keyString = Base64.getEncoder().encodeToString(keyBytes);
            bw.write(keyString);
            bw.close();
            store_in_file(encryptedText, path);
        } catch (Exception ignored) {
        }
    }


    public static SecretKey getSecretKey(String keyString) throws Exception {

        //convert secret key from string to bytes
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        //define key specification
        // Create a SecretKey from the keyBytes
        SecretKey secKey = new SecretKeySpec(keyBytes, "AES");
        return secKey;
    }


}
