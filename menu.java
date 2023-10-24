package encyptiondecryptionproject;

import java.io.File;
import java.util.Objects;
import java.util.Scanner;

public class menu {
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
        String File = readPath("encyptionPROJECT\\files_decrypt", true);
        Scanner sc = new Scanner(System.in);
        System.out.println("Nombre del archivo nuevo encriptado :");
        String newFile = sc.next();
        if ( !newFile.contains(".txt")){
            newFile = newFile + ".txt";
        }
        if (checkFile(newFile, true)){
            EncyptionDecryptionProject.Encrypt(File, newFile);
        }
    }
    public static void decrypt_menu() throws Exception {
        String File = readPath("encyptionPROJECT\\files_encrypt", false);
        Scanner sc = new Scanner(System.in);
    }
    public static boolean checkFile(String file, boolean encriptar){
        String path = "encyptionPROJECT\\files_decrypt";
        if (encriptar) {
            path = "encyptionPROJECT\\files_encrypt";
        }

        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            String[] fileNames = dir.list();
            if (fileNames != null && fileNames.length > 0) {
                for (String fileName : fileNames) {
                    if (Objects.equals(fileName, file)) {
                        System.out.println("Ya exsiste este nombre");
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public static String readPath(String path, boolean encriptar) {
        File dir = new File(path);
        Scanner sc = new Scanner(System.in);
        String txt = "desencriptar";
        if (encriptar) {    txt = "encriptar";  }

            if (dir.exists() && dir.isDirectory()) {

                String[] fileNames = dir.list();

                if (fileNames != null && fileNames.length > 0) {
                    System.out.println("Elige el fichero que deseas " + txt + ": ");
                    int i = 0;
                    for (String fileName : fileNames) {
                        System.out.println(i + " : " + fileName);
                        i++;
                    }
                    int opcion = sc.nextInt();
                    System.out.println(fileNames[opcion]);
                    return fileNames[opcion];
                } else {
                    System.out.println("No exsisten archivos en este directorio");
                }
            } else {
                System.out.println("Este directorio no exsiste");
            }
        return null;
    }
}


