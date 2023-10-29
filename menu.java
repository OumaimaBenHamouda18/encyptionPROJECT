package encyptiondecryptionproject;

import java.io.File;
import java.util.Objects;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Scanner;

public class menu {


    static String decryption_directory= AppConfig.getDecryptionDirectory();
    static String encryption_directory= AppConfig.getEncryptionDirectory();

    public static void main(String[] args) throws Exception {






        Scanner sc = new Scanner(System.in);

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
        //ACCESS ENV VIRABLES
        String File = readPath(encryption_directory, true);
        Scanner sc = new Scanner(System.in);
        String newFile;

        System.out.println("New encypted file name: ");

        do {

            newFile = sc.next();
            if ( !newFile.contains(".txt")){
                newFile = newFile + ".txt";
                 }
            if (!checkFile(newFile, true)){
                System.out.println("This file name already exists please type another filename: ");
            }
        }while(!checkFile(newFile, true));
        EncyptionDecryptionProject.Encrypt(File, newFile);
    }

    public static void decrypt_menu() throws Exception {

        String File = readPath(decryption_directory, false);
        Scanner sc = new Scanner(System.in);
    }


    public static boolean checkFile(String file, boolean encriptar){



        String path = encryption_directory;
        if (encriptar) {
            path = encryption_directory;
        }

        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            String[] fileNames = dir.list();
            if (fileNames != null && fileNames.length > 0) {
                for (String fileName : fileNames) {
                    if (Objects.equals(fileName, file)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public static String readPath(String path, boolean encriptar) throws IndexOutOfBoundsException {
        System.out.println("path: "+path);
        File dir = new File(path);
        Scanner sc = new Scanner(System.in);
        String txt = "desencriptar";
        if (encriptar) {    txt = "encriptar";  }
            if (dir.exists() && dir.isDirectory()) {

                String[] fileNames = dir.list();

                if (fileNames != null && fileNames.length > 0) {

                    int i = 0;
                    for (String fileName : fileNames) {
                        System.out.println((i+1) + " : " + fileName);
                        i++;
                    }
                    int opcion;

                    do{
                        System.out.println("Elige el fichero que deseas " + txt + ": ");
                        opcion = sc.nextInt();
                        try{
                            System.out.println(fileNames[opcion-1]);
                        }catch(IndexOutOfBoundsException e){
                            System.out.println("Incorrect Option!");
                        }


                    }while((opcion-1)>= fileNames.length);

                    return fileNames[opcion-1];
                } else {
                    System.out.println("No exsisten archivos en este directorio");
                }
            } else {
                System.out.println("Este directorio no exsiste");
            }
        return null;
    }
}

