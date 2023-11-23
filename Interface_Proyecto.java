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
    String getTextFromFile(String path, boolean encriptar) throws Exception;
    void store_in_file(String textToStore, String path) throws IOException;
    SecretKey recuperar_secret_key(String keyString) throws Exception;

    //=============================================================================================================\\
    //                                          MENUS Y RELACIONADOS                                               \\
    //=============================================================================================================\\

    void encrypt_menu() throws Exception;
    String encrypt_without_secretkey(String encrypetText);
    String encrypt_with_secret_key(String encryptedText, String newFile) throws Exception;
    void decrypt_menu() throws Exception;
    String decrypt_without_secret_key(String encryptedText) throws Exception;
    String decrypt_with_secret_key(String encryptedText) throws Exception;
    boolean checkFile(String file, boolean encriptar);
    String choose_file_from_dir(String dirPath, String fileType);
    void show_files(File folderRef);
    String user_choose_file_with_scanner(String[] fileNames, String fileType);
    void menu_PDF();
    void storePDF(String file, String dir);
    String Colores(String color);
    void Texto(String color, String Texto, boolean saltoLinea);
    int numero();

    //=============================================================================================================\\
    //                                                  UNSAFE                                                     \\
    //=============================================================================================================\\

    String Unsafe_encrypt(String textToEncrypt);
    String Unsafe_decrypt(String encryptedText) throws Exception;
}