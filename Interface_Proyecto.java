package encyptiondecryptionproject;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public interface Interface_Proyecto{

    //=============================================================================================================\\
    //                                                AES                                                          \\
    //=============================================================================================================\\

    SecretKey initializeKey() throws Exception;
    byte[] encrypt(String message, SecretKey secKey) throws Exception;
    String decrypt(String encyptedMessage, SecretKey secKey) throws Exception;
    byte[] encode(byte[] data);
    byte[] hexToByte(String txt);
    String bytesToHex(byte[] hash);

    //=============================================================================================================\\
    //                                          EncryptionDecryption                                               \\
    //=============================================================================================================\\

    Scanner openFile(String path) throws Exception;
    String getTextFromFile(String path) throws Exception;
    void store_in_file(String textToStore, String path) throws IOException;
    SecretKey recuperar_secret_key(String keyString) throws Exception;

    //=============================================================================================================\\
    //                                          MENUS Y RELACIONADOS                                               \\
    //=============================================================================================================\\

    void encrypt_menu();

    void decrypt_menu() throws Exception;
    boolean checkFile(String file, boolean encriptar);

    String choose_file_from_dir(String dirPath, String fileType);

    void show_files(File folderRef);
    String user_choose_file_with_scanner(String[] fileNames, String fileType);

    //=============================================================================================================\\
    //                                                  UNSAFE                                                     \\
    //=============================================================================================================\\

    static String Unsafe_encrypt(){
        return "";
    }
    static String Unsafe_decrypt(String encryptedText){
        return "";
    }

}