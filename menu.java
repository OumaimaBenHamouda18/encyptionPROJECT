package encyptiondecryptionproject;

import javax.crypto.SecretKey;
import java.io.File;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;

public class menu {


    //global variables
    static String decryption_directory = AppConfig.getDecryptionDirectory();
    static String encryption_directory = AppConfig.getEncryptionDirectory();
    static String secretKeys_directory = AppConfig.getSecretKeysDirectory();
    static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) throws Exception {


        while (true) {
            System.out.println("Option menu:");
            System.out.println("1. File Encrypt");
            System.out.println("2. File Decrypt");
            System.out.println("3. Leave");
            System.out.print("Select an option: ");

            int opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    encrypt_menu();
                    break;
                case 2:
                    decrypt_menu();
                case 3:
                    System.out.println("¡Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Not valid option. Try again.");
            }
        }
    }

    public static void encrypt_menu() {
        String File = choose_file_from_dir(encryption_directory, "encrypt");
        Scanner sc = new Scanner(System.in);
        String newFile;

        System.out.println("New encrypted file name: ");

        do {

            newFile = sc.next();
            if (!newFile.contains(".txt")) {
                newFile = newFile + ".txt";
            }
            if (!checkFile(newFile, true)) {
                System.out.println("This file name already exists please type another filename: ");
            }
        } while (!checkFile(newFile, true));

        try {
            // generates the key and encrypts the text with that
            SecretKey secKey = AES.initializeKey();
            String textToEncrypt = EncyptionDecryption.getTextFromFile(encryption_directory + "\\" + File);
            byte[] test = AES.encrypt(textToEncrypt, secKey);
            String encryptedText = AES.bytesToHex(test);

            EncyptionDecryption.store_in_file(encryptedText, decryption_directory + "\\" + newFile);
            // guarda key
            byte[] keyBytes = secKey.getEncoded();
            String keyString = Base64.getEncoder().encodeToString(keyBytes);
            EncyptionDecryption.store_in_file(keyString, secretKeys_directory + "\\key_" + newFile);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }


    public static void decrypt_menu() throws Exception {

        System.out.println("Choose the file you to decrypt:");
        String fileName = choose_file_from_dir(decryption_directory, "decrypt");
        String secretKeyFileName = "";
        //leer and store the text to decrypt
        String decrptedtextfromfile = EncyptionDecryption.getTextFromFile(decryption_directory + "\\" + fileName);

        //leer and store the secret key base64 that will be used to decrypt
        System.out.println("Choose the file you want to retrieve the secret key from:");
        secretKeyFileName = choose_file_from_dir(secretKeys_directory, "retrieve the secret key from");
        System.out.print("Type the name you want to store the decrypted file with:");

        String newFileDecryptedName = sc.next();

        String keyString = EncyptionDecryption.getTextFromFile(secretKeys_directory + "\\" + secretKeyFileName);

        //generate a secret key using the base64 string converting it to an array of bytes
        SecretKey secKey = EncyptionDecryption.recuperar_secret_key(keyString);

        //decrypt
        String decryptado = AES.decrypt(decrptedtextfromfile, secKey);


        //store decrypted text with the name specified
        EncyptionDecryption.store_in_file(decryptado, encryption_directory + "\\" + newFileDecryptedName);


    }


    public static boolean checkFile(String file, boolean encriptar) {

        String path = encryption_directory;
        if (encriptar) {
            path = decryption_directory;
        }

        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            String[] fileNames = dir.list();
            if (fileNames != null) {
                for (String fileName : fileNames) {
                    if (Objects.equals(fileName, file)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    //*****************************cambiar to   choose_file_option*******************//


    public static String choose_file_from_dir(String dirPath, String fileType) {
        File folderRef = new File(dirPath);
        String[] fileNames = null;
        String file = "";
        if (folderRef.exists() && folderRef.isDirectory()) {
            fileNames = folderRef.list();
            if (fileNames.length == 0) System.out.println("There's no files to decrypt. \nFolder empty.");

            if (fileNames != null && fileNames.length > 0) {
                show_files(folderRef);
                file = user_choose_file_with_scanner(fileNames, fileType);
            } else {
                System.out.println("There's no files in this directory");
            }
        } else {
            System.out.println("This directory doesn't exist");
        }
        return file;
    }


    public static String user_choose_file_with_scanner(String[] fileNames, String fileType) {
        int option;
        do {
            option = sc.nextInt() - 1;
            try {
                if (option >= fileNames.length || option < 0) {
                    System.out.println("Incorrect Option!");
                    System.out.print("Choose the file you want to " + fileType + " : ");
                }

            } catch (IndexOutOfBoundsException e) {
            }


        } while (option >= fileNames.length || option < 0);
        return fileNames[option];

    }

    public static void show_files(File folderRef) {

        int i = 1;
        String[] fileNames = folderRef.list();
        for (String file : fileNames) {
            System.out.println(i + "-" + file);
            i++;
        }

    }


}
