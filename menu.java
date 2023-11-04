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
                    Functions F = new Functions();
                    F.encrypt_menu(); // Llama al método encrypt_menu
                    break;
                case 2:
                    decrypt_menu();
                    break;
                case 3:
                    System.out.println("¡Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Not valid option. Try again.");
            }
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
        // Revisa si ya existe un archivo con el mismo nombre en el mismo directorio, en el caso de que si devuelve un true, la booleana
        // sirve para que se pueda usar en ambos directorios.
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

        // Verificar si el directorio existe y es un directorio válido
        if (folderRef.exists() && folderRef.isDirectory()) {
            // Obtener los nombres de archivos y directorios en el directorio
            fileNames = folderRef.list();

            // Si no hay archivos en el directorio, mostrar un mensaje
            if (fileNames.length == 0) System.out.println("There's no files here. \nEmpty directory.");

            // Si hay archivos en el directorio
            if (fileNames != null && fileNames.length > 0) {
                // Mostrar los archivos en el directorio
                show_files(folderRef);
                // Permitir al usuario elegir un archivo con un escáner, basado en el tipo de archivo
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
            // Solicitar al usuario que ingrese la opción del archivo que desea
            option = sc.nextInt() - 1;
            try {
                // Verificar si la opción está fuera de los límites de la matriz de nombres de archivos
                if (option >= fileNames.length || option < 0) {
                    System.out.println("Incorrect Option!");
                    System.out.print("Choose the file you want to " + fileType + " : ");
                }
            } catch (IndexOutOfBoundsException e) {
            }
        } while (option >= fileNames.length || option < 0);

        // Devolver el nombre del archivo seleccionado por el usuario
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



