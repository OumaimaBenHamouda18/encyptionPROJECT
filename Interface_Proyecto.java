package encyptiondecryptionproject;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public interface Interface_Proyecto{



    //******************************************AES******************************************//


    static SecretKey initializeKey()throws Exception{
        return null;
    }
    static byte[] encrypt(String message, SecretKey secKey) throws Exception{
        return null;
    }
    static String decrypt(String encyptedMessage, SecretKey secKey) throws Exception{

        return"";
    }

    public static byte[] encode(byte[] data){
        return null;
    }

    public static byte[] hexToByte(String txt){
        return null;
    }
    static String bytesToHex(byte[] hash){
        return "";
    }



    //******************************************EncryptionDecryption******************************************//
    static String getTextFromFile(String path){
        return "";
    }

    public static void store_in_file(String textToStore, String path) throws IOException{

    }
    public static SecretKey recuperar_secret_key(String keyString) throws Exception{
        return null;
    }
    static Scanner openFile(String path) throws FileNotFoundException, EmptyFileException{
        return null;
    }


    //******************************************EncryptionDecryption******************************************//

    void encrypt_menu();

    static String choose_file_from_dir(String dirPath,String fileType){
        return "";
    }

    static String user_choose_file_with_scanner(String []fileNames,String fileType){
        return "";
    }
    static void show_files(File folderRef){

    }
}