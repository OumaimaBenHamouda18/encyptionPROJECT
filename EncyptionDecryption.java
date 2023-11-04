
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


public class EncyptionDecryption {

    public static Scanner openFile(String path) throws FileNotFoundException, EmptyFileException {

        File fileRef = new File(path);
        Scanner sc = new Scanner(fileRef);
        if (!sc.hasNextLine()) {
            throw new EmptyFileException("File is empty");
        } else
            return sc;
    }


    public static String getTextFromFile(String path) {
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


    public static void store_in_file(String textToStore, String path) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(textToStore);
        bw.close();
        System.out.println("Succes, saving inf " + path);
    }




    public static SecretKey recuperar_secret_key(String keyString) throws Exception {

        //convert secret key from string to bytes
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        //define key specification
        // Create a SecretKey from the keyBytes
        SecretKey secKey = new SecretKeySpec(keyBytes, "AES");
        return secKey;
    }


}
